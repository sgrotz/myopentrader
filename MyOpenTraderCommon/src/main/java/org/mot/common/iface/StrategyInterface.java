

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
  
  
  package org.mot.common.iface;

import java.util.ArrayList;

import org.mot.common.objects.Expression;
import org.mot.common.objects.LoadValue;
import org.mot.common.objects.Order;
import org.mot.common.objects.SimulationRequest;
import org.mot.common.objects.SimulationResponse;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

	/**
	 * @author stephan
	 *
	 */
	public interface StrategyInterface extends UpdateListener {
		
		/**
		 * This is the main method for receiving any tick updates from the CEP Engine. 
		 * Two arrays are passed in, one with the new events, another one with all old events. 
		 * 
		 * (non-Javadoc)
		 * @see com.espertech.esper.client.UpdateListener#update(com.espertech.esper.client.EventBean[], com.espertech.esper.client.EventBean[])
		 */
		void update(EventBean[] newEvents, EventBean[] oldEvents);
		
		/**
		 * This method is used to return any esper expressions, which want to be run as part of the strategy. 
		 * As part of this, the code should provide a name and an expression. Use the name in the update method
		 * to access the values. 
		 */
		ArrayList<Expression> getEsperExpression();
				
		
		/**
		 * This method is used to simulate back tests. These tests are important for simulating the past 
		 * performance of a stock. This method will be called by the Backtest simulator...
		 */
		ArrayList<SimulationResponse> processSimulationRequests(SimulationRequest sr); 
		
		
		/**
		 * This method is called by the simulation loader. Simply return any combinations of simulation requests - 
		 * the simulation loader will then send to the message bus for processing.
		 */
		ArrayList<SimulationRequest> getSimulationRequests(String symbol, String className, String frequency, Integer quantity, String startDate, String endDate, Double txnPct, Double minProfit) ;
		
		
		/**
		 * This method is called by the watchdog scheduled service to check if a particular order shall be closed
		 * for "special" reasons. This could happen, if a trade is stuck or losses were too large. This is an easy way to
		 * protect for stop-lossing.
		 * 
		 * @param order - should this order be closed? 
		 * @param lastKnownPrice - the last known price as per our internal database. 
		 * @param diffPct - the difference between the order buy price and the last known price in percent
		 * @param openMinutes - the time the order has been open in minutes (if order has not been closed in x days/minutes - then close)
		 * @return - indicate if you want the order to be closed (true) or not (default - false)
		 */
		Boolean closeOrder(Order order, Double lastKnownPrice, Double diffPct, Long openMinutes);
		
		
		/**
		 * The startup method is used as the main constructor. It is called once at bootup/initialization phase.
		 * This should be used to initiate any new strategy
		 */
		void startup(String name, String symbol, LoadValue[] values, Boolean simulated, Integer amount);
		
		/**
		 * The shutdown method is not yet in use, but should be implemented in a later release. 
		 */
		void shutdown();
		
		
		
}
