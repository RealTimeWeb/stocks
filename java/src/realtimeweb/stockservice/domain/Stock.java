package realtimeweb.stockservice.domain;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * A structured representation of stock information, including ticker symbol, latest sale price, and price change since yesterday.
 */
public class Stock {
	
	
	private int id;
	private String ticker;
	private String exchange;
	private double last;
	private String last_Trade_Date;
	private String last_Trade_Time;
	private double change;
	private double percent_Change;
	
	
	/**
	 * The unique ID number for this ticker symbol
	
	 * @return int
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @param id The unique ID number for this ticker symbol
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * The Ticker Symbol (e.g. AAPL)
	
	 * @return String
	 */
	public String getTicker() {
		return this.ticker;
	}
	
	/**
	 * 
	 * @param ticker The Ticker Symbol (e.g. AAPL)
	 */
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	/**
	 * The name of the exchange (e.g. NASDAQ)
	
	 * @return String
	 */
	public String getExchange() {
		return this.exchange;
	}
	
	/**
	 * 
	 * @param exchange The name of the exchange (e.g. NASDAQ)
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	
	/**
	 * The latest sale price for this stock.
	
	 * @return double
	 */
	public double getLast() {
		return this.last;
	}
	
	/**
	 * 
	 * @param last The latest sale price for this stock.
	 */
	public void setLast(double last) {
		this.last = last;
	}
	
	/**
	 * The entire date of the last trade.
	
	 * @return String
	 */
	public String getLast_Trade_Date() {
		return this.last_Trade_Date;
	}
	
	/**
	 * 
	 * @param last_Trade_Date The entire date of the last trade.
	 */
	public void setLast_Trade_Date(String last_Trade_Date) {
		this.last_Trade_Date = last_Trade_Date;
	}
	
	/**
	 * The time of the last trade.
	
	 * @return String
	 */
	public String getLast_Trade_Time() {
		return this.last_Trade_Time;
	}
	
	/**
	 * 
	 * @param last_Trade_Time The time of the last trade.
	 */
	public void setLast_Trade_Time(String last_Trade_Time) {
		this.last_Trade_Time = last_Trade_Time;
	}
	
	/**
	 * The price change since yesterday.
	
	 * @return double
	 */
	public double getChange() {
		return this.change;
	}
	
	/**
	 * 
	 * @param change The price change since yesterday.
	 */
	public void setChange(double change) {
		this.change = change;
	}
	
	/**
	 * The percent price change since yesterday.
	
	 * @return double
	 */
	public double getPercent_Change() {
		return this.percent_Change;
	}
	
	/**
	 * 
	 * @param percent_Change The percent price change since yesterday.
	 */
	public void setPercent_Change(double percent_Change) {
		this.percent_Change = percent_Change;
	}
	
	
	
	/**
	 * A structured representation of stock information, including ticker symbol, latest sale price, and price change since yesterday.
	
	 * @return String
	 */
	public String toString() {
		return "Stock[" + id + ", " + ticker + ", " + exchange + ", " + last + ", " + last_Trade_Date + ", " + last_Trade_Time + ", " + change + ", " + percent_Change + "]";
	}
	
	/**
	 * Internal constructor to create a Stock from a Json representation.
	 * @param json The raw json data that will be parsed.
	 * @param gson The Gson parser. See <a href='https://code.google.com/p/google-gson/'>https://code.google.com/p/google-gson/</a> for more information.
	 * @return 
	 */
	public  Stock(JsonObject json, Gson gson) {
		this.id = json.get("id").getAsInt();
		this.ticker = json.get("t").getAsString();
		this.exchange = json.get("e").getAsString();
		this.last = json.get("l").getAsDouble();
		this.last_Trade_Date = json.get("lt").getAsString();
		this.last_Trade_Time = json.get("ltt").getAsString();
		this.change = json.get("c").getAsDouble();
		this.percent_Change = json.get("cp").getAsDouble();
	}
	
	/**
	 * Regular constructor to create a Stock.
	 * @param id The unique ID number for this ticker symbol
	 * @param ticker The Ticker Symbol (e.g. AAPL)
	 * @param exchange The name of the exchange (e.g. NASDAQ)
	 * @param last The latest sale price for this stock.
	 * @param last_Trade_Date The entire date of the last trade.
	 * @param last_Trade_Time The time of the last trade.
	 * @param change The price change since yesterday.
	 * @param percent_Change The percent price change since yesterday.
	 * @return 
	 */
	public  Stock(int id, String ticker, String exchange, double last, String last_Trade_Date, String last_Trade_Time, double change, double percent_Change) {
		this.id = id;
		this.ticker = ticker;
		this.exchange = exchange;
		this.last = last;
		this.last_Trade_Date = last_Trade_Date;
		this.last_Trade_Time = last_Trade_Time;
		this.change = change;
		this.percent_Change = percent_Change;
	}
	
}
