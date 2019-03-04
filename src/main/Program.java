package main;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Program {

	public static void main(String[] args) {

		Analyser analyser = new Analyser();

		// analyser.drawGraph("CHF", "2015,08,3", "2017,12,2");

		Options options = new Options();

		options.addOption("c", true, "currencyCode");
		options.addOption("s", true, "startDate");
		options.addOption("e", true, "endDate");
		options.addOption("cur", true, "currencies");
		options.addOption("d", true, "date");
		options.addOption("h",false,"help");

		options.addOption("goldcurrency", false, "gold and currency price");
		options.addOption("goldaverage", false, "average gold price");
		options.addOption("currencyhighestamp", false, "currency highest amplitude");
		options.addOption("minpurchaserate", false, "minimal purchase rate");
		options.addOption("nsortedcurrencies", false, "N sorted currencies");
		options.addOption("currencyminmax", false, "currency min max date");
		options.addOption("drawgraph", false, "drawgraph");

		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("goldcurrency")) {
				if (cmd.hasOption("c") && cmd.hasOption("d")) {
					String date = cmd.getOptionValue("d");
					String currencyCode = cmd.getOptionValue("c");

					analyser.showGoldCurrencyPrice(date, currencyCode);
				} else {
					System.out.println("Missing arguments");
				}
			} else if (cmd.hasOption("goldaverage")) {
				if (cmd.hasOption("s") && cmd.hasOption("e")) {
					String startDate = cmd.getOptionValue("s");
					String endDate = cmd.getOptionValue("e");
					analyser.showAverageGoldPrice(startDate, endDate);
				} else {
					System.out.println("Missing arguments");
				}
			} else if (cmd.hasOption("currencyhighestamp")) {
				if (cmd.hasOption("d")) {
					String date = cmd.getOptionValue("d");
					analyser.findCurrencyWithHighestAmplitude(date);
				} else {
					System.out.println("Missing arguments");
				}
			} else if (cmd.hasOption("minpurchaserate")) {
				if (cmd.hasOption("d")) {
					String date = cmd.getOptionValue("d");
					analyser.findCurrencyWithLowestPurchasePrice(date);
				} else {
					System.out.println("Missing arguments");
				}
			} else if (cmd.hasOption("nsortedcurrencies")) {
				if (cmd.hasOption("d") && cmd.hasOption("cur")) {
					String date = cmd.getOptionValue("d");
					String currencies = cmd.getOptionValue("cur");
					analyser.writeNSortedCurrencies(date, currencies);
				} else {
					System.out.println("Missing arguments");
				}
			} else if (cmd.hasOption("currencyminmax")) {
				if (cmd.hasOption("c")) {
					String currencyCode = cmd.getOptionValue("c");
					analyser.showCurrencyLowestHighestValue(currencyCode);
				} else {
					System.out.println("Missing arguments");
				}
			} else if (cmd.hasOption("drawgraph")) {
				if (cmd.hasOption("s") && cmd.hasOption("e") && cmd.hasOption("c")) {
					String startDate = cmd.getOptionValue("s");
					String endDate = cmd.getOptionValue("e");
					String currencyCode = cmd.getOptionValue("c");
					analyser.drawGraph(currencyCode, startDate, endDate);
				} else {
					System.out.println("Missing arguments");
				}

			} else{
				System.out.println("HELP for NBP Application:");
				System.out.println("Options:");
				System.out.println("-goldcurrency       shows currency and gold price");
				System.out.println("ex: -goldcurrency -d 2017-07-10 -c USD");
				System.out.println("-goldaverage        shows average gold price between start and end dates");
				System.out.println("ex: -goldaverage -s 2014-10-10 -e 2016-07-09");
				System.out.println("-currencyhighestamp shows currency highest amplitude in certain day");
				System.out.println("ex: -currencyhighestamp -d 2017-10-10");
				System.out.println("-minpurchaserate    shows currency with minpurchaserate in specified day");
				System.out.println("ex: -minpurchaserate -d 2016-06-07");
				System.out.println(
						"-nsortedcurrencies  shows N sorted currencies sorted increasingly towards difference between sell and purchase price ");
				System.out.println("ex: -nsortedcurrencies -d 2016-07-10 -cur USD,EUR,CHF,GBP");
				System.out.println(
						"-currencyminmax     shows for specified currency information when was cheapest/most expensive");
				System.out.println("ex: -currencyminmax -c USD");
				System.out.println("-drawgraph          draws graph for specified currency ");
				System.out.println("ex: -drawgraph -s 2017,01,1 -e 2017,10,2 -c EUR");
				System.out.println("           WHERE 1 and 2 are weeks not days");

				System.out.println("Additional information:");
				System.out.println("-s     startDate");
				System.out.println("-e     endDate");
				System.out.println("-c     currencyCode");
				System.out.println("-cur   currencies");
				System.out.println("-d     date");

			}

		} catch (ParseException e) {
			System.out.println("B³êdnie wpisane dane!");
		}

	}

}
