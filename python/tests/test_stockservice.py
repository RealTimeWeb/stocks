import unittest
from python.stockservice import stockservice


class TestStockService(unittest.TestCase):

    # def setUp(self):
    #     stockservice.disconnect("../stockservice/cache.json")

    def test_get_stocks(self):
        stockservice.connect()

        stocks = stockservice.get_stock_information(["AAPL"])
        self.assertTrue(isinstance(stocks, dict))
        self.assertTrue('id' in stocks)
        self.assertTrue('t' in stocks)
        self.assertTrue('e' in stocks)
        self.assertTrue('l' in stocks)
        # self.assertTrue('' in stocks)
        # self.assertTrue('' in stocks)
        # self.assertTrue('' in stocks)
        # self.assertTrue('' in stocks)
        # self.assertTrue('' in stocks)
        # self.assertTrue('' in stocks)
        # self.assertTrue('' in stocks)
        # self.assertTrue('' in stocks)

    # def test_get_coord_disconnected(self):
    #     stockservice.disconnect("../stockservice/cache.json")
    #
    #     coords = stockservice.code('2200 Kraft Drive Blacksburg VA')
    #     self.assertTrue(isinstance(coords, dict))
    #     self.assertTrue('latitude' in coords)
    #     self.assertTrue('longitude' in coords)

    def test_throw_exception(self):
        stockservice.connect()

        with self.assertRaises(stockservice.StockServiceException) as context:
            stockservice.get_stock_information("")

        self.assertEqual('No valid stocks were given', context.exception.args[0])

        with self.assertRaises(stockservice.stockserviceException) as context:
            stockservice.get_stock_information(1)

        self.assertEqual('No valid stocks were given', context.exception.args[0])

        with self.assertRaises(stockservice.stockserviceException) as context:
            stockservice.get_stock_information("SDSDFSDFSDFSDFSDFSDF")

        self.assertEqual('Sorry no results found', context.exception.args[0])
