

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
  
  
  package org.mot.core.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mot.common.db.OrderDAO;
import org.mot.common.db.StrategyDAO;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.Order;
import org.mot.common.order.OrderEngine;
import org.mot.common.tools.PriceTracker;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.DateBuilder;
import org.mot.common.util.IDGenerator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchDog implements Job {

	private CalculatorFactory cf = new CalculatorFactory();
	private TickPriceDAO td = new TickPriceDAO();
	private OrderDAO od = new OrderDAO();
	private IDGenerator idg = new IDGenerator();
	OrderEngine oe = OrderEngine.getInstance();
	DateBuilder db = new DateBuilder();
	Logger logger = LoggerFactory.getLogger(getClass());
	PriceTracker pt = PriceTracker.getInstance();
	private Boolean enabled; 
	private int difference;
	
	
	public WatchDog() {
		PropertiesFactory pf = PropertiesFactory.getInstance();
		String pathToConfigDir = pf.getConfigDir();
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(pathToConfigDir + "/scheduler.properties");
			enabled = config.getBoolean("watchdog.enabled", true);
			difference = config.getInt("watchdog.sellAtDifference.pct", -5);
			
			// Make sure to set the difference to -1 by default. Positive values could be damaging
			if (difference >= 0) {
				difference = -1;
			}
			
			this.strategyWatchdog();
			
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	StrategyDAO sd = new StrategyDAO();
	
	/**
	 * This strategy watchdog is responsible to check for potential losses and make sure we sell particular instrumnents,
	 * if we are stuck in a buy-and-cry scenario.
	 */
	public void strategyWatchdog() {
		
		Order[] order = od.getAllOpenOrders();
		
		for (int i = 0; i < order.length; i++) {
			
			String strategy = order[i].getStrategy();
			String className = sd.getStrategyClassNameByStrategy(strategy);
			
			Double avgPrice = order[i].getAvgBuyPrice();
			String symbol = order[i].getSymbol();
			
			// Need to check for the date of the last price indication (could be that all orders get closed after a long break)
			Double lastKnownPrice; 
			
			lastKnownPrice = pt.getPrice(symbol + "-BID");
			// The last known price can only be null, if the engine starts up and no ticks have been read in yet
			if (lastKnownPrice == null) {
				// Fetch the last known price from the database instead
				lastKnownPrice = td.getLastPriceForSymbol(symbol, "BID");
			}
			Double diffPct = cf.getDifferenceInPct(lastKnownPrice, avgPrice);
			Long openForMinutes = db.calculateDifferenceInMinutes(order[i].getTimestamp());
					
			Class<?> c;
			try {
				c = Class.forName(className);
				Object inst = c.newInstance();
			
				// Make sure the startup method is called for initialization...
				Method m1= c.getDeclaredMethod("closeOrder", Order.class, Double.class, Double.class, Long.class );
				Object ret = m1.invoke(inst, order[i], lastKnownPrice, diffPct, openForMinutes);
				Boolean closeOrder = (Boolean) ret;
				
				// if closeOrder is true - make sure to close the order
				if (closeOrder) {
					// Overwrite the Sell indicator
					order[i].setClosed(order[i].getID());
					order[i].setID(idg.getUniqueIntID());
					order[i].setBUYSELL("SELL");
					order[i].setPrice(lastKnownPrice);
					order[i].setForced(true);
					
					oe.executeOrder(order[i], true, true);
				}
						
				
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
		
		
	}

	
	
	public static void main (String args[]) {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		
		String configDir = args[0];
		
		System.out.println("Running new Watchdog :) ...");

		// First make sure to set the config directory
		pf.setConfigDir(configDir);
				
		while (true) {
			new WatchDog();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		if (enabled) {
			//System.out.println("Running new watchdog :) ...");
		
			logger.info("Spawning new watchdog instance ...");
			new WatchDog();
		}
		
	}
	
	
}
