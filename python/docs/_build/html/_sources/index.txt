.. stockservice documentation master file, created by
   sphinx-quickstart on Tue Jul 30 14:19:10 2013.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.



Welcome to stockservice's documentation!
========================================

Exceptions
----------

.. autoexception:: stockservice.StockServiceException


Classes
-------
.. autoclass:: stockservice.Stock

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