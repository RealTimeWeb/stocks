package realtimeweb.stockservice.json;

import java.util.HashMap;

import realtimeweb.stockservice.main.AbstractStockService;
import realtimeweb.stockservice.util.Util;

/**
 * Used to get data as a raw string.
 */
public class JsonStockService implements AbstractStockService {
	private static JsonStockService instance;
	protected boolean local;
	private ClientStore clientStore;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  JsonStockService() {
		disconnect();
		this.clientStore = new ClientStore();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return JsonStockService
	 */
	public static JsonStockService getInstance() {
		if (instance == null) {
			synchronized (JsonStockService.class) {
				if (instance == null) {
					instance = new JsonStockService();
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
		this.local = false;
	}
	
	/**
	 * Establishes that Business Search data should be retrieved locally. This does not require an internet connection.<br><br>If data is being retrieved locally, you must be sure that your parameters match locally stored data. Otherwise, you will get nothing in return.
	
	 */
	@Override
	public void disconnect() {
		this.local = true;
	}
	
	/**
	 * **For internal use only!** The ClientStore is the internal cache where offline data is stored.
	
	 * @return ClientStore
	 */
	public ClientStore getClientStore() {
		return this.clientStore;
	}
	
	/**
	 * Retrieves current stock information.
	 * @param ticker A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
	 * @return String
	 */
	public String getStockInformation(String ticker) throws Exception {
		String url = String.format("http://www.google.com/finance/info", ticker);
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("q", String.valueOf(ticker));
		parameters.put("client", String.valueOf("iq"));
		if (this.local) {
			return clientStore.getData(Util.hashRequest(url, parameters));
		}
		String jsonResponse = "";
		try {
		    jsonResponse = Util.get(url, parameters);
		    if (jsonResponse.startsWith("<")) {
		        throw new Exception(jsonResponse);
		    }
		    return jsonResponse;
		} catch (Exception e) {
		    throw new Exception(e.toString());
		}
	}
	
	/**
	 * Retrieves current stock information.
	 * @param ticker A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
	 * @param callback The listener that will be given the data (or error).
	 */
	public void getStockInformation(final String ticker, final JsonGetStockInformationListener callback) {
		
		Thread thread = new Thread() {
		    @Override
		    public void run() {
		        try {
		            callback.getStockInformationCompleted(JsonStockService.getInstance().getStockInformation(ticker));
		        } catch (Exception e) {
		            callback.getStockInformationFailed(e);
		        }
		    }
		};
		thread.start();
		
	}
	
}
