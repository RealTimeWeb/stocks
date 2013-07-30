package realtimeweb.stockservice.structured;

import java.util.ArrayList;

import realtimeweb.stockservice.json.JsonGetStockInformationListener;
import realtimeweb.stockservice.json.JsonStockService;
import realtimeweb.stockservice.main.AbstractStockService;

import com.google.gson.Gson;

/**
 * Used to get data as built-in Java objects (HashMap, ArrayList, etc.).
 */
public class StructuredStockService implements AbstractStockService {
	private static StructuredStockService instance;
	private JsonStockService jsonInstance;
	private Gson gson;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  StructuredStockService() {
		this.jsonInstance = JsonStockService.getInstance();
		this.gson = new Gson();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return StructuredStockService
	 */
	public static StructuredStockService getInstance() {
		if (instance == null) {
			synchronized (StructuredStockService.class) {
				if (instance == null) {
					instance = new StructuredStockService();
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
	 * @return HashMap<String, Object>
	 */
	public ArrayList getStockInformation(String ticker) throws Exception {
		return gson.fromJson(jsonInstance.getStockInformation(ticker).substring(3), ArrayList.class);
	}
	
	/**
	 * Retrieves current stock information.
	 * @param ticker A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
	 * @param callback The listener that will be given the data (or error)
	 */
	public void getStockInformation(String ticker, final StructuredGetStockInformationListener callback) {
		
		jsonInstance.getStockInformation(ticker, new JsonGetStockInformationListener() {
		    @Override
		    public void getStockInformationFailed(Exception exception) {
		        callback.getStockInformationFailed(exception);
		    }
		    
		    @Override
		    public void getStockInformationCompleted(String data) {
		        callback.getStockInformationCompleted(gson.fromJson(data.substring(3), ArrayList.class));
		    }
		});
		
	}
	
}
