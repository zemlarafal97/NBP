package test;


import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import main.Network;

public class NetworkTest {

	
	@Test
	public void test(){
		
		String result="{\"table\":\"A\",\"currency\":\"euro\",\"code\":\"EUR\",\"rates\":[{\"no\":\"160/A/NBP/2017\",\"effectiveDate\":\"2017-08-21\",\"mid\":4.2747}]}";
		String url="http://api.nbp.pl/api/exchangerates/rates/a/EUR/2017-08-21/?format=json";
		
		String result2="";
		try {
			result2 = Network.makeQuery(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals("NetworkTest1", result, result2);
		
		
		
	}
	
	
}
