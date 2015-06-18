

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
  
  
package org.mot.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Destination;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.mot.common.cache.CacheFactory;
import org.mot.common.conversion.LoadValueConverter;
import org.mot.common.db.AverageCalculationDAO;
import org.mot.common.db.OrderDAO;
import org.mot.common.db.StrategyAnalysisDAO;
import org.mot.common.db.StrategyDAO;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.objects.LoadValue;
import org.mot.common.objects.Strategy;
import org.mot.common.objects.WatchList;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.tools.QuartzScheduler;
import org.mot.common.util.StrategyAnalyser;
import org.mot.core.algo.GenericAlgorithm;
import org.mot.core.listener.TickHistoryMessageListener;
import org.mot.core.listener.TickMessageListener;
import org.mot.core.listener.TickSizeMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the main MyOpenTrader Core class, used to start up the core engine. 
 * The class has 2 main entry points - as it can be executed as a war file, but also through command line. For that reason
 * you will find the dependencies on the httpServlet class left in here. 
 * 
 * Make sure to provide at least a configuration directory, as well as an executor name (see 
 * http://wiki.myopentrader.org/confluence/display/MOTD/Executor for more details) to the main class. 
 * 
 * Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
 */
public class MyOpenTraderCore extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * This is required for the servlet implementation - always call the super method.
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	public MyOpenTraderCore() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LoggerFactory.getLogger(MyOpenTraderCore.class);
	PropertiesFactory pf = PropertiesFactory.getInstance();

	/**
	 * The init method is the main starting point for both, the servlet (war) and the main method
	 * in case it is executed from the command line...
	 * 
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	
		logger.info("*** Initializing Interactive Broker CORE Servlet ...");
		String pathToConfigDir = config.getServletContext().getRealPath("/WEB-INF/conf");
		startWorkers(pathToConfigDir, null);
	
	}


	/**
	 * This is the main starting point for the CORE class. From here all other subclasses are initiated and triggered. 
	 * Make sure to be careful about changes in here!
	 * 
	 * @param PathToConfigDir - provide a configuration directory to use
	 * @param name - the executor name
	 */
	protected void startWorkers(String PathToConfigDir, String name) {
	
		// First make sure to set the config directory
		pf.setConfigDir(PathToConfigDir);
	
		try {
			// Read in the properties
			Configuration genericProperties = new PropertiesConfiguration(PathToConfigDir + "/config.properties");
	
			// Make sure to set the global property and initiate logging
			PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
			logger.debug("Setting PathToCoreConfigDir property to: " + PathToConfigDir);	
	
			// If the executor is left empty, use wildcard ALL instead.
			if (name == null) {
				// Get the engine name
				name = genericProperties.getString("engine.core.name", "ALL");
			}
	
			// Start the tick message listener
			if (genericProperties.getBoolean("engine.startup.core.tickListener.enabled", true)) {
				startIndiviualTickMessageListener(name, genericProperties);
			};
	
			// Start the tick size listener
			if (genericProperties.getBoolean("engine.startup.core.tickSizeListener.enabled", true)) {
				startTickSizeListener();
			};
			
			// Start the tick history listener
			if (genericProperties.getBoolean("engine.startup.core.historyListener.enabled", true)) {
				startTickMessageHistoryListener();
			};
	
			// Start the internal scheduling engine
			startScheduler(name);
						
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


	/**
	 * This method is used to start all internal schedulers. It uses the Quartz scheduling engine for this. 
	 * Make sure the configuration in the conf/scheduler.conf is up2date. 
	 * 
	 * @param name
	 */
	private void startScheduler(String name) {

		int seconds = 60; 
		PropertiesFactory pf = PropertiesFactory.getInstance();
		String pathToConfigDir = pf.getConfigDir();
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(pathToConfigDir + "/scheduler.properties");
			
			// Make sure the scheduler only runs on the designated node. 
			// Otherwise with multiple nodes running, each job will run several times! 
			String runOn;
			if (!name.equals("ALL")) {
				runOn = config.getString("scheduler.runOn", "mot1");
			} else {
				// For development (single node) purposes - just overwrite the value to be identical!
				runOn = "ALL";
			}
			seconds = config.getInt("watchdog.frequency.seconds", 60);
			boolean enabled = config.getBoolean("scheduler.enabled", true);

			if ((enabled) && (name.equals(runOn))) {
				QuartzScheduler qs = QuartzScheduler.getInstance();
				
				boolean watchDogEnabled = config.getBoolean("watchdog.enabled", true);
				String wdClass = config.getString("watchdog.class", "org.mot.core.scheduler.WatchDog");

				if (watchDogEnabled) {
					qs.scheduleNewRepeatingJob(wdClass, "WatchDog", "base", seconds);
				}

				String archiverClass = config.getString("tickarchiver.class", "org.mot.core.scheduler.TickArchiver");
				boolean archiverEnabled = config.getBoolean("tickarchiver.enabled", true);
				
				if (archiverEnabled) {
					qs.scheduleNewJob(archiverClass, "Archiver", "base");
				}
				
				String sdrClass = config.getString("staticdatareader.class", "org.mot.core.scheduler.StaticDataCollector");
				boolean sdrEnabled = config.getBoolean("staticdatareader.enabled", true);
				int sdrFrequency = config.getInt("staticdatareader.frequency", 360000);

				if (sdrEnabled) {
					qs.scheduleNewRepeatingJob(sdrClass, "StaticDataReader", "base", sdrFrequency);
				}
				
				String simClass = config.getString("backtest.simulator.class", "org.mot.core.scheduler.BackTestSimulator");
				boolean simEnabled = config.getBoolean("backtest.simulator.enabled", true);

				if (simEnabled) {
					qs.scheduleNewJob(simClass, "Simulator", "base");
				}
				
				boolean emailEnabled = config.getBoolean("email.engine.enabled", true);

				if (emailEnabled) {
					String emailClass = config.getString("email.engine.class", "org.mot.core.scheduler.EmailEngine");
					String cronExpression = config.getString("email.engine.cronExpression", "0 1 * * *");
					qs.scheduleCronJob(emailClass, "EmailEngine", "base", cronExpression);
				}
				
			}

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * This method is just a wrapper to start each tick message listener individually.
	 * Each stock will therefore have its own dedicated tickChannel listener - this prevents bottlenecks in the messsaging layer 
	 * 
	 * @param name - The executor name
	 * @param genericProperties - pass in the generic properties
	 */
	private void startIndiviualTickMessageListener(String name, Configuration genericProperties) {
		
		WatchListDAO wld = new WatchListDAO();
		final WatchList[] wl = wld.getWatchlistByExecutorAsObject(name);
			
		for (int u = 0; u < wl.length; u++) {
			String symbol = wl[u].getSymbol();
			String type = wl[u].getType();
			String currency = wl[u].getCurrency();

	
			// Check if the CEP Engine is supposed to be started... it only makes sense to start the engine, if the tick feed was also started!
			if (genericProperties.getBoolean("engine.startup.core.enabled", true)) {
				createOrdersForStrategies(genericProperties, symbol);
			};
	
			startTickMessageListener(symbol, type, currency, genericProperties);	
			
		}
	}


	/**
	 * This method creates the actual JMS message listener for all stock prices. 
	 * 
	 * @param symbol - which symbol to listen for
	 * @param type - type of the symbol (FX, STK etc)
	 * @param currency - which currency is in use
	 * @param genericProperties - generic properties
	 */
	private void startTickMessageListener( String symbol, String type, String currency,
			Configuration genericProperties) {

		AverageCalculationDAO acd = new AverageCalculationDAO();
		ActiveMQFactory amf = new ActiveMQFactory();
		logger.debug("Creating new tick message listener ...");
		TickMessageListener tml = new TickMessageListener();
		Destination ticks = amf.createDestination("tickPriceChannel");


		/*
		 * The following section would automatically create a moving average algo with the best known combination
		 */ 

		if (genericProperties.getBoolean("engine.startup.autoCreateBestCombination")) {
			Iterator<Entry<Integer, Integer>> lm =  acd.getBestAverageCombination(symbol, "TICK", genericProperties.getInt("engine.startup.autoCreateBestCombineation.top") ).entrySet().iterator();
			if (lm.hasNext()){
				Map.Entry<Integer, Integer> lm2 = lm.next();
				String name = symbol + lm2.getKey()*10 + "-"+ lm2.getValue()*10 +"-mvg";
				//loadAlgorithm(genericProperties.getString("engine.startup.autoCreateBestCombineation.class"), name, symbol, lm2.getKey()*10, lm2.getValue()*10, 10, true);

			} 
		}

		if (type.equals("FX")) {
			// If type is FX also provide the currency
			amf.createMessageConsumer(ticks, tml, symbol, currency);
		} else {
			amf.createMessageConsumer(ticks, tml, symbol);
		}

	}
	
	
	/**
	 * This method starts the tick size listener, containing volume information etc
	 */
	private void startTickSizeListener() {

		ActiveMQFactory amf = new ActiveMQFactory();
		logger.debug("Creating new tick size message listener ...");
		TickSizeMessageListener tsml = new TickSizeMessageListener();
		Destination tickSize = amf.createDestination("tickSizeChannel");

		amf.createMessageConsumer(tickSize, tsml);

	}


	/**
	 * Receive the historic feed for a particular stock
	 */
	private void startTickMessageHistoryListener() {
		ActiveMQFactory amf = new ActiveMQFactory();
		logger.debug("Creating new tickHistory message listener ...");
		TickHistoryMessageListener thml = new TickHistoryMessageListener();
		Destination tickHistory = amf.createDestination("tickHistoryChannel");
		amf.createMessageConsumer(tickHistory, thml);
	}


	/**
	 * Automatically create orders for a particular strategy. 
	 * 
	 * @param genericProperties
	 * @param symbol
	 */
	private void createOrdersForStrategies(Configuration genericProperties, String symbol) {
		StrategyDAO sd = new StrategyDAO();
		TickPriceDAO tpd = new TickPriceDAO();
		Strategy[] strategies = sd.getStrategiesForSymbolAsObject(symbol);
	
		try {
	
			for (int u = 0; u<strategies.length; u++ ){
	
				// Read out the dynamic properties for startup ...	
				LoadValueConverter lvc = new LoadValueConverter();
				LoadValue[] values = lvc.convertStringToValues(strategies[u].getLoadValues());
	
				// Default simulated flag to true - just to be save! 
				// This is overwritten with the strategy value later on
				boolean simulated = true;
				
				// Default the amount to 10 shares
				int amount = 10;
				
				// Find out how many shares can be bought for the investment amount
				if (strategies[u].getAmount() != null) {
					amount = (int) (strategies[u].getAmount() / tpd.getLastPriceForSymbol(strategies[u].getSymbol(), "ASK")) ;
					simulated = strategies[u].isSimulated();
				}
	
				new GenericAlgorithm(strategies[u].getType(), strategies[u].getName(), strategies[u].getSymbol(), values, simulated, amount);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args){
		
		try {
			Options options = new Options();
			options.addOption("c", true, "Config directory");
			options.addOption("e", true, "Executor name (ALL is default wildcard)");
			options.addOption("h", false, "Print the command line help");
			
			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse( options, args);
	
			if (args.length == 0 || cmd.hasOption("h")) {
				System.out.println("*** Missing arguments: ***");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "runMyOpenTraderCore.sh|.bat", options );
				System.exit(0);
			}
			
			
			String confDir = "conf";
			// Get the configuration directory
			if (cmd.getOptionValue("c") != null) {
				confDir = cmd.getOptionValue("c");
			} 

			
			String executor = "ALL";
			// Get the executor
			if (cmd.getOptionValue("e") != null) {
				executor = cmd.getOptionValue("e");
			} 

			System.out.println("Setting config path to: " + confDir);
			System.out.println("Running as executor: " + executor);

			new MyOpenTraderCore().startWorkers(confDir, executor);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("*** Please provide the config directory as an command line argument ...! ");
			e.printStackTrace();
		}		

	}

}
