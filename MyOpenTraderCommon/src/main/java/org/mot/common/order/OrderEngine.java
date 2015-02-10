

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
  
  
  package org.mot.common.order;

import javax.jms.MessageProducer;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mot.common.db.ExchangeDAO;
import org.mot.common.db.OrderDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.objects.Order;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderEngine {
	
	
	Logger logger = LoggerFactory.getLogger(getClass());
	private ActiveMQFactory amf = ActiveMQFactory.getInstance();
	private WatchListDAO wld = new WatchListDAO();
	private ExchangeDAO ed = new ExchangeDAO();
	private OrderDAO od = new OrderDAO();
	
	static Configuration config;
	static Double txnPct;
	static int min;
	static int max;
	
	private OrderEngine() {
		PropertiesFactory pf = PropertiesFactory.getInstance();
		String pathToConfigDir = pf.getConfigDir();
		try {
			config = new PropertiesConfiguration(pathToConfigDir + "/config.properties");
			txnPct = config.getDouble("order.transactionCostPerOrder.pct", 0.0024);
			min = config.getInt("order.barrier.sellIncrement.min", 10);
			max = config.getInt("order.barrier.sellIncrement.max", 25);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static OrderEngine instance = null; 
	
	public static OrderEngine getInstance() {
		if (instance == null) {
			return new OrderEngine();
		} else { 
			return instance;
		}
	}
	


	/**
	 * The main method, which will execute the order. It also determines, if the order is being written to the DB
	 * 
	 * @param order - order to execute
	 * @param writeToDB - boolean - shall the order get persisted in the database?
	 * @return
	 */
	public boolean executeOrder(Order order, boolean writeToDB) {
		
		
		logger.debug("Received new order " + order.getID() + " for symbol " + order.getSymbol());
		String exchange = wld.getExchangeForSymbol(order.getSymbol());
		
		boolean open = ed.isExchangOpen(exchange);
		logger.debug("Exchange for " + order.getID() + " is open: " + open);
		
		// Only process if the exchange is open
		if ( open || order.isSimulated()) {
			
			//This is a security valve ... always set the simulation flag to true...
			//order.setSimulated(true); 
			//order.setQuantity(1);
			
			if (writeToDB) {
				// Writing the order to the database
				logger.debug("Adding order " + order.getID() + " to database ...");
				od.addNewOrder(order);
				
				if (order.getBUYSELL() == "SELL" && order.getClosed() != null) {
					logger.debug("Marking order " + order.getID() + " as closed ...");
					od.markOrderAsClosed(order.getID(), order.getClosed(), order.isForced());
				}
			}
			
		
			// Only execute if this is not a simulation
			if (!order.isSimulated()) { 
				MessageProducer mp = amf.createMessageProducer("orderChannel", 0, true);
				// Then placing the order on the exchange
				amf.publishOrder(order, mp);
				amf.closeMessageProducer(mp);
			}
			
			return true;
		} else {
			logger.debug("Exchange " + exchange + " is closed - ignoring order!");
			return false; 
		}
		
	}
	
	
	
	public static void main (String args[]) {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		
		String configDir = args[0];

		// First make sure to set the config directory
		pf.setConfigDir(configDir);
				
		OrderEngine oe = new OrderEngine();
		//oe.strategyWatchdog();
		
		/*
		String buySell = args[1];
		String symbol = args[2];
		String quantity = args[3];
		
		Order order = new Order();
		order.setBUYSELL(buySell);
		order.setSymbol(symbol);
		order.setQuantity(Integer.valueOf(quantity));
		order.setSimulated(false);
		order.setStrategy("manual");
		order.setSimulated(false);
		
		if (args.length > 4) {
			String price = args[4];
			order.setPrice(Double.valueOf(price));
		}
		
		OrderEngine oe = new OrderEngine();
		//oe.evaluateOrder(order, null, true);
		
		//oe.executeOrder(order, true);
		
		*/
		
		System.exit(0);
		
	}
	
	
}
