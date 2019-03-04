package main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Analyser implements IAnalyser {

	private final String GOLD_PRICE_URL = "http://api.nbp.pl/api/cenyzlota";
	private final String CURRENCY_PRICE_URL = "http://api.nbp.pl/api/exchangerates/rates/a";
	private final String CURRENCY_PRICES_URL = "http://api.nbp.pl/api/exchangerates/tables/c";
	private final String CURRENCY_PRICES_TABLE_A_URL = "https://api.nbp.pl/api/exchangerates/tables/a";
	private final String JSON_FORMAT = "?format=json";

	@Override
	public void showGoldCurrencyPrice(String date, String currencyCode) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(GOLD_PRICE_URL);
		stringBuilder.append("/");
		stringBuilder.append(date);
		stringBuilder.append("/");
		stringBuilder.append(JSON_FORMAT);
		String goldUrl = stringBuilder.toString();

		stringBuilder.setLength(0);

		stringBuilder.append(CURRENCY_PRICE_URL);
		stringBuilder.append("/");
		stringBuilder.append(currencyCode);
		stringBuilder.append("/");
		stringBuilder.append(date);
		stringBuilder.append(JSON_FORMAT);
		String currencyUrl = stringBuilder.toString();

		JsonParser parser = new JsonParser();
		JsonElement element = null;
		JsonArray jsonArray;
		JsonObject jsonObject;
		double goldPrice;
		double currencyPrice;

		try {
			element = parser.parse(Network.makeQuery(goldUrl));
		} catch (JsonSyntaxException e) {
			System.out.println("Problem!");
			System.out.println("Change date or currency and try again");
		} catch (IOException e) {
			System.out.println("Problem!");
			System.out.println("Change date or currency and try again");
		}
		jsonArray = element.getAsJsonArray();
		jsonObject = (JsonObject) jsonArray.get(0);
		goldPrice = jsonObject.get("cena").getAsDouble();

		try {
			element = parser.parse(Network.makeQuery(currencyUrl));
		} catch (JsonSyntaxException e) {
			System.out.println("Problem!");
			System.out.println("Change date or currency and try again");
		} catch (IOException e) {
			System.out.println("Problem!");
			System.out.println("Change date or currency and try again");
		}
		jsonObject = element.getAsJsonObject();
		jsonArray = jsonObject.get("rates").getAsJsonArray();
		jsonObject = (JsonObject) jsonArray.get(0);
		currencyPrice = jsonObject.get("mid").getAsDouble();

		System.out.println("-----------------------");
		System.out.println("DATA: " + date);
		System.out.println("Cena z³ota: " + goldPrice + "z³");
		System.out.println("Cena " + currencyCode + ": " + currencyPrice + "z³");
		System.out.println("-----------------------");

	}

	@Override
	public void showAverageGoldPrice(String startDate, String endDate) {

		String[] sTmp = startDate.split("-");
		String[] eTmp = endDate.split("-");
		int sYear = Integer.parseInt(sTmp[0]);
		int eYear = Integer.parseInt(eTmp[0]);

		int quantity = 0;
		double total = 0;

		boolean firstLoop = true;

		while (sYear <= eYear) {
			String queryUrl = "";

			if (firstLoop) {
				StringBuilder sb = new StringBuilder();
				sb.append(GOLD_PRICE_URL);
				sb.append("/");
				sb.append(startDate);
				sb.append("/");

				if (sYear == eYear) {
					sb.append(endDate);
				} else {
					sb.append(sYear);
					sb.append("-12-31");
				}
				sb.append("/");
				sb.append(JSON_FORMAT);

				queryUrl = sb.toString();

				firstLoop = false;
			} else {
				String sTmpDate = sYear + "-01-01";
				String eTmpDate = "";

				if (sYear == eYear) {
					eTmpDate = endDate;
				} else {
					eTmpDate = sYear + "-12-31";
				}

				StringBuilder sb = new StringBuilder();
				sb.append(GOLD_PRICE_URL);
				sb.append("/");
				sb.append(sTmpDate);
				sb.append("/");
				sb.append(eTmpDate);
				sb.append("/");
				sb.append(JSON_FORMAT);

				queryUrl = sb.toString();
			}

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();

			String result = null;
			try {
				result = Network.makeQuery(queryUrl);
				jsonArray = jsonParser.parse(result).getAsJsonArray();
				quantity += jsonArray.size();
			} catch (IOException e) {
				System.out.println("Problem!");
				System.out.println("Dates range for gold is");
				System.out.println("2013-01-02 to ...");
			}

			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.get(i).getAsJsonObject();
				total += jsonObject.get("cena").getAsDouble();

			}
			sYear++;
		}

		double average = total / quantity;

		System.out.println("---------------------------------------------------------");
		System.out.println("Œrednia cena z³ota za okres (" + startDate + "  " + endDate + ")");
		System.out.println("Wartoœæ: " + Math.round(average * 100.0) / 100.0 + "zl");
		System.out.println("---------------------------------------------------------");

	}

	@Override
	public void findCurrencyWithHighestAmplitude(String startDate) {

		HashMap<String, Double> mapMin = new HashMap<>();
		HashMap<String, Double> mapMax = new HashMap<>();

		Calendar calendar = Calendar.getInstance();
		boolean firstLoop = true;
		String endDate = String.valueOf(calendar.get(Calendar.YEAR)) + "-"
				+ Formatter.addZeroToBeginning(String.valueOf(calendar.get(Calendar.MONTH) + 1)) + "-"
				+ Formatter.addZeroToBeginning(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;

		try {
			start = formatter.parse(startDate);
			end = formatter.parse(endDate);
		} catch (ParseException e) {
			System.out.println("Problem!");
			System.out.println("Formatting problem");
		}

		Calendar s = Calendar.getInstance();
		Calendar e = Calendar.getInstance();
		s.setTime(start);
		e.setTime(end);

		int days = 0;

		while (s.before(e)) {
			Calendar begin = Calendar.getInstance();
			begin.setTime(s.getTime());

			while (days < 90 && s.before(e)) {
				s.add(Calendar.DATE, 1);
				days++;
			}

			String tmpDate = begin.get(Calendar.YEAR) + "-"
					+ Formatter.addZeroToBeginning(String.valueOf(begin.get(Calendar.MONTH) + 1)) + "-"
					+ Formatter.addZeroToBeginning(String.valueOf(begin.get(Calendar.DAY_OF_MONTH)));

			String queryUrl = "";
			StringBuilder sb = new StringBuilder();
			sb.append(CURRENCY_PRICES_TABLE_A_URL);
			sb.append("/");
			sb.append(tmpDate);
			tmpDate = s.get(Calendar.YEAR) + "-"
					+ Formatter.addZeroToBeginning(String.valueOf(s.get(Calendar.MONTH) + 1)) + "-"
					+ Formatter.addZeroToBeginning(String.valueOf(s.get(Calendar.DAY_OF_MONTH)));
			sb.append("/");
			sb.append(tmpDate);
			sb.append("/");
			sb.append(JSON_FORMAT);

			queryUrl = sb.toString();

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = new JsonObject();

			String result = null;

			try {
				result = Network.makeQuery(queryUrl);
				// System.out.println(result);
			} catch (IOException e1) {
				System.out.println("Poblem!");
				System.out.println("Change date and try again!");
			}

			JsonArray outerJsonArray = jsonParser.parse(result).getAsJsonArray();
			JsonArray innerJsonArray;

			for (int i = 0; i < outerJsonArray.size(); i++) {
				jsonObject = outerJsonArray.get(i).getAsJsonObject();
				innerJsonArray = jsonObject.get("rates").getAsJsonArray();
				for (int j = 0; j < innerJsonArray.size(); j++) {
					JsonObject iJsonObiect = new JsonObject();
					iJsonObiect = innerJsonArray.get(j).getAsJsonObject();
					String currCode = iJsonObiect.get("code").getAsString();
					double value = iJsonObiect.get("mid").getAsDouble();

					if (firstLoop) {
						mapMin.put(currCode, value);
						mapMax.put(currCode, value);

					} else {
						try {
							if (mapMin.get(currCode) > value) {
								mapMin.put(currCode, value);
							}
							if (mapMax.get(currCode) < value) {
								mapMax.put(currCode, value);
							}
						} catch (NullPointerException e1) {

						}
					}

				}
				firstLoop = false;
			}

			// jsonArray = jsonParser.parse(result).getAsJsonArray();
			// quantity += jsonArray.size();

			s.add(Calendar.DATE, 1);
			days = 0;
		}

		double diff = 0;
		String currName = "";
		boolean firstLoop2 = true;

		for (Map.Entry<String, Double> entry : mapMax.entrySet()) {

			if (firstLoop2) {
				currName = entry.getKey();
				diff = entry.getValue() - mapMin.get(entry.getKey());
				firstLoop2 = false;
			} else {
				if (entry.getValue() - mapMin.get(entry.getKey()) > diff) {
					currName = entry.getKey();
					diff = entry.getValue() - mapMin.get(entry.getKey());
				}

			}

		}

		System.out.println("----------------------------------");
		System.out.println("MAX aplituda: " + diff);
		System.out.println("DLA: " + currName);
		System.out.println("----------------------------------");

	}

	@Override
	public void findCurrencyWithLowestPurchasePrice(String date) {

		String currency = Computations.findMinimum("rates", "bid", CURRENCY_PRICES_URL + "/" + date + "/?format=json");

		String text = "Waluta z najmniejsz¹ cen¹ zakupu: ";
		
		System.out.println(text + currency);
		
	}

	@Override
	public void writeNSortedCurrencies(String date, String currencies) {
		String[] currTab = currencies.split(",");

		String queryUrl = "";
		StringBuilder sb = new StringBuilder();
		sb.append(CURRENCY_PRICES_URL);
		sb.append("/");
		sb.append(date);
		sb.append("/");
		sb.append(JSON_FORMAT);
		queryUrl = sb.toString();

		System.out.println(queryUrl);

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();

		HashMap<String, Double> bidMap = new HashMap<>();
		HashMap<String, Double> askMap = new HashMap<>();
		HashMap<String, Double> diffMap = new HashMap<>();

		String result = null;
		try {
			result = Network.makeQuery(queryUrl);
			jsonArray = jsonParser.parse(result).getAsJsonArray();
			jsonObject = jsonArray.get(0).getAsJsonObject();
			jsonArray = jsonObject.get("rates").getAsJsonArray();

		} catch (IOException e) {
			System.out.println("Problem!");
			System.out.println("Change date or currency and try again");
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.get(i).getAsJsonObject();

			for (String el : currTab) {
				if (jsonObject.get("code").getAsString().equals(el)) {
					bidMap.put(el, jsonObject.get("bid").getAsDouble());
					askMap.put(el, jsonObject.get("ask").getAsDouble());
				}
			}

		}

		double[] sortedTab = new double[bidMap.size()];

		int i = 0;
		for (Map.Entry<String, Double> entry : bidMap.entrySet()) {
			diffMap.put(entry.getKey(), askMap.get(entry.getKey()) - bidMap.get(entry.getKey()));
			sortedTab[i] = diffMap.get(entry.getKey());
			i++;
		}

		for (int j = i - 2; j >= 0; j--) {
			double x = sortedTab[j];
			int k = j + 1;
			while ((k < i) && (x > sortedTab[k])) {
				sortedTab[k - 1] = sortedTab[k];
				k++;
			}
			sortedTab[k - 1] = x;
		}

		for (int k = 0; k < i; k++) {
			for (Map.Entry<String, Double> entry : diffMap.entrySet()) {
				if (sortedTab[k] == entry.getValue()) {
					System.out.println(entry.getKey() + ": (" + entry.getValue() + ")");
				}
			}
		}

	}

	@Override
	public void showCurrencyLowestHighestValue(String currencyCode) {

		Calendar calendar = Calendar.getInstance();

		// System.out.println(calendar.getTime());

		String startDate = "2002-01-02";
		String endDate = String.valueOf(calendar.get(Calendar.YEAR)) + "-"
				+ Formatter.addZeroToBeginning(String.valueOf(calendar.get(Calendar.MONTH) + 1)) + "-"
				+ Formatter.addZeroToBeginning(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
		// System.out.println(endDate);

		int sYear = 2002;
		int eYear = calendar.get(Calendar.YEAR);

		String dateLowestPrice = "";
		String dateHighestPrice = "";

		double min = 0;
		double max = 0;

		boolean minMaxFirstIteration = true;
		boolean firstLoop = true;

		while (sYear <= eYear) {
			String queryUrl = "";

			if (firstLoop) {
				StringBuilder sb = new StringBuilder();
				sb.append(CURRENCY_PRICE_URL);
				sb.append("/");
				sb.append(currencyCode);
				sb.append("/");
				sb.append(startDate);
				sb.append("/");

				if (sYear == eYear) {
					sb.append(endDate);
				} else {
					sb.append(sYear);
					sb.append("-12-31");
				}
				sb.append("/");
				sb.append(JSON_FORMAT);

				queryUrl = sb.toString();

				firstLoop = false;
			} else {
				String sTmpDate = sYear + "-01-01";
				String eTmpDate = "";

				if (sYear == eYear) {
					eTmpDate = endDate;
				} else {
					eTmpDate = sYear + "-12-31";
				}

				StringBuilder sb = new StringBuilder();
				sb.append(CURRENCY_PRICE_URL);
				sb.append("/");
				sb.append(currencyCode);
				sb.append("/");
				sb.append(sTmpDate);
				sb.append("/");
				sb.append(eTmpDate);
				sb.append("/");
				sb.append(JSON_FORMAT);

				queryUrl = sb.toString();
			}

			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArray = new JsonArray();
			JsonObject jsonObject = new JsonObject();

			String result = null;
			try {
				result = Network.makeQuery(queryUrl);

				jsonObject = jsonParser.parse(result).getAsJsonObject();
				jsonArray = jsonObject.get("rates").getAsJsonArray();

				// System.out.println(jsonArray);
			} catch (IOException e) {
				System.out.println("Problem!");
				System.out.println("Change currency and try again!");
			}

			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.get(i).getAsJsonObject();

				String date = jsonObject.get("effectiveDate").getAsString();

				if (minMaxFirstIteration) {
					min = jsonObject.get("mid").getAsDouble();
					max = jsonObject.get("mid").getAsDouble();
					dateLowestPrice = date;
					dateHighestPrice = date;
					minMaxFirstIteration = false;
				} else {
					if (min > jsonObject.get("mid").getAsDouble()) {
						min = jsonObject.get("mid").getAsDouble();
						dateLowestPrice = date;
					}
					if (max < jsonObject.get("mid").getAsDouble()) {
						max = jsonObject.get("mid").getAsDouble();
						dateHighestPrice = date;
					}

				}

			}

			sYear++;
		}

		System.out.println("---------------------------------------------------");
		System.out.println("Kod waluty = " + currencyCode);
		System.out.println("MAX kurs: " + max + " [ " + dateHighestPrice + " ]");
		System.out.println("MIN kurs: " + min + " [ " + dateLowestPrice + " ] ");
		System.out.println("---------------------------------------------------");

	}

	@Override
	public void drawGraph(String currencyCode, String startDate, String endDate) {

		HashMap<String, Double> map = new HashMap<>();

		String[] tmp = startDate.split(",");
		int y = Integer.parseInt(tmp[0]);
		int m = Integer.parseInt(tmp[1]);
		int wn = Integer.parseInt(tmp[2]);

		int sYear = y;

		// System.out.println("WN: " + wn);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = String.valueOf(y) + "-" + String.valueOf(Formatter.addZeroToBeginning(String.valueOf(m))) + "-"
				+ "01";
		// System.out.println(date);

		Date data = null;

		try {
			data = formatter.parse(date);
		} catch (ParseException e) {
			System.out.println("Problem!");
			System.out.println("Formatting problem");
		}

		Calendar start = Calendar.getInstance();
		start.setTime(data);
		// System.out.println(start.getTime());

		while (wn > 0) {
			start.add(Calendar.DATE, 1);
			if (start.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				wn--;
			}
		}

		// System.out.println(start.getTime());
		// start - ustawiony

		tmp = endDate.split(",");
		y = Integer.parseInt(tmp[0]);
		m = Integer.parseInt(tmp[1]);
		wn = Integer.parseInt(tmp[2]);

		int eYear = y;

		date = String.valueOf(y) + "-" + String.valueOf(Formatter.addZeroToBeginning(String.valueOf(m))) + "-" + "01";
		Calendar end = Calendar.getInstance();
		try {
			data = formatter.parse(date);
		} catch (ParseException e) {
			System.out.println("Problem!");
			System.out.println("Change currencies,startdate or enddate and try again!");
		}
		end.setTime(data);

		// System.out.println(end.getTime());

		while (wn > 0) {
			end.add(Calendar.DATE, 1);
			if (end.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				wn--;
			}
		}
		end.add(Calendar.DATE, 4);
		// System.out.println(end.getTime());

		boolean firstLoop = true;
		String sDate = String.valueOf(start.get(Calendar.YEAR)) + "-"
				+ Formatter.addZeroToBeginning(String.valueOf(start.get(Calendar.MONTH) + 1)) + "-"
				+ Formatter.addZeroToBeginning(String.valueOf(start.get(Calendar.DAY_OF_MONTH)));
		String eDate = String.valueOf(end.get(Calendar.YEAR)) + "-"
				+ Formatter.addZeroToBeginning(String.valueOf(end.get(Calendar.MONTH) + 1)) + "-"
				+ Formatter.addZeroToBeginning(String.valueOf(end.get(Calendar.DAY_OF_MONTH)));
		while (sYear <= eYear) {
			String queryUrl = "";

			if (firstLoop) {

				StringBuilder sb = new StringBuilder();
				sb.append(CURRENCY_PRICE_URL);
				sb.append("/");
				sb.append(currencyCode);
				sb.append("/");
				sb.append(sDate);
				sb.append("/");

				if (sYear == eYear) {
					sb.append(eDate);
				} else {
					sb.append(sYear);
					sb.append("-12-31");
				}
				sb.append("/");
				sb.append(JSON_FORMAT);

				queryUrl = sb.toString();

				firstLoop = false;
			} else {
				String sTmpDate = sYear + "-01-01";
				String eTmpDate = "";

				if (sYear == eYear) {
					eTmpDate = eDate;
				} else {
					eTmpDate = sYear + "-12-31";
				}

				StringBuilder sb = new StringBuilder();
				sb.append(CURRENCY_PRICE_URL);
				sb.append("/");
				sb.append(currencyCode);
				sb.append("/");
				sb.append(sTmpDate);
				sb.append("/");
				sb.append(eTmpDate);
				sb.append("/");
				sb.append(JSON_FORMAT);

				queryUrl = sb.toString();
			}

			// System.out.println(queryUrl);

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();

			String result = null;

			try {
				result = Network.makeQuery(queryUrl);
				jsonObject = jsonParser.parse(result).getAsJsonObject();
				jsonArray = jsonObject.get("rates").getAsJsonArray();
				// System.out.println(result);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.get(i).getAsJsonObject();
				map.put(jsonObject.get("effectiveDate").getAsString(), jsonObject.get("mid").getAsDouble());
			}

			sYear++;
		}

		try {
			data = formatter.parse(sDate);
			start.setTime(data);
			data = formatter.parse(eDate);
			end.setTime(data);
		} catch (ParseException e) {
			System.out.println("Problem!");
			System.out.println("Problem with parsing.");
		}

		// System.out.println(start.getTime());
		// System.out.println(end.getTime());

		end.add(Calendar.DATE, 1);

		int index = 1;

		boolean firstTimeLoop = true;
		double min = 0;
		double max = 0;

		for (Map.Entry<String, Double> entry : map.entrySet()) {
			// System.out.println(entry.getKey() + ": " + entry.getValue());
			if (firstTimeLoop) {
				min = entry.getValue();
				max = entry.getValue();

				firstTimeLoop = false;
			} else {
				if (min > entry.getValue())
					min = entry.getValue();
				if (max < entry.getValue())
					max = entry.getValue();

			}
		}

		// System.out.println("MAX: " + max);
		// System.out.println("MIN: " + min);

		double diff = max - min;
		double jmp = diff / 20.0;

		HashMap<String, String> resultMap = new HashMap<>();

		while (start.before(end)) {

			String dayName = "";

			String dateText = "";
			StringBuilder sb = new StringBuilder();
			sb.append(start.get(Calendar.YEAR));
			sb.append("-");
			sb.append(Formatter.addZeroToBeginning(String.valueOf((start.get(Calendar.MONTH) + 1))));
			sb.append("-");
			sb.append(Formatter.addZeroToBeginning(String.valueOf((start.get(Calendar.DAY_OF_MONTH)))));
			dateText = sb.toString();

			switch (start.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				dayName = "Pon";
				break;
			case Calendar.TUESDAY:
				dayName = "Wto";
				break;
			case Calendar.WEDNESDAY:
				dayName = "Sro";
				break;
			case Calendar.THURSDAY:
				dayName = "Czw";
				break;
			case Calendar.FRIDAY:
				dayName = "Pia";
				break;
			case Calendar.SUNDAY:
				index++;
				break;
			}
			dayName += index;

			if (start.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| start.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

			} else {
				String concater = "";
				try {

					double val = map.get(dateText);
					int i = 0;
					double temp = min;

					while (temp <= val) {
						temp += jmp;
						i++;
					}
					concater = dayName;
					while (concater.length() < 7) {
						concater += " ";
					}

					for (int j = 0; j < i; j++) {
						concater += "=";
					}
					concater += "  " + val + "z³";
					resultMap.put(dayName, concater);
					// System.out.println(concater);
					// System.out.println(val);

				} catch (NullPointerException e) {
					concater = dayName + "  --- brak ---";
					while (concater.length() < 7) {
						concater += " ";
					}
					resultMap.put(dayName, concater);
				}

			}

			start.add(Calendar.DATE, 1);
		}

		for (int i = 0; i < 5; i++) {
			int cnt = 1;
			String begin = "";
			switch (i) {
			case 0:
				begin = "Pon";
				break;
			case 1:
				begin = "Wto";
				break;
			case 2:
				begin = "Sro";
				break;
			case 3:
				begin = "Czw";
				break;
			case 4:
				begin = "Pia";
				break;
			}

			while (cnt <= index) {
				System.out.println(resultMap.get(begin + cnt));

				cnt++;
			}
		}

	}

}
