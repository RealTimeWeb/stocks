from __future__ import print_function
import sys
import json

HEADER = {'User-Agent': 'RealTimeWeb Geocode library for educational purposes'}
PYTHON_3 = sys.version_info >= (3, 0)

_CACHE = {}
_CACHE_COUNTER = {}
_CONNECTED = False

if PYTHON_3:
    import urllib.request as request
    from urllib.parse import quote_plus
else:
    import urllib2
    from urllib import quote_plus


class StockServiceException(Exception):
    pass


def _iteritems(dict_):
    """
    Internal method to factor-out Py2-to-3 differences in dictionary item
        iterator methods

    :param dict dict_: the dictionary to parse
    :returns: the iterable dictionary
    """
    if PYTHON_3:
        return dict_.items()
    else:
        return dict_.iteritems()


def _get(url):
    """
    Internal method to convert a URL into it's response (a *str*).

    :param str url: the url to request a response from
    :returns: the *str* response
    """
    if PYTHON_3:
        req = request.Request(url, headers=HEADER)
        response = request.urlopen(req)
        return response.read().decode('utf-8')
    else:
        req = urllib2.Request(url, headers=HEADER)
        response = urllib2.urlopen(req)
        return response.read()


def _urlencode(query, params):
    """
    Internal method to combine the url and params into a single url string.

    :param str query: the base url to query
    :param dict params: the parameters to send to the url
    :returns: a *str* of the full url
    """
    return query + '?' + '&'.join(key+'='+quote_plus(str(value))
                                  for key, value in _iteritems(params))


def _recursively_convert_unicode_to_str(input):
    """
    Force the given input to only use `str` instead of `bytes` or `unicode`.
    This works even if the input is a dict, list,
    """
    if isinstance(input, dict):
        return {_recursively_convert_unicode_to_str(key): _recursively_convert_unicode_to_str(value) for key, value in input.items()}
    elif isinstance(input, list):
        return [_recursively_convert_unicode_to_str(element) for element in input]
    elif not PYTHON_3:
        return input.encode('utf-8')
    else:
        return input


def _lookup(key):
    """
    Internal method that looks up a key in the local cache.
    :param key: Get the value based on the key from the cache.
    :type key: string
    :returns: void
    """
    if key not in _CACHE:
        return ""
    if _CACHE_COUNTER[key] >= len(_CACHE[key][1:]):
        if _CACHE[key][0] == "empty":
            return ""
        elif _CACHE[key][0] == "repeat" and _CACHE[key][1:]:
            return _CACHE[key][-1]
        elif _CACHE[key][0] == "repeat":
            return ""
        else:
            _CACHE_COUNTER[key] = 1
    else:
        _CACHE_COUNTER[key] += 1
    if _CACHE[key]:
        return _CACHE[key][_CACHE_COUNTER[key]]
    else:
        return ""


def connect():
    """
    Connect to the online data source in order to get up-to-date information.
    :returns: void
    """
    global _CONNECTED
    _CONNECTED = True

def _form_query(params):
    """
    Internal method to form and query the server

    :param dict params: the parameters to pass to the server
    :returns: a *dict* of the JSON response
    """
    baseurl = 'https://www.google.com/finance/info'
    query = _urlencode(baseurl, params)
    query = ''.join((query, '&sensor=false'))

    result = _get(query) if _CONNECTED else _lookup(query)

    result = result.replace("// ", "") # Remove Strange Double Slashes in Google Finance
    result = result.replace("\n", "") # Remove All New Lines ..

    json_res = json.loads(result)
    return json_res


def get_stock_information(tickers):
    """
    Retrieves current stock information.

    :param ticker: A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
    :type ticker: string
    :returns: string
    """
    if isinstance(tickers, list):
        tickers = ','.join(tickers)

    params = {'infotype': 'infoquoteall', 'q': tickers}

    json_res = _form_query(params)

    print(json_res)

    #
    # if _using_cache:
    #     result = cache.lookup(("http://www.google.com/finance/info") + "%{q=" + ticker+ "}""%{client=" + "iq"+ "}")
    #     return result
    # else:
    #     result = requests.get("http://www.google.com/finance/info", params = {"q" : ticker, "client" : "iq"})
    #     return result.text