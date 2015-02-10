package org.mot.common.tools;

import java.util.LinkedHashMap;

public class PriceTracker {
	
	
	private LinkedHashMap<String, Double> tracker = new LinkedHashMap<String, Double>();
	private static PriceTracker instance; 
	
	private PriceTracker() {

	}
	
	public static PriceTracker getInstance() {
		if (instance == null) {
			instance = new PriceTracker();
		} 
		return instance;
	}
	
	
	/**
	 * This method is used to track the price changes of a symbol. 
	 * @param symbol - symbol to track. Make sure to include ASK/BID into the string (AAPL-BID or AAPL-ASK)
	 * @param value - Double value (price)
	 */
	public void updatePrice(String symbol, Double value) {
		tracker.put(symbol, value);
	}
	
	
	/**
	 * Use this method to get the price for a particular symbol
	 * @param symbol - symbol to look for
	 * @return - double value containing the last price
	 */
	public Double getPrice(String symbol) {
		return tracker.get(symbol);
	}
	
	/**
	 * Use this method to get the price for a particular symbol, if null - use provided default value.
	 * 
	 * @param symbol - symbol to look for
	 * @param defaultValue - defaultValue to be used, if tracker returns null
	 * @return - double value containing the last price
	 */
	public Double getPriceOrDefault(String symbol, Double defaultValue) {
		
		Double ret = tracker.get(symbol);
		if (ret == null) {
			ret = defaultValue;
		}
		return ret;
	}

}
