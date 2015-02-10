

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
  
  
  package org.mot.core.listener;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.mot.common.conversion.TickConverter;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.esper.EsperFactory;
import org.mot.common.objects.Tick;
import org.mot.common.tools.PriceTracker;



public class TickMessageListener implements MessageListener {

	TickConverter tc = new TickConverter(); 
	EsperFactory ef = EsperFactory.getInstance();
	PriceTracker pt = PriceTracker.getInstance();
	
	Logger logger = Logger.getLogger(getClass());
	
	@Override 
	public void onMessage(Message msg) {
		// TODO Auto-generated method stub
		
		BytesMessage message = (BytesMessage) msg;
		
		TickPriceDAO tpd = new TickPriceDAO();
		WatchListDAO wld = new WatchListDAO();
		
			byte[] bytes;
			try {
				bytes = new byte[(int) message.getBodyLength()];
				message.readBytes(bytes);
				
				Tick tick = Tick.deserialize(bytes);
				
				// For some odd reason, it can happen that the first couple of messages are interrupted. Ignore them...
				// Will throw an java.io.StreamCorruptedException: invalid stream header: 00000000
				if (tick != null) {
					
					long start = System.currentTimeMillis();
					
					String symbol = wld.getStockForID(tick.getTickerID());
					String field = tick.getPriceField();
					Double value = tick.getPrice();
					
					if ((field.equals("ASK") || field.equals("BID")) && !field.equals(null)) {
						// Send only the ASK/BID ticks to Esper
						//ef.sendEsperEvent(tick);
					
						logger.trace("Successfully sent esper event for: " + tick.toString());
						
					}
	
					if (!tick.isReplay()) {
						// Add Tick to Database
						tpd.addNewTickPrice(symbol, tick.getPrice(), tc.convertPriceFieldToValue(tick.getField()), tick.getTimestamp());
						
						logger.trace("Added to DB: " + tick.getTimestamp());
					}
					
					// Make sure to update the price tracker with the latest price information
					pt.updatePrice(symbol + "-" + field, value);
					long end = System.currentTimeMillis();
					logger.debug("Processing tick " + tick.toString() + " for symbol " + symbol + " took " + (end - start) + " msecs...");
					//System.out.println("Processing tick " + tick.toString() + " for symbol " + symbol + " took " + (end - start) + " msecs...");
					
					//Thread.sleep(500);
					
				}
				
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					msg.acknowledge();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		
	}

}
