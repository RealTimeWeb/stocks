import requests
import json
import threading
from _cache import _recursively_convert_unicode_to_str, lookup
from stock import Stock
import raw_json
def connect():
    """
    Connect to the online data source in order to get up-to-date information.
    :returns: void
    """
    raw_json.connect()
def disconnect():
    """
    Connect to the local cache, so no internet connection is required.
    :returns: void
    """
    raw_json.disconnect()
def get_stock_information(ticker):
    """
    Retrieves current stock information.
    :param ticker: A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
    :type ticker: string
    :returns: list
    """
    return _recursively_convert_unicode_to_str(json.loads(raw_json.get_stock_information(ticker)))

def get_stock_information_async(callback, error_callback, ticker):
    """
    Asynchronous version of get_stock_information
    :param callback: Function that consumes the data (a list) returned on success.
    :type callback: function
    :param error_callback: Function that consumes the exception returned on failure.
    :type error_callback: function
    :param ticker: A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
    :type ticker: string
    :returns: void
    """
    def server_call(callback, error_callback, ticker):
        """
        Internal closure to thread this call.
        :param callback: Function that consumes the data (a list) returned on success.
        :type callback: function
        :param error_callback: Function that consumes the exception returned on failure.
        :type error_callback: function
        :param ticker: A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
        :type ticker: string
        :returns: void
        """
        try:
            callback(get_stock_information(ticker))
        except Exception, e:
            error_callback(e)
    threading.Thread(target=server_call, args = (ticker,)).start()

