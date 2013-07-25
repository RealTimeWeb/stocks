package realtimeweb.stockservice.regular;

import realtimeweb.stockservice.main.AbstractStockService;
import realtimeweb.stockservice.json.JsonStockService;
import realtimeweb.stockservice.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import realtimeweb.stockservice.domain.Stock;
import realtimeweb.stockservice.json.JsonGetStockInformationListener;

/**
 * Used to get data as classes.
 */
public class StockService implements AbstractStockService {
	private static StockService instance;
	private JsonStockService jsonInstance;
	private Gson gson;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  StockService() {
		this.jsonInstance = JsonStockService.getInstance();
		this.gson = new Gson();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return StockService
	 */
	public static StockService getInstance() {
		if (instance == null) {
			synchronized (StockService.class) {
				if (instance == null) {
					instance = new StockService();
				}
			}
			
		}
		return instance;
	}
	
	/**
	 * Establishes a connection to the online service. Requires an internet connection.
	
	 */
	@Override
	public void connect() {
		jsonInstance.connect();
	}
	
	/**
	 * Establishes that Business Search data should be retrieved locally. This does not require an internet connection.<br><br>If data is being retrieved locally, you must be sure that your parameters match locally stored data. Otherwise, you will get nothing in return.
	
	 */
	@Override
	public void disconnect() {
		jsonInstance.disconnect();
	}
	
	/**
	 * Retrieves current stock information.
	 * @param ticker A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
	 * @return ArrayList<Stock>
	 */
	public ArrayList<Stock> getStockInformation(String ticker) throws Exception {
		String response = jsonInstance.getStockInformation(ticker);
		JsonParser parser = new JsonParser();
		JsonArray allChildren = parser.parse(response).getAsJsonArray();
		ArrayList<Stock> result = new ArrayList<Stock>();
		for (int i = 0; i < allChildren.size()-1; i += 1) {
			result.add(new Stock(allChildren.get(i).getAsJsonObject(), gson));
		}
		return result;
	}
	
	/**
	 * Retrieves current stock information.
	 * @param ticker A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
	 * @param callback The listener that will receive the data (or error).
	 */
	public void getStockInformation(String ticker, final GetStockInformationListener callback) {
		
		jsonInstance.getStockInformation(ticker, new JsonGetStockInformationListener() {
		    @Override
		    public void getStockInformationFailed(Exception exception) {
		        callback.getStockInformationFailed(exception);
		    }
		    
		    @Override
		    public void getStockInformationCompleted(String response) {
		        JsonParser parser = new JsonParser();
		      JsonArray allChildren = parser.parse(response).getAsJsonArray();
		        ArrayList<Stock> result = new ArrayList<Stock>();
		        for (int i = 0; i < allChildren.size(); i += 1) {
		            result.add(new Stock(allChildren.get(i).getAsJsonObject(), gson));
		        }
		        callback.getStockInformationCompleted(result);
		    }
		});
		
	}
	
}
