import unittest
from python.stockservice import stockservice


class TestStockService(unittest.TestCase):

    def test_get_stock_online(self):
        stockservice.connect()

        keys = ['change_number', 'change_percentage', 'exchange_name',
                'last_trade_date_and_time', 'last_trade_price', 'ticker']

        # Test getting one stock
        stock = stockservice.get_stock_information("AAPL")
        self.assertTrue(isinstance(stock, dict))

        # Assert all of the keys are in the stock
        intersection = set(keys).intersection(stock)
        self.assertEqual(6, len(intersection))

    def test_get_stock_offline(self):
        stockservice.disconnect("../stockservice/cache.json")

        keys = ['change_number', 'change_percentage', 'exchange_name',
                'last_trade_date_and_time', 'last_trade_price', 'ticker']

        # Test getting one stock
        stock = stockservice.get_stock_information("AAPL")
        self.assertTrue(isinstance(stock, dict))

        # Assert all of the keys are in the stock
        intersection = set(keys).intersection(stock)
        self.assertEqual(6, len(intersection))

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

    def test_get_json(self):

        appl = stockservice.Stock(-1.16, -0.19, 'NASDAQ', 603.55, 'May 21, 11:26AM EDT','AAPL')
        appl_dict = appl._to_dict()

        stockservice.disconnect("../stockservice/cache.json")
        json_res = stockservice._fetch_stock_info({'q': 'AAPL'})
        cache_stock = stockservice.Stock._from_json(json_res)
        cache_dict = cache_stock._to_dict()

        self.assertDictEqual(cache_dict, appl_dict)

