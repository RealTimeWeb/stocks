package realtimeweb.stockservice.structured;

import java.util.ArrayList;
import java.util.HashMap;
import realtimeweb.stockservice.domain.Stock;
/**
 * A listener for the getStockInformation method. On success, passes the data into the getStockInformationCompleted method. On failure, passes the exception to the getStockInformationFailed method.
 */
public interface StructuredGetStockInformationListener {
	/**
	 * 
	 * @param data The method that should be overridden to handle the data if the method was successful.
	 */
	public abstract void getStockInformationCompleted(ArrayList<Object> data);
	/**
	 * 
	 * @param error The method that should be overridden to handle an exception that occurred while getting the SearchResponse.
	 */
	public abstract void getStockInformationFailed(Exception error);
}
