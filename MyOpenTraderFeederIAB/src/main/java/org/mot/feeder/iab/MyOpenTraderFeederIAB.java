

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
  
  
  package org.mot.feeder.iab;


import javax.jms.Destination;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.mot.common.db.WatchListDAO;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.objects.WatchList;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.IDGenerator;
import org.mot.feeder.iab.data.MarketDataReader;
import org.mot.feeder.iab.listener.OrderMessageListener;
import org.mot.feeder.iab.wrapper.IABConnector;
import org.mot.iab.client.EClientSocket;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;


/**
 * This is the main class for the MyOpenTraderFeederIAB Class. This is the starting point, to feed the Interactive Brokers Data
 * to the MOT message bus. The class itself has can be run as war (in a tomcat) or directly through the main method through command line. 
 *  Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
 */
public class MyOpenTraderFeederIAB extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * The servlet class, in case it is started from within Tomcat/Jersey.
     * 
     * @see HttpServlet#HttpServlet()
     */
    public MyOpenTraderFeederIAB() {
        super(); 
        // TODO Auto-generated constructor stub
    }
   
    private static final Logger logger = LoggerFactory.getLogger(MyOpenTraderFeederIAB.class);
	
	/**
	 * The init class is used by the servlet, in case the app is started through Tomcat or Jersey.
	 * 
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
		System.out.println("Starting MyOpenTraderFeederIAB Component ...");
		logger.info("*** Initializing MyOpenTraderFeederIAB Component  ...");
		startWorkers(config.getServletContext().getRealPath("/WEB-INF/conf"), null);
				
	}

	PropertiesFactory pf = PropertiesFactory.getInstance();
	
	
	
	/**
	 * This is the main start method. Both, the servlet, as well as the main class will call this function.
	 * 
	 * @param PathToConfigDir - provide a configuration directory
	 * @param name - name of the executor
	 */
	protected void startWorkers(String PathToConfigDir, String name) {
		try {
			
			// First make sure to set the config directory
			// Make sure to init the propertiesfactory! 
			pf.setConfigDir(PathToConfigDir);
			PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
		
			// Get the client instance
			EClientSocket ecs = IABConnector.getInstance();
			
			// Generate a new client ID
			int clientID = Integer.valueOf(new IDGenerator().getUniqueIntID());
			
			// Read out the TraderWorkstation & generic properties
			Configuration twsProperties = new PropertiesConfiguration(PathToConfigDir + "/tws.properties");
			Configuration genericProperties = new PropertiesConfiguration(PathToConfigDir + "/config.properties");
			ecs.eConnect(twsProperties.getString("iab.tws.host"), twsProperties.getInt("iab.tws.port"), clientID);
			logger.info("Connected: " + ecs.isConnected());
			
			// If connected ...
			if (ecs.isConnected()) {
				
				// If no executor name is specified, use ALL as wildcard
				if (name == null) {
					// Get the engine name
					name = genericProperties.getString("engine.core.name", "ALL");
				}
				
				// Check which objects from the watchlist should run within this executor
				WatchListDAO wfw = new WatchListDAO();
				WatchList[] wl = wfw.getWatchlistByExecutorAsObject(name);
				
				// Start the market data feeds individually
				for (int i = 0; i < wl.length; i++) {
					String symbol = wl[i].getSymbol();
					Integer ID = Integer.valueOf(wl[i].getID());
					String currency = wl[i].getCurrency();
					
					// By default, assume the currency is USD, if not specified otherwise
					if (currency == null) {
						currency = "USD";
					}
					
					logger.info("*** Starting new MarketDataFeeder for " + symbol + " ...");
					new MarketDataReader(ecs, ID, symbol, currency); 
				}
				
				// Request for news updates from the API
				logger.info("Subscribing to News Bulletins ...");
				ecs.reqNewsBulletins(true);
				
				// Make sure to listen to outgoing orders from the core engine
				ActiveMQFactory amf = ActiveMQFactory.getInstance();
				
				logger.debug("Creating new order message listener ...");
				OrderMessageListener tml = new OrderMessageListener();
				Destination orders = amf.createDestination("orderChannel");
				 
				new ActiveMQFactory().createMessageConsumer(orders, tml);
				
				
			} else {
				logger.error("Can not connect to local ECClient - check your configuration ... will exit");
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getLocalizedMessage());
			System.out.println(e.getLocalizedMessage());
		} 
	}
	
	
	public static void main(String[] args){
		
		// Simple main method to run without tomcat container ... 
		try {
			
			String confDir = "conf";
			String executor = "ALL";
			if (args.length == 2) {
				if (args[0].toString() != null) {
					confDir =args[0].toString();
				}
				
				if (args[1].toString() != null) {
					executor =args[1].toString() ;
				}
			}
			
			System.out.println("Setting config path to: " + confDir);
			System.out.println("Running as executor: " + executor);
			
			
			new MyOpenTraderFeederIAB().startWorkers(confDir, executor);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("*** Please provide the config directory as an command line argument ...! ");
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}		
		
	}

}
