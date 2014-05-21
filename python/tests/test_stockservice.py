import unittest
from python.stockservice import stockservice


class TestStockService(unittest.TestCase):

    def test_get_stock_online(self):
        stockservice.connect()

        # Mid Day Trading
        # keys = ['avvo', 'beta', 'c', 'c_fix', 'ccol', 'cp', 'cp_fix', 'delay',
        #         'e', 'eo', 'eps', 'fwpe', 'hi', 'hi52', 'id', 'inst_own', 'l',
        #         'l_cur', 'l_fix', 'lo', 'lo52', 'lt', 'lt_dts', 'ltt', 'mc',
        #         'name', 'op', 'pcls_fix', 'pe', 's', 'shares', 't', 'type', 'vo']

        keys = ['c', 'c_fix', 'ccol', 'cp', 'cp_fix', 'e', 'id', 'l', 'l_cur',
                'l_fix', 'lt', 'lt_dts', 'ltt', 'pcls_fix', 's', 't']

        # Test getting one stock
        stocks = stockservice.get_stock_information("AAPL")
        self.assertTrue(isinstance(stocks, dict))

        # Assert all of the keys are in the stock
        intersection = set(keys).intersection(stocks)
        self.assertEqual(16, len(intersection))

        # Test getting two stocks
        stocks = stockservice.get_stock_information("AAPL,GOOG")
        self.assertTrue(isinstance(stocks, list))
        self.assertTrue(len(stocks) == 2)

        # Assert all of the keys are in the stocks
        for dict_res in stocks:
            intersection = set(keys).intersection(dict_res)
            self.assertEqual(16, len(intersection))

    def test_get_stock_offline(self):
        stockservice.disconnect("../stockservice/cache.json")

        # Mid Day Trading
        keys = ['c', 'c_fix', 'ccol', 'cp', 'cp_fix', 'e', 'id', 'l', 'l_cur',
                'l_fix', 'lt', 'lt_dts', 'ltt', 'pcls_fix', 's', 't']

        # Test getting one stock
        stocks = stockservice.get_stock_information("AAPL")
        self.assertTrue(isinstance(stocks, dict))

        # Assert all of the keys are in the stock
        intersection = set(keys).intersection(stocks)
        self.assertEqual(16, len(intersection))

    def test_throw_exception(self):
        stockservice.connect()

        with self.assertRaises(stockservice.StockServiceException) as context:
            stockservice.get_stock_information(["AAPL"])

        self.assertEqual('Please enter a string of Stock Tickers', context.exception.args[0])

        with self.assertRaises(stockservice.StockServiceException) as context:
            stockservice.get_stock_information(1)

        self.assertEqual('Please enter a string of Stock Tickers', context.exception.args[0])

        with self.assertRaises(stockservice.StockServiceException) as context:
            stockservice.get_stock_information("INVALID_STOCK")

        self.assertEqual('Make sure you entered a valid stock option', context.exception.args[0])
