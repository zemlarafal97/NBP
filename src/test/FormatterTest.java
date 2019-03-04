package test;


import org.junit.Assert;
import org.junit.Test;

import main.Formatter;

public class FormatterTest {

	@Test
	public void test(){
		String val1 = "1";
		String val2 = "0";
		String val3 = "5";
				
		Assert.assertEquals("01",Formatter.addZeroToBeginning(val1));
		Assert.assertEquals("00",Formatter.addZeroToBeginning(val2));
		Assert.assertEquals("05", Formatter.addZeroToBeginning(val3));
		
		String text = "\"Sowa\"";
		
		Assert.assertEquals("Sowa", Formatter.quotationFormatter(text));
		
		
		
	}
	
	
	
	
	
	
	
}
