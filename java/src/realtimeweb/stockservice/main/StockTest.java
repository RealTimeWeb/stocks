package realtimeweb.stockservice.main;

import realtimeweb.stockservice.json.JsonStockService;
import realtimeweb.stockservice.regular.StockService;
import realtimeweb.stockservice.structured.StructuredStockService;

public class StockTest {
	public static void main(String[] args) {
		JsonStockService json = JsonStockService.getInstance();
		json.connect();
		try {
			System.out.println(json.getStockInformation("AAPL"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StructuredStockService structured = StructuredStockService.getInstance();
		try {
			System.out.println(structured.getStockInformation("AAPL"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StockService stocks = StockService.getInstance();
		try {
			System.out.println(stocks.getStockInformation("AAPL"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
