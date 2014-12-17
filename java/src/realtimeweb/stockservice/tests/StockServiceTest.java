package realtimeweb.stockservice.tests;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Test;

import realtimeweb.stickyweb.EditableCache;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidPostArguments;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidQueryString;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;
import realtimeweb.stockservice.StockService;
import realtimeweb.stockservice.domain.Stock;

public class StockServiceTest {
	
	private void assertAllStockFieldsNotNull(Stock s){
		assertNotNull(s.getId());
		assertNotNull(s.getExchange());
		assertNotNull(s.getTicker());
		assertNotNull(s.getLast());
		assertNotNull(s.getLast_Trade_Date());
		assertNotNull(s.getLast_Trade_Time());
		assertNotNull(s.getPercent_Change());
	}
	
	String[] tickers = {"aapl", "amzn","fb","ibm"};
	StockService stockService = new StockService();
	
	@Test
	public void testStockServiceOnline() {
		
		for(String t : tickers){
			Stock stock = stockService.getStockInfo(t);
			assertAllStockFieldsNotNull(stock);
		}
	}
	
	@Test
	public void testStockServiceCache() {
		EditableCache recording = new EditableCache();
		
		//recording
		for(String t : tickers){
			try {
				recording.addData(stockService.getStockInfoRequest(t));
			} catch (StickyWebNotInCacheException | StickyWebInternetException
					| StickyWebInvalidQueryString
					| StickyWebInvalidPostArguments e) {
				e.printStackTrace();
			}
		}
		//saving
		try {
			recording.saveToStream(new FileOutputStream("test-cache.json"));
		} catch (StickyWebDataSourceNotFoundException
				| StickyWebDataSourceParseException
				| StickyWebLoadDataSourceException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//test retrieving from cache
	
			StockService StockServiceFromCache = new StockService("test-cache.json");
			for(String t : tickers){
				Stock stock = StockServiceFromCache.getStockInfo(t);
				assertAllStockFieldsNotNull(stock);
			}
	}

}
