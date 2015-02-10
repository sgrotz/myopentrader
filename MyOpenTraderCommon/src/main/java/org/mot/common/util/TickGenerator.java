package org.mot.common.util;

import javax.jms.MessageProducer;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.mot.common.conversion.TickConverter;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.objects.Tick;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TickGenerator {
	
	CalculatorFactory cf = new CalculatorFactory();
	TickPriceDAO tpd = new TickPriceDAO();
	DateBuilder dbi = new DateBuilder();
	TickConverter tc = new TickConverter();
	ActiveMQFactory amq = ActiveMQFactory.getInstance();
	WatchListDAO wld = new WatchListDAO();
	Logger logger = LoggerFactory.getLogger(getClass());
	static long pause = 500;
	static int maxVolatility = 18;
	static String[] listOfSymbols = {"STOCK#1", "STOCK#2", "STOCK#3"};
	static int maxCount = 0;
	static boolean replay = false;
	
	public TickGenerator(String symbol, long pause, int maxCount, boolean replay) {
		
		String currency = wld.getCurrencyForSymbol(symbol);
		String type = "STK";
		int tickerID = wld.getIDForStock(symbol);
		this.maxCount = maxCount;
		this.replay = replay;
		
		publishTicks(symbol, pause, currency, type, tickerID);
		
	}



	private void publishTicks(String symbol, long pause, String currency,
			String type, int tickerID) {
		
		Double lastPrice = null;
		int count = 1;
		long start = System.currentTimeMillis();
		
		MessageProducer mp = amq.createMessageProducer("tickPriceChannel", 0, false);
		
		
		while (true) {
			// 1 indicates up - 2 indicates down
			int upOrDown = cf.getRandom(1, 2);
			
			lastPrice = getLastPrice(symbol, lastPrice, upOrDown);
			
			logger.debug("Publishing new BID Tick for " + symbol + ", with bid price " + lastPrice);
			// System.out.println("Publishing new BID Tick for " + symbol + ", with bid price " + lastPrice);
			
			//(String symbol, String currency, String type, int tickerID, long timestamp, int field, String priceField, double price, int canAutoExecute, boolean replay)
			Tick tick = new Tick(symbol, currency, type, tickerID, dbi.getTimestampFromDate(), tc.convertPriceValueToField("BID"), "BID", lastPrice, 1, replay );
			amq.publishTick(tick, mp);
			
			int b = cf.getRandom(1, 5);
			Double diff = 0.01 * (double) b;
			
			Double askPrice = cf.round(lastPrice -diff, 2);
			logger.debug("Publishing new ASK Tick for " + symbol + ", with ask price " + askPrice);
			Tick askTick = new Tick(symbol, currency, type, tickerID, dbi.getTimestampFromDate(), tc.convertPriceValueToField("ASK"), "ASK", askPrice, 1, replay );
			amq.publishTick(askTick, mp);
			
			if (count > 0 && count % 10000 == 00) {
				long end = System.currentTimeMillis();
				System.out.println("Published 10000 messages for " + symbol + " in " + (end - start) + " msecs ...");
				start = System.currentTimeMillis();
			}
			
			try {
				Thread.sleep(pause);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (this.maxCount > 0 && this.maxCount == count) {
				break;
			}
			count++;	
		}
		
		amq.closeMessageProducer(mp);
		
	}



	private Double getLastPrice(String symbol, Double lastPrice, int upOrDown) {
		if (lastPrice == null) {
			lastPrice = tpd.getLastPriceForSymbol(symbol, "BID");
			if (lastPrice == null) {
				// If there is no last price, create a new random value
				lastPrice = (double)cf.getRandom(10, 100);
			}
		} else {
			// If there is an available price, adjust it by either adding or subtracting a random amount
			int a = cf.getRandom(1, maxVolatility);
			Double diff = 0.01 * (double) a;
			
			if (upOrDown == 1) {
				lastPrice = cf.round(lastPrice + diff, 2); 
			} else {
				lastPrice = cf.round(lastPrice - diff,2);
			}
			
		}
		return lastPrice;
	}
	
	
	
	public static void main (String[] args) {
		
		
		try {
			Options options = new Options();
			options.addOption("c", true, "Config directory");
			options.addOption("s", true, "List of symbols to generate ticks for (default is: STOCK#1, STOCK#2, STOCK#3");
			options.addOption("p", true, "Pause factor (in msecs) - after each tick, pause for x msecs (default is: 500)");
			options.addOption("m", true, "Max volatility of tick prices (default: 18) - formula is 0.01x18=0.18");
			options.addOption("t", true, "Set a max count of ticks to create (default: unlimited)");
			options.addOption("r", true, "Set the replay flag - ignores writing to database (default: false)");
			
			if (args.length == 0) {
				System.out.println("*** Missing arguments: ***");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "runTickGenerator.sh|.bat", options );
				System.exit(0);
			}
			
			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse( options, args);
			
			// Get Properties Instance
			PropertiesFactory pf = PropertiesFactory.getInstance();
	
			// Set the configuration directory
			String configDir;
			if (cmd.getOptionValue("c") != null) {
				configDir = cmd.getOptionValue("c");
			} else {
				configDir = "conf";
			}
			
			// First make sure to set the config directory
			pf.setConfigDir(configDir);
			
			// Set the max Volatility
			if (cmd.getOptionValue("m") != null) {
				maxVolatility = Integer.valueOf(cmd.getOptionValue("m"));
			} 
			
			// Set the max Volatility
			if (cmd.getOptionValue("r") != null) {
				replay = Boolean.valueOf(cmd.getOptionValue("r"));
			} 
			
			// Set the pause factor
			if (cmd.getOptionValue("p") != null) {
				pause = Long.valueOf(cmd.getOptionValue("p"));
			} 
			
			// Set the list of symbols
			if (cmd.getOptionValue("s") != null) {
				listOfSymbols = cmd.getOptionValues("s");
			} 

			// Set the maxCount
			if (cmd.getOptionValue("t") != null) {
				maxCount = Integer.valueOf(cmd.getOptionValue("t"));
			} 
			
		
			int threadCount = listOfSymbols.length;
			
			System.out.println("********************************");
			System.out.println("* Running new TickGenerator ");
			System.out.println("* Setting conf directory to " + configDir);
			System.out.println("* Setting pause factor to " + pause);
			System.out.println("* Setting maxVolatility to " + maxVolatility);
			System.out.println("* Setting maxCount (per symbol) to " + (maxCount *2));
			System.out.println("* Setting Replay flag to " + replay);
			System.out.println("* Pausing publishing for " + pause + " msecs, after each published Tick");
			System.out.println("* Publishing performance metrics after every 10000 messages");
			System.out.println("********************************");
			
			Thread[] threads = new Thread[threadCount];
			for (int i = 0; i < threadCount; i++ ){
				final String symbol = listOfSymbols[i];
	
				threads[i] = new Thread() {
					public void run() {				
	
						new TickGenerator(symbol, pause, maxCount, replay);
						
					}
				};
				
				threads[i].setName("TickGenerator for " + symbol);
				threads[i].start();
			}
			
			for (int i = 0; i < threads.length; i++) {
				threads[i].join();
			}
		} catch (Exception e) {
			
		}
		

		
		System.out.println("* Finished TickGenerator ");
		System.exit(0);
		
		
	}
	

}
