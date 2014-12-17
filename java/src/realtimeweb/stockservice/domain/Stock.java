package realtimeweb.stockservice.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/**
 * A structured representation of stock information, including ticker symbol, latest sale price, and price change since yesterday.
 */
public class Stock {
	
    private Double percent_Change;
    private String exchange;
    private String last_Trade_Date;
    private String last_Trade_Time;
    private Double last;
    private String ticker;
    private Long id;
    private Double change;
    
    
    /*
     * @return The percent price change since yesterday.
     */
    public Double getPercent_Change() {
        return this.percent_Change;
    }
    
    /*
     * @param The percent price change since yesterday.
     * @return Double
     */
    public void setPercent_Change(Double percent_Change) {
        this.percent_Change = percent_Change;
    }
    
    /*
     * @return The name of the exchange (e.g. NASDAQ)
     */
    public String getExchange() {
        return this.exchange;
    }
    
    /*
     * @param The name of the exchange (e.g. NASDAQ)
     * @return String
     */
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    
    /*
     * @return The entire date of the last trade.
     */
    public String getLast_Trade_Date() {
        return this.last_Trade_Date;
    }
    
    /*
     * @param The entire date of the last trade.
     * @return String
     */
    public void setLast_Trade_Date(String last_Trade_Date) {
        this.last_Trade_Date = last_Trade_Date;
    }
    
    /*
     * @return The time of the last trade.
     */
    public String getLast_Trade_Time() {
        return this.last_Trade_Time;
    }
    
    /*
     * @param The time of the last trade.
     * @return String
     */
    public void setLast_Trade_Time(String last_Trade_Time) {
        this.last_Trade_Time = last_Trade_Time;
    }
    
    /*
     * @return The latest sale price for this stock.
     */
    public Double getLast() {
        return this.last;
    }
    
    /*
     * @param The latest sale price for this stock.
     * @return Double
     */
    public void setLast(Double last) {
        this.last = last;
    }
    
    /*
     * @return The Ticker Symbol (e.g. AAPL)
     */
    public String getTicker() {
        return this.ticker;
    }
    
    /*
     * @param The Ticker Symbol (e.g. AAPL)
     * @return String
     */
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    
    /*
     * @return The unique ID number for this ticker symbol
     */
    public Long getId() {
        return this.id;
    }
    
    /*
     * @param The unique ID number for this ticker symbol
     * @return Integer
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /*
     * @return The price change since yesterday.
     */
    public Double getChange() {
        return this.change;
    }
    
    /*
     * @param The price change since yesterday.
     * @return Double
     */
    public void setChange(Double change) {
        this.change = change;
    }
    
	
	/**
	 * Creates a string based representation of this Stock.
	
	 * @return String
	 */
	public String toString() {
		return "Stock[" +percent_Change+", "+exchange+", "+last_Trade_Date+", "+last_Trade_Time+", "+last+", "+ticker+", "+id+", "+change+"]";
	}
	
	/**
	 * Internal constructor to create a Stock from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    public Stock(Map<String, Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
            this.percent_Change = Double.parseDouble(raw.get("cp").toString());
            this.exchange = raw.get("e").toString();
            this.last_Trade_Date = raw.get("lt").toString();
            this.last_Trade_Time = raw.get("ltt").toString();
            this.last = Double.parseDouble(raw.get("l").toString());
            this.ticker = raw.get("t").toString();
            this.id = Long.parseLong(raw.get("id").toString());
            this.change = Double.parseDouble(raw.get("c").toString());
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Stock; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Stock; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}	
}