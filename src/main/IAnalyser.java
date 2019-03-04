package main;

import java.io.IOException;

public interface IAnalyser {

	/**
	 * 
	 * Show gold and currency price in day specified by date
	 * 
	 * @param date
	 * 			date in format YYYY-MM-DD
	 * 
	 * @param currencyCode
	 * 			currency code
	 * 
	 * @throws IOException
	 * 			throws it when there is a problem with reading result
	 * 
	 */
	public void showGoldCurrencyPrice(String date, String currencyCode) throws IOException;
	
	/**
	 * 
	 * Show average gold price between two dates 
	 * 
	 * @param startDate
	 * 			startDate in format YYYY-MM-DD
	 * 
	 * @param endDate
	 * 			endDate in format YYYY-MM-DD
	 */
	public void showAverageGoldPrice(String startDate, String endDate);
	
	
	/**
	 * 
	 * Show currency with highest amplitude
	 * 
	 * @param startDate
	 * 			date in format YYYY-MM-DD
	 */
	public void findCurrencyWithHighestAmplitude(String startDate);
	
	/**
	 * 
	 * Find currency with lowest purchase price
	 * 
	 * @param date
	 * 			date in format YYYY-MM-DD
	 */
	public void findCurrencyWithLowestPurchasePrice(String date);
	
	/**
	 * 
	 * Write N currencies sorted by amplitude between bid and ask value
	 * 
	 * @param date
	 * 			date in format YYYY-MM-DD
	 * 
	 * @param currencies
	 * 			currencies separated by comma
	 */
	public void writeNSortedCurrencies(String date, String currencies);
	
	/**
	 * 
	 * Show highest and lowest currency value
	 * 
	 * 
	 * @param currencyCode
	 * 			code of currency ex: USD=$, GBP-Great Britain Pound
	 * 
	 */
	public void showCurrencyLowestHighestValue(String currencyCode);
	
	/**
	 * 
	 * Draw graph for specified currency between two dates
	 * 
	 * 
	 * @param currencyCode
	 * 			code of currency ex: USD=$, GBP-Great Britain Pound
	 * 
	 * @param startDate
	 * 			date in format YYYY-MM-WN
	 * 			where WN - week number! It is not a day
	 
	 * @param endDate
	 * 			date in format YYYY-MM-WN
	 * 			where WN - week number! It is not a day
	 * 
	 */
	public void drawGraph(String currencyCode, String startDate, String endDate);

	
	
}
