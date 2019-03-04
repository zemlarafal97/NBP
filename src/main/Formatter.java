package main;

public class Formatter {

	
	
	
	/**
	 * 
	 * @param text 
	 * 			this text will be formated
	 * 
	 * @return formatted text
	 */
	public static String quotationFormatter(String text) {

		text = text.trim();

		if (text.startsWith("\"") && text.endsWith("\"")) {
			text = text.substring(1, text.length() - 1);
		}

		return text;
	}

	/** Add zero to beginning of string which integer value is between 0..9
	 * 
	 * @param text
	 * 			input value
	 * 
	 * @return formatted text
	 */
	
	
	public static String addZeroToBeginning(String text) {

		if (Integer.valueOf(text) < 10) {
			text = "0" + text;
		}

		return text;

	}
}
