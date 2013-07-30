class Stock(object):
    """
    A structured representation of stock information, including ticker symbol, latest sale price, and price change since yesterday.
    """
    def __init__(self, id, ticker, exchange, last, last_trade_date, last_trade_time, change, percent_change):
        """
        Creates a new Stock
        
        :param self: This object
        :type self: Stock
        :param id: The unique ID number for this ticker symbol
        :type id: int
        :param ticker: The Ticker Symbol (e.g. AAPL)
        :type ticker: string
        :param exchange: The name of the exchange (e.g. NASDAQ)
        :type exchange: string
        :param last: The latest sale price for this stock.
        :type last: float
        :param last_trade_date: The entire date of the last trade.
        :type last_trade_date: string
        :param last_trade_time: The time of the last trade.
        :type last_trade_time: string
        :param change: The price change since yesterday.
        :type change: float
        :param percent_change: The percent price change since yesterday.
        :type percent_change: float
        :returns: Stock
        """
        self.id = id
        self.ticker = ticker
        self.exchange = exchange
        self.last = last
        self.last_trade_date = last_trade_date
        self.last_trade_time = last_trade_time
        self.change = change
        self.percent_change = percent_change
    
    @staticmethod
    def _from_json(json_data):
        """
        Creates a Stock from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Stock
        """
        return Stock(json_data['id'],
                    json_data['t'],
                    json_data['e'],
                    json_data['l'],
                    json_data['lt'],
                    json_data['ltt'],
                    json_data['c'],
                    json_data['cp'])