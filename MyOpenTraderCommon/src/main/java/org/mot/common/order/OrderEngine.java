

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
import org.mot.common.db.StrategyDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.objects.Order;
import org.mot.common.objects.Strategy;
import org.mot.common.objects.Strategy.Status;
import org.mot.common.tools.Collective2Connector;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderEngine {
	
	
	Logger logger = LoggerFactory.getLogger(getClass());
	private ActiveMQFactory amf = ActiveMQFactory.getInstance();
	private WatchListDAO wld = new WatchListDAO();
	private ExchangeDAO ed = new ExchangeDAO();
	private OrderDAO od = new OrderDAO();
	private Collective2Connector cc = new Collective2Connector();
	private StrategyDAO sd = new StrategyDAO();
	
	static Configuration config;
	static Double txnPct;
	static int min;
	static int max;
	
	
	
	/**
	 * Default constructor 
	 */
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
	
	
	/**
	 * Use this method to get the current order engine instance (should always be a singleton!)
	 * @return OrderEngine instance
	 */
	public static OrderEngine getInstance() {
		if (instance == null) {
			return new OrderEngine();
		} else { 
			return instance;
		}
	}
	

	/**
	 * This method validates if a particular exchange is open
	 * @param exchange - the exchange code to check (NYSE, NASDAQ)
	 * @return boolean indicator, if exchange is open (true) or closed (false)
	 */
	public boolean isExchangeOpen(String exchange) {
		return ed.isExchangOpen(exchange);
	}
	
	

	/**
	 * The main method, which will execute the order. It also determines, if the order is being written to the DB
	 * 
	 * @param order - order to execute
	 * @param writeToDB - boolean - shall the order get persisted in the database?
	 * @param ignoreIfExchangeClosed - boolean indicator, if true, this order will be ignored if the exchange is closed.
	 * @return
	 */
	public boolean executeOrder(Order order, boolean writeToDB, boolean ignoreIfExchangeClosed) {
		
		logger.debug("Received new order " + order.getID() + " for symbol " + order.getSymbol());
		String exchange = wld.getExchangeForSymbol(order.getSymbol());
		boolean open = true;
		
		// Check if the exchange is currently open
		if (!ignoreIfExchangeClosed) {
			open = this.isExchangeOpen(exchange);
			logger.debug("Exchange for " + order.getID() + " is open: " + open);
		} 
		
		// Only process if the exchange is open
		if ( open || order.isSimulated()) {
							
			/*
			 * This section will handle the closing of strategies
			 * In here we will check for the remaining open positions, and depending on being short or long, only allow the opposite side trades.
			 * This only applies, if you have marked a strategy as "CLOSING" in the DB
			 */
			String strategy = order.getStrategy();
						
			if (sd.getStrategyStatus(strategy).equals(Status.CLOSING.toString())) {

				int openOrderQuantity = od.getOpenOrderQuantity(strategy);
				
				if (openOrderQuantity == 0) {
					// We have closed all of the remaining positions, we can mark the strategy as DISABLED now
					sd.updateStrategyStatus(order.getStrategy(), Status.DISABLED);			
			
					logger.warn("Disabling strategy " + strategy + " - No more open positions! ");
					
					// Exit from the method and return false
					return false;
					
				} else if (openOrderQuantity > 0) {
					// This implies we are long on a position, so we should only allow sell trades
					if (order.getBUYSELL().equals("BUY")) {
						
						logger.debug("Strategy " + strategy + " is marked as CLOSING - no more BUY trades are allowed ...");
						
						// Do not execute the trade but exit with false;
						return false;
					}
				} else {
					// This can only happen if we are short on a position (<0)
					// Only allow to buy trades 
					if (order.getBUYSELL().equals("SELL")) {
						
						logger.debug("Strategy " + strategy + " is marked as CLOSING - no more SELL trades are allowed ...");
						// Do not execute the trade but exit with false;
						return false;
					}	
				}
			}
			
			
			
			/*
			 * The following section will manage the writing to the internal database
			 */
			if (writeToDB) {
				// Writing the order to the database
				logger.debug("Adding order " + order.getID() + " to database ...");
				od.addNewOrder(order);
				
				if (order.getBUYSELL() == "SELL" && order.getClosed() != null) {
					logger.debug("Marking order " + order.getID() + " as closed ...");
					od.markOrderAsClosed(order.getID(), order.getClosed(), order.isForced());
				}
			}
		
			
			/* 
			 * Only execute this section, if this is not a simulated order.
			 * Publish to broker for further processing
			 */
			if (!order.isSimulated()) { 
				MessageProducer mp = amf.createMessageProducer("orderChannel", 0, true);
				
				logger.debug("Sending order " + order.getID() + " to Message queue - waiting for processing ...");
				
				// Then placing the order on the queue
				amf.publishOrder(order, mp);
				amf.closeMessageProducer(mp);
			}
			
			// Pass the order to the collective2Connector, so make sure the order is listed online.
			// This may be moved to a separate order jms listener at some stage.
			cc.sendOrderToCollective2(order);
			
			
			return true;
		} else {
			logger.debug("Exchange " + exchange + " is closed - ignoring order!");
			return false; 
		}
		
	}
	
	
	/**
	 * Use this method to find out how many shares are still open for a particular strategy.
	 * @param strategy - Strategyname
	 * @return an integer representation of how many shares are still open
	 */
	public int getOpenOrderQuantity(String strategy) {
		int ret = 0;
		Order [] orders = od.getAllOpenOrders(strategy);
		for (int i = 0; i < orders.length; i++) {
			ret = ret + orders[i].getQuantity();
		}
		return ret;
	}
	
	
	
	public static void main (String args[]) {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		
		String configDir = args[0];

		// First make sure to set the config directory
		pf.setConfigDir(configDir);
				
		
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
			
		oe.executeOrder(order, true, false);
		
		
		/*
		*/
		
		System.exit(0);
		
	}
	
	
}
