

 /*
  * Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *
  */
  
  
  package org.mot.core.simulation;

import javax.jms.Destination;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.DateBuilder;
import org.mot.core.simulation.listener.SimulationMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationRunner {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public static void main(String[] args)  {
		
		// Run with -w 1-42 -l TSLA -1 581 -2 730 -d
		
		Options options = new Options();
		options.addOption("c", true, "Config directory");
		options.addOption("l", true, "Single instrument/symbol.");
		options.addOption("1", true, "Moving Average 1");
		options.addOption("2", true, "Moving Average 2");
		options.addOption("w", true, "Week of Year to process");
		options.addOption("d", false, "Write results to DB (default: false)");
		
		if (args.length == 0) {
			System.out.println("*** Missing arguments: ***");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "runSimulator.sh|.bat", options );
			System.exit(0);
		}
		
		try {
			// Setting command line options
			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse( options, args);
			
			
			PropertiesFactory pf = PropertiesFactory.getInstance();
			
			if (cmd.hasOption("c")) {
				// First make sure to set the config directory
				pf.setConfigDir(cmd.getOptionValue("c"));
			} else {
				// Default the configuration directory to <ROOT>/conf
				pf.setConfigDir("conf");
			}
				
			System.setProperty("PathToConfigDir", pf.getConfigDir());
			
			// Read in properties from file
			Configuration simulationProperties;
		
			simulationProperties = new PropertiesConfiguration(pf.getConfigDir()  + "/simulation.properties");
			final String className = simulationProperties.getString("simulation.default.class", "org.mot.core.esper.AdvancedMvgAvgComparisonListenerG4");

			
			Double txnpct;
			// Get the transaction percentage
			if (cmd.getOptionValue("t") != null) {
				txnpct = Double.valueOf(cmd.getOptionValue("t"));
			} else {
				txnpct = simulationProperties.getDouble("simulation.order.txncost.pct", 0.0024);
			}
			
			String symbol = null;
			// Array for the list of instruments
			if (cmd.getOptionValue("l") != null) {
				symbol = cmd.getOptionValue("l");
			} 
			
			Configuration genericProperties = new PropertiesConfiguration(pf.getConfigDir() + "/config.properties");
			PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
			final int quantity = simulationProperties.getInt("simulation.default.quantity", 10);

			SimulationRunner rmas = new SimulationRunner();
			
			if (cmd.getOptionValue("w") != null) {
				
				String week = cmd.getOptionValue("w");
				Boolean writeToDB = false; 
				
				if (cmd.hasOption("d")) {
					writeToDB = true;
				} 
				
				int m1 = Integer.valueOf(cmd.getOptionValue("1"));
				int m2 = Integer.valueOf(cmd.getOptionValue("2"));
				
				if (week.contains("-")) {
					
					String[] weekList = week.split("-");
					int start = Integer.valueOf(weekList[0]);
					int end = Integer.valueOf(weekList[1]);
					
					
					for (int u = start; u < end; u++ ){
						rmas.processSingleRequestByWeek(m1,m2, txnpct, symbol, quantity, u, writeToDB, className);
					}
				
				} else {
					rmas.processSingleRequestByWeek(m1,m2, txnpct, symbol, quantity, Integer.valueOf(week), writeToDB, className);
				}
				
				System.exit(0);

			} else {
				rmas.startThreads(genericProperties);
				
				while (true) {
					Thread.sleep(1000);
				}
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "runMovingAverageSimulator.sh|.bat", options );

		} catch (ConfigurationException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "runMovingAverageSimulator.sh|.bat", options );// TODO Auto-generated catch block

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
	}


	private void processSingleRequestByWeek(int m1, int m2, Double txnpct,
			String symbol, int quantity, int week, boolean writeToDB, String className) {

		String startDate = null;
		String endDate = null;
		
		DateBuilder db = new DateBuilder();
		String pattern = "yyyy-MM-dd";
		startDate = db.getDateForWeekOfYear(week, pattern);
		endDate = db.addDaysToDate(startDate, pattern, 5);

		
		
/*		SimulationRequest sr = new SimulationRequest(symbol, className, "WEEKLY", quantity, m1, m2, startDate, endDate, txnpct);
		// For weekly reviews, we always want to overwrite the min profit
		sr.setMinProfit(-99999.0);
		AdvancedMvgAvgComparisonListenerG3 avg3 = new AdvancedMvgAvgComparisonListenerG3(sr);
		
		logger.info("*** Running single simulation for " + symbol);
		logger.info("Average 1: " + m1);
		logger.info("Average 2: " + m2);
		logger.info("Input taken from week: " + week);
		
		// (String stock, int quantity, int mvgAvg1, int mvgAvg2, Double[] askPrices, Double[] bidPrices) {
		StrategyAnalysis sa = avg3.processSingleRow(sr);
		
		logger.info("*** Results: ***");
		logger.info("Trades: " + sa.getTradeCount());
		logger.info("Transaction costs: " + sa.getTxnCost());
		logger.info("P / L: " + sa.getPnL());
		logger.info("Total (P/L minus txncosts): " + (sa.getPnL() - sa.getTxnCost()));
		
		if (writeToDB) {
			avg3.writeToDB(sa, sr);
		}*/
	}
	
	
	private void startThreads(Configuration genericProperties) {
		int threadCount = genericProperties.getInt("engine.core.simulation.threads");
		
		Thread[] threads = new Thread[threadCount];
		//Thread[] threads = new Thread[1];
		for (int i = 0; i < threadCount; i++ ){

			threads[i] = new Thread() {
				public void run() {				

					ActiveMQFactory amc = new ActiveMQFactory();

					logger.debug("Creating new simulation message listener ...");
					SimulationMessageListener sml = new SimulationMessageListener();
					Destination simulations = amc.createDestination("simulationChannel?consumer.prefetchSize=1");

					amc.createMessageConsumer(simulations, sml);
				}
			};
			threads[i].setName("SimulationListener");
			threads[i].start();
			//i++;

			//Thread.sleep(1000);
		}
	}
	
	
	private SimulationRunner() {

	}

}
