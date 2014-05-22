from __future__ import print_function
import sys
import json

HEADER = {'User-Agent': 'RealTimeWeb Stock library for educational purposes'}
PYTHON_3 = sys.version_info >= (3, 0)

if PYTHON_3:
    import urllib.error
    import urllib.request as request
    from urllib.parse import quote_plus
else:
    import urllib2
    from urllib import quote_plus

################################################################################
# Auxilary
################################################################################


def _parse_float(value, default=0.0):
    """
    Attempt to cast *value* into a float, returning *default* if it fails.
    """
    if value is None:
        return default
    try:
        return float(value)
    except ValueError:
        return default


def _iteritems(_dict):
    """
    Internal method to factor-out Py2-to-3 differences in dictionary item
        iterator methods

    :param dict _dict: the dictionary to parse
    :returns: the iterable dictionary
    """
    if PYTHON_3:
        return _dict.items()
    else:
        return _dict.iteritems()


def _urlencode(query, params):
    """
    Internal method to combine the url and params into a single url string.

    :param str query: the base url to query
    :param dict params: the parameters to send to the url
    :returns: a *str* of the full url
    """
    return query + '?' + '&'.join(key+'='+quote_plus(str(value))
                                  for key, value in _iteritems(params))


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


################################################################################
# Cache
################################################################################

_CACHE = {}
_CACHE_COUNTER = {}
_EDITABLE = False
_CONNECTED = True
_PATTERN = "repeat"


def _start_editing(pattern="repeat"):
    """
    Start adding seen entries to the cache. So, every time that you make a request,
    it will be saved to the cache. You must :ref:`_save_cache` to save the
    newly edited cache to disk, though!
    """
    global _EDITABLE, _PATTERN
    _EDITABLE = True
    _PATTERN = pattern


def _stop_editing():
    """
    Stop adding seen entries to the cache.
    """
    global _EDITABLE
    _EDITABLE = False


def _add_to_cache(key, value):
    """
    Internal method to add a new key-value to the local cache.
    :param str key: The new url to add to the cache
    :param str value: The HTTP response for this key.
    :returns: void
    """
    if key in _CACHE:
        _CACHE[key].append(value)
    else:
        _CACHE[key] = [_PATTERN, value]
        _CACHE_COUNTER[key] = 0


def _clear_key(key):
    """
    Internal method to remove a key from the local cache.
    :param str key: The url to remove from the cache
    """
    if key in _CACHE:
        del _CACHE[key]


def _save_cache(filename="cache.json"):
    """
    Internal method to save the cache in memory to a file, so that it can be used later.

    :param str filename: the location to store this at.
    """
    with open(filename, 'w') as f:
        json.dump({"data": _CACHE, "metadata": ""}, f)


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

################################################################################
# Exceptions
################################################################################


class StockServiceException(Exception):
    pass

################################################################################
# Domain Objects
################################################################################


class Stock(object):

    """
    A Stock object containing information regarding a ticker
    """
    def __init__(self, chg_num=None, chg_per=None, ex_name=None,
                 lst_trd_price=None, lst_trd_date_time=None, tick=None):

        self.change_number = _parse_float(chg_num)              # c
        self.change_percentage = _parse_float(chg_per)          # cp
        self.exchange_name = ex_name                            # e
        self.last_trade_price = _parse_float(lst_trd_price)     # l
        self.last_trade_date_and_time = lst_trd_date_time       # lt
        self.ticker = tick                                      # s

    def __unicode__(self):
        string = """
        <Stock Change: {}, Change (Percentage): {}, Exchange Name: {},
        Last Trade Price: {}, Last Trade Date and Time: {}, Ticker: {}>
        """
        return string.format(self.change_number,
                             self.change_percentage,
                             self.exchange_name,
                             self.last_trade_price,
                             self.last_trade_date_and_time,
                             self.ticker)

    def __repr__(self):
        string = self.__unicode__()

        if not PYTHON_3:
            return string.encode('utf-8')

        return string

    def __str__(self):
        string = self.__unicode__()

        if not PYTHON_3:
            return string.encode('utf-8')

        return string

    def _to_dict(self):
        return {'change_number': self.change_number,
                'change_percentage': self.change_percentage,
                'exchange_name': self.exchange_name,
                'last_trade_price': self.last_trade_price,
                'last_trade_date_and_time': self.last_trade_date_and_time,
                'ticker': self.ticker}

    @staticmethod
    def _from_json(json_data):
        # Assuming json_data is a dict
        if json_data is None:
            return Stock()
        try:
            chg_num = _parse_float(json_data[0]['change_number'])
            chg_per = _parse_float(json_data[0]['change_percentage'])
            ex_name = json_data[0]['exchange_name']
            lst_trd_price = _parse_float(json_data[0]['last_trade_price'])
            lst_trd_date_time = json_data[0]['last_trade_date_and_time']
            tick = json_data[0]['ticker']
            stock = Stock(chg_num, chg_per, ex_name, lst_trd_price, lst_trd_date_time, tick)
            return stock
        except KeyError:
            raise StockServiceException("The given information was incomplete.")


################################################################################
# Service Methods
################################################################################


def _fetch_stock_info(params):
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

    result = result.replace("// ", "")  # Remove Strange Double Slashes
    result = result.replace("\n", "")  # Remove All New Lines

    try:
        json_res = json.loads(result)
    except ValueError:
        raise StockServiceException("Internal Error")

    dict = json_res[0]

    dict['change_number'] = dict.pop('c')
    dict['change_percentage'] = dict.pop('cp')
    dict['exchange_name'] = dict.pop('e')
    dict['last_trade_price'] = dict.pop('l')
    dict['last_trade_date_and_time'] = dict.pop('lt')
    dict['ticker'] = dict.pop('t')


    if _CONNECTED and _EDITABLE:
        _add_to_cache(query, result)

    return json_res


def get_stock_information(ticker):
    """
    Retrieves current stock information.

    """
    if not isinstance(ticker, str):
        raise StockServiceException("Please enter a string of Stock Tickers")

    params = {'q': ticker}
    json_res = _fetch_stock_info(params)
    stock = Stock._from_json(json_res)
    return stock._to_dict()