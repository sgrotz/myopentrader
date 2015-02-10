

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.MessageProducer;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.mot.common.db.WatchListDAO;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.objects.SimulationRequest;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimulationLoader {


	private static final Logger logger = LoggerFactory.getLogger(SimulationLoader.class);

	ActiveMQFactory amq = ActiveMQFactory.getInstance();

	
	
	public void loadSimulationRequests(String symbol, String className, String frequency, int quantity, String startDate, String endDate, Double txnPct, Double minProfit ) {
		
		Class<?> c;
		try {
			c = Class.forName(className);
			Object inst = c.newInstance();
		
			// Get the simulation requests from the strategy class
			Method m= c.getDeclaredMethod("getSimulationRequests", String.class, String.class, String.class, Integer.class, String.class, String.class, Double.class, Double.class);
			Object ret = m.invoke(inst, symbol, className, frequency, quantity, startDate, endDate, txnPct, minProfit);
			ArrayList<SimulationRequest> expressions = (ArrayList<SimulationRequest>) ret;
			
			MessageProducer mp = amq.createMessageProducer("simulationChannel", 0, false);
			
			// Iterate over the expressions
			Iterator<SimulationRequest> it = expressions.iterator();
			while (it.hasNext()) {
				SimulationRequest e = it.next();
				
				//System.out.println("Publishing new simulation request for " + symbol + " with loadValues "+ e.getLoadValues());
				amq.publishSimulationRequest(e, mp);
			}
			
			amq.closeMessageProducer(mp);
					
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public static void main(String[] args) {

		Options options = new Options();
		options.addOption("c", true, "Config directory");
		options.addOption("n", true, "Provide the name of the simulation class");
		options.addOption("l", true, "List of instruments - separated by comma (no spaces!).");
		options.addOption("t", true, "Transaction costs - normally 0.0024 bps");
		options.addOption("s", true, "Start date in the format of 2014-07-01");
		options.addOption("e", true, "End date in the format of 2014-07-31");
		options.addOption("w", true, "Week of Year to process - this will ignore -s / -e flag");
		
		if (args.length == 0) {
			System.out.println("*** Missing arguments: ***");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "runSimulationLoader.sh|.bat", options );
			System.exit(0);
		}
		
		long t1 = System.currentTimeMillis();
		
		try {
			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse( options, args);
			
			PropertiesFactory pf = PropertiesFactory.getInstance();
			
			if (cmd.getOptionValue("c") != null) {
				// First make sure to set the config directory
				pf.setConfigDir(cmd.getOptionValue("c"));
			}
				
			System.setProperty("PathToConfigDir", pf.getConfigDir());

		
			logger.debug("Starting a new Simulation ...");
	
			// Read in properties from file
			Configuration simulationProperties;
		
			simulationProperties = new PropertiesConfiguration(pf.getConfigDir()  + "/simulation.properties");


			final String frequency = simulationProperties.getString("simulation.default.frequency", "TICK");
			final int quantity = simulationProperties.getInt("simulation.default.quantity", 10);
			final Double minProfit = simulationProperties.getDouble("simulation.default.minProfit", 2.0);
			String className = simulationProperties.getString("simulation.default.class", "org.mot.client.sg.mvgAvg.SimpleMvgAvgComparison");
			
			Double txnpct;
			// Get the transaction percentage
			if (cmd.getOptionValue("t") != null) {
				txnpct = Double.valueOf(cmd.getOptionValue("t"));
			} else {
				txnpct = simulationProperties.getDouble("simulation.order.txncost.pct", 0.0024);
			}
			
			
			// Make sure to overwrite the className if provided in the command line
			if (cmd.getOptionValue("n") != null) {
				className = cmd.getOptionValue("n");
			} 
			
			
			String[] symbols;
			// Array for the list of instruments
			if (cmd.getOptionValues("l") != null) {
				symbols = cmd.getOptionValues("l");

			} else {
				symbols = simulationProperties.getStringArray("simulation.default.instruments");
			}

			String startDate = null;
			String endDate = null;
			
			if (cmd.getOptionValue("w") != null) {
				
				DateBuilder db = new DateBuilder();
				String pattern = "yyyy-MM-dd";
				startDate = db.getDateForWeekOfYear(Integer.valueOf(cmd.getOptionValue("w")), pattern);
				endDate = db.addDaysToDate(startDate, pattern, 5);
				
				
			} else {

				// Get the start date
				if (cmd.getOptionValue("s") != null) {
					startDate = cmd.getOptionValue("s");
				} else {
					startDate = simulationProperties.getString("simulation.default.date.start", "2014-07-01");
				}
				
				// get the End date
				if (cmd.getOptionValue("e") != null) {
					endDate = cmd.getOptionValue("e");
				} else {
					endDate = simulationProperties.getString("simulation.default.date.end", "2014-07-31");
				}
			}
			
			System.out.println("*** Loading new values to simulation pipeline: *** ");
			System.out.println("* Using configuration from: " + pf.getConfigDir());
			System.out.println("* for Symbol: " + symbols.toString());
			System.out.println("* Start date: " + startDate);
			System.out.println("* End date: " + endDate);
			System.out.println("* Transaction costs: " + txnpct);
			System.out.println("* Using class file: " + className );
			System.out.println("************************************************** ");
			
					
			PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
			

			Map<Integer, String> instruments = new Hashtable<Integer, String>(); 
			if (symbols[0].equals("ALL")) {
				WatchListDAO wld = new WatchListDAO();		
				instruments = wld.getWatchlistAsTable("STK");

			} else {
				for (int p = 0; p < symbols.length; p++) {
					instruments.put(p, symbols[p].trim());
				}
			}

			Iterator<Entry<Integer, String>> it = instruments.entrySet().iterator();

			while (it.hasNext()) {
				final Map.Entry<Integer, String> entry = it.next();

				SimulationLoader msa = new SimulationLoader();
				//msa.loadSimulationRequests(entry.getValue(), className, frequency, quantity,  rangemin, rangemax, maxIncrement, startDate, endDate, txnpct);
				
				// (String symbol, String className, String frequency, int quantity, String startDate, String endDate, Double txnPct, Double minProfit ) {
				msa.loadSimulationRequests(entry.getValue(), className, frequency, quantity, startDate, endDate, txnpct, minProfit);

			}
			
			System.out.println("*** FINISHED *** ");

		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "runSimulationLoader.sh|.bat", options );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "runSimulationLoader.sh|.bat", options );
			
		}

		long totalTime = (System.currentTimeMillis() - t1);

		logger.debug("End... Run took: " + totalTime + " msecs");
		System.exit(0);

	}
}
