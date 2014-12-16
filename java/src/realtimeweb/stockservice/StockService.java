package realtimeweb.stockservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import realtimeweb.stickyweb.EditableCache;
import realtimeweb.stickyweb.StickyWeb;
import realtimeweb.stickyweb.StickyWebRequest;
import realtimeweb.stickyweb.converters.JsonConverter;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidPostArguments;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidQueryString;
import realtimeweb.stickyweb.exceptions.StickyWebJsonResponseParseException;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;
import realtimeweb.stockservice.domain.Stock;

/**
 * Get the latest information about stocks.
 */
public class StockService {
    private StickyWeb connection;
	private boolean online;
    
    public static void main(String[] args) {
        StockService stockService = new StockService();
        
        // The following pre-generated code demonstrates how you can
		// use StickyWeb's EditableCache to create data files.
		try {
            // First, you create a new EditableCache, possibly passing in an FileInputStream to an existing cache
			EditableCache recording = new EditableCache();
            // You can add a Request object directly to the cache.
			// recording.addData(stockService.getstockinfoRequest(...));
            // Then you can save the expanded cache, possibly over the original
			recording.saveToStream(new FileOutputStream("cache.json"));
		} catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given FileStream was not able to be found.");
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("The given FileStream could not be parsed; possibly the structure is incorrect.");
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be loaded.");
		} catch (FileNotFoundException e) {
			System.err.println("The given cache.json file was not found, or could not be opened.");
		}
        // ** End of how to use the EditableCache
    }
	
    /**
     * Create a new, online connection to the service
     */
	public StockService() {
        this.online = true;
		try {
			this.connection = new StickyWeb(null);
		} catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given datastream could not be loaded.");
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("The given datastream could not be parsed");
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be loaded");
		}
	}
	
    /**
     * Create a new, offline connection to the service.
     * @param cache The filename of the cache to be used.
     */
	public StockService(String cache) {
        // TODO: You might consider putting the cache directly into the jar file,
        // and not even exposing filenames!
        try {
            this.online = false;
            this.connection = new StickyWeb(new FileInputStream(cache));
        } catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given data source could not be found.");
            System.exit(1);
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("Could not read the data source. Perhaps its format is incorrect?");
            System.exit(1);
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be read.");
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println("The given cache file could not be found. Make sure it is in the right folder.");
			System.exit(1);
		}
	}
    
    
    /**
     * Retrieves current stock information.
     *
     * This version of the function meant for instructors to capture a
     * StickyWebRequest object which can be put into an EditableCache and then
     * stored to a "cache.json" file.
     * 
     * @param ticker A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
     * @return a StickyWebRequest
     */
    public StickyWebRequest getStockInfoRequest(String ticker) {
        try {
            /*
            * Perform any user parameter validation here. E.g.,
            * if the first argument can't be zero, or they give an empty string.
            */
            
            // Build up query string
            final String url = String.format("http://www.google.com/finance/info");
            
            // Build up the query arguments that will be sent to the server
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("q", String.valueOf(ticker));
            
            // Build up the list of actual arguments that should be used to
            // create the local cache hash key
            ArrayList<String> indexList = new ArrayList<String>();
            indexList.add("q");
            
            
            // Build and return the connection object.
            return connection.get(url, parameters)
                            .setOnline(online)
                            .setIndexes(indexList);
        
        } catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("Could not find the data source.");
		}
        return null;
    }
    
    /**
     * Retrieves current stock information.
    
     * @param ticker A comma separated list of ticker symbols (e.g. "AAPL, MSFT, CSCO").
     * @return a Stock
     */
	public Stock getStockInfo(String ticker) {
        
        // Might need to consume first two characters, which appear to be double slashes
        try {
			StickyWebRequest request =  getStockInfoRequest(ticker);
			//clean the data
			String result = request.execute().asText();
			String clean1 = result.substring(4, result.length());	//remove double slashes
			String clean2 = clean1.substring(1,clean1.length()-2);	//get rid of square bracket and get the object
			String clean3 = clean2.trim();	//clean any empty spaces
			Map<String, Object> map_result = JsonConverter.convertToMap(clean3);
			return new Stock(map_result);
			
		} catch (StickyWebNotInCacheException e) {
			System.err.println("There is no query in the cache for the given inputs. Perhaps something was mispelled?");
		} catch (StickyWebInternetException e) {
			System.err.println("Could not connect to the web service. It might be your internet connection, or a problem with the web service.");
		} catch (StickyWebInvalidQueryString e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
		} catch (StickyWebInvalidPostArguments e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
        
        } catch (StickyWebJsonResponseParseException e) {
            System.err.println("The response from the server couldn't be understood.");
        
		}
		return null;
	}
	

    
}