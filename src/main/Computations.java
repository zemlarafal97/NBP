package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Computations {

	
	
	/** Finds minimal value by tableName and field in result of queryUrl
	 * 
	 * @param tableName
	 * 			table name
	 * @param field
	 * 			field name
	 * 
	 * @param queryUrl
	 * 			query url
	 * 			
	 * @return currency code of currency with minimum value
	 */
	public static String findMinimum(String tableName, String field, String queryUrl) {
		String result="";	
		
		try {
			result = Network.makeQuery(queryUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		JsonParser parser = new JsonParser();
		
		JsonArray jsonArray = (JsonArray) parser.parse(result);
		
		
		JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
		
		jsonArray = jsonObject.get("rates").getAsJsonArray();
		
		HashMap<String, Double> map = new HashMap<>();
		
		for(int i=0; i<jsonArray.size(); i++) {
			jsonObject = jsonArray.get(i).getAsJsonObject();
			map.put(Formatter.quotationFormatter(jsonObject.get("code").toString()),Double.parseDouble(jsonObject.get("bid").toString()));
		}
		
		
		
		boolean first=true;
		double minValue=0;
		String minCurrencyCode="";
		for(Map.Entry<String,Double> entry : map.entrySet()) {
			Double val = entry.getValue();		
			String key = entry.getKey();
			if(first) {
				minValue=val;
				minCurrencyCode=key;
			} 
			if(minValue > val) {
				minValue=val;
				minCurrencyCode=key;
			}		
			first=false;
		//	System.out.println(key + " :: " + val);
		}
		
	//	System.out.println(minCurrencyCode + " : " + minValue);

		
		return minCurrencyCode;
		
	}
	
	
	
	
	
	
}
