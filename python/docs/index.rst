.. stockservice documentation master file, created by
   sphinx-quickstart on Tue Jul 30 14:19:10 2013.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.


Welcome to Stock Service's documentation!
=========================================

The Stock Service library offers access to the Google's Finance feed.
This is stock data available from U.S. exchanges such as NASDAQ and NYSE and
covers a wide variety of stocks.

The information that is available includes the change for a stock since the
opening bell, the percentage change for a stock since the opening bell, the
exchange that a stock is listed under, the last trade price for a stock,
the last trade date and time for a stock, and the ticker name for a stock

Note that this data stream has a very high velocity - if you check every five
 minutes, you'll find it may have already changed.

>>> import stockservice

You can get information for any stock, for example let's get information for Apple

>>> stock_dict = stockservice.get_stock_information("AAPL")
>>> stock_dict
{'last_trade_date_and_time': u'May 22, 11:25AM EDT', 'ticker_name': u'AAPL', 'last_trade_price': 605.51, 'exchange_name': u'NASDAQ', 'change_percentage': -0.13, 'change_number': -0.8}

.. Other Possible Fields
    id              Internal Google Security ID?
    t               Ticker
    e               Exchange Name
    l               <<< (Regular Hours) Last Trade Price (No Currency Sign) >>>
    l_cur           <<< (Regular Hours) Last Trade Price with Currency (Mutual Funds Will Always Display The Currency Sign and any other stock or index not using US currency will display the currency sign) >>>
    s               <<< Status of Information Fetched (0 - During Market Information, 1 - Pre Market Information, 2 - Post Market Information) >>>
    ltt             (Regular Hours) Last Trade Time
    lt              (Regular Hours) Last Trade Date and Time
    c               (Regular Hours) Change - Formatted with +/-
    cp              (Regular Hours) Change - Displayed As Percentage
    ccol            <<< (Regular Hours) Current Color? | chg - In The Green, chr - In the Red >>>
    eo
    delay           Delay Time ("" Represents Real-Time, 15 Represents Delay of 15 Minutes, Etc.)
    op              Opening (Depends on Status)
    hi              High (Depends on Status)
    lo              Low (Depends on Status)
    vo              Volume (Depends on Status)
    avvo            Average Volume
    hi52            High in 52 Weeks
    lo52            Low in 52 Weeks
    mc              Market Cap
    pe              Pricing / Earnings Ratio
    fwpe
    beta            Beta
    eps             Earnings Per Share
    shares          Shares Outstanding
    inst_own        Institutional Ownership
    name            Name of the Company
    type            Type

The built-in cache allows you to work online:

>>> stockservice.connect() # unnecessary: default is connected

or offline:

>>> stockservice.disconnect()
>>> stockservice.get_stock_information("AAPL")
{'last_trade_date_and_time': u'May 21, 11:26AM EDT', 'ticker_name': u'AAPL', 'last_trade_price': 603.55, 'exchange_name': u'NASDAQ', 'change_percentage': -0.19, 'change_number': -1.16}

But remember there must be data in the cache already!

>>> stock = stockservice.get_stock_information("AAPL")
stockservice.StockServiceException: There were no results

Populating the cache
^^^^^^^^^^^^^^^^^^^^

Say you want to add Apple's stock (AAPL) to the cache

>>> stockservice._start_editing()
>>> stock = stockservice.get_stock_information("AAPL")
>>> stockservice._save_cache()

Now the file "cache.json" file will have an entry for ("AAPL"), and
you can use that as an input to the function when disconnected.

You can also create a different cache file by passing a filename to the
_save_cache() method, and use that cache by passing its name to the
disconnect() method.

For example, this will populate a file called "goog.json", which will contain
stock information for google

>>> stockservice._start_editing()
>>> goog_stock = stockservice.get_stock_information("GOOG")
>>> stockservice._save_cache('goog.json')

To use that cached file, specify the json file name when you call disconnect():

>>> stockservice.disconnect("goog.json")

Finally, you can put multiple entries into the cache for a given input, simulating multiple calls. These items will be appended. If the cache runs out, it will start returning empty reports.

>>> stockservice.connect()
>>> stockservice._start_editing()
>>> goog_stock = stockservice.get_stock_information("GOOG")
>>> aapl_stock = stockservice.get_stock_information("AAPL")
>>> stockservice._save_cache()
>>> stockservice.disconnect()
>>> stockservice.get_stock_information("GOOG")
{'last_trade_date_and_time': u'May 22, 12:40PM EDT', 'ticker_name': u'GOOG', 'last_trade_price': 542.66, 'exchange_name': u'NASDAQ', 'change_percentage': 0.69, 'change_number': 3.72}
>>> stockservice.get_stock_information("AAPL")
{'last_trade_date_and_time': u'May 21, 11:26AM EDT', 'ticker_name': u'AAPL', 'last_trade_price': 603.55, 'exchange_name': u'NASDAQ', 'change_percentage': -0.19, 'change_number': -1.16}


Exceptions
----------

.. autoexception:: stockservice.StockServiceException


.. Classes
.. -------

.. .. autoclass:: stockservice.Stock

Methods
-------

.. autofunction:: stockservice._iteritems(_dict)

.. autofunction:: stockservice._get(url)

.. autofunction:: stockservice._urlencode(query, params)

.. autofunction:: stockservice._lookup(key)

.. autofunction:: stockservice._recursively_convert_unicode_to_str(input)

.. autofunction:: stockservice.connect()

.. autofunction:: stockservice.disconnect()

.. autofunction:: stockservice._fetch_stock_info(params)

.. autofunction:: stockservice.get_stock_information(tickers)