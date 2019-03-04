package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Network {

	
	/**
	 * 
	 * 
	 * 
	 * @param queryUrl
	 * 			It is a query url 
	 * @return result of querying
	 * 
	 * @throws IOException 
	 * 			throws it when there is a problem with reading result
	 */

	public static String makeQuery(String queryUrl) throws IOException {
		String result = "";

		URL url = new URL(queryUrl);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			result += inputLine;
		}

		return result;
	}

}
