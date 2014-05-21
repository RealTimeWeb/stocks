from __future__ import print_function
import sys
import json

HEADER = {'User-Agent': 'RealTimeWeb Stock library for educational purposes'}
PYTHON_3 = sys.version_info >= (3, 0)

_CACHE = {}
_CACHE_COUNTER = {}
_CONNECTED = False

if PYTHON_3:
    import urllib.error
    import urllib.request as request
    from urllib.parse import quote_plus
else:
    import urllib2
    from urllib import quote_plus

# Helpers to Copy into Other Libraries


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


def connect():
    """
    Connect to the online data source in order to get up-to-date information.

    :returns: void
    """
    global _CONNECTED
    _CONNECTED = True


def disconnect(filename="./cache.json"):
    """
    Connect to the local cache, so no internet connection is required.

    :returns: void
    """
    global _CONNECTED, _CACHE
    try:
        with open(filename, 'r') as f:
            _CACHE = _recursively_convert_unicode_to_str(json.load(f))['data']
    except (OSError, IOError) as e:
        raise StockServiceException("The cache file '{}' was not found.".format(filename))
    for key in _CACHE.keys():
        _CACHE_COUNTER[key] = 0
    _CONNECTED = False

# Library Specific Functions and Classes


class StockServiceException(Exception):
    pass


def _send_query(params):
    """
    Internal method to form and query the server

    :param dict params: the parameters to pass to the server
    :returns: the JSON response object
    """
    baseurl = 'https://www.google.com/finance/info'
    query = _urlencode(baseurl, params)

    if PYTHON_3:
        try:
            result = _get(query) if _CONNECTED else _lookup(query)
        except urllib.error.HTTPError:
            raise StockServiceException("Make sure you entered a valid stock option")
    else:
        try:
            result = _get(query) if _CONNECTED else _lookup(query)
        except urllib2.HTTPError:
            raise StockServiceException("Make sure you entered a valid stock option")

    if not result:
        raise StockServiceException("There were no results")

    result = result.replace("// ", "") # Remove Strange Double Slashes
    result = result.replace("\n", "") # Remove All New Lines

    json_res = json.loads(result)

    # _get returns a string and json loads turns it into a list
    # _lookup returns a string and json loads turns it into a dict
    if (isinstance(json_res, list)):
        return json_res[0]
    elif (isinstance(json_res, dict)):
        return json_res
    else:
        raise StockServiceException("There was an internal error")


def _get_stock_dict(json_res):
    """
    Internal method to return the dict from the JSON response

    :returns: a *dict* of the JSON response
    """

    if not isinstance(json_res, dict):
        raise StockServiceException("There was an internal error")

    return json_res


def get_stock_information(tickers):
    """
    Retrieves current stock information.

    :param ticker: A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
    :type ticker: string
    :returns: dict
    """
    if not isinstance(tickers, str):
        raise StockServiceException("Please enter a string of Stock Tickers")

    params = {'q': tickers}
    json_res = _send_query(params)

    return _get_stock_dict(json_res)
