

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
  
  
  package org.mot.common.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.MessageProducer;

import org.apache.log4j.PropertyConfigurator;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.objects.Tick;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TickPriceReplayer {

	ActiveMQFactory mq = ActiveMQFactory.getInstance();
	TickPriceDAO tpd = new TickPriceDAO();
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
	public void processByCount(String symbol, int count) {

		// Get ticks from the database
		Tick[] tickList = tpd.getTicksForSymbol(symbol, count);
		
		MessageProducer mp = mq.createMessageProducer("tickPriceChannel", 0, false);
		publishTicks(tickList, mp);
		mq.closeMessageProducer(mp);

	}

	
	public void processByDays(String symbol, int days) {

		// Get ticks from the database
		Tick[] tickList = tpd.getTicksForSymbolByDays(symbol, days);
		
		logger.debug("Received " + tickList.length + " ticks for " + symbol + " to publish ...");
		
		MessageProducer mp = mq.createMessageProducer("tickPriceChannel", 0, false);
		
		publishTicks(tickList, mp);
		mq.closeMessageProducer(mp);

	}
	


	private void publishTicks(Tick[] tickList, MessageProducer mp ) {
		
		if (tickList.length >= 0) {
			// Need to change the sorting of the array. If they are sorted descending from the DB - the order is reversed?!
			// Loop over array and publish back to broker
			for (int i = tickList.length -1; i > 0; i--) {
				tickList[i].setReplay(true);
				mq.publishTick(tickList[i], mp);
				//System.out.println("Publishing tick " + tickList[i].getTimestamp());
			}
		}
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

		PropertiesFactory pf = PropertiesFactory.getInstance();
		pf.setConfigDir(args[0]);

		System.setProperty("PathToConfigDir", pf.getConfigDir());
		PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");

		System.out.println("Starting to publish ...");

		WatchListDAO wld = new WatchListDAO();

		Map<Integer, String> instruments = wld.getWatchlistAsTable();
		Iterator<Entry<Integer, String>> it = instruments.entrySet().iterator();
		
		final int count = Integer.valueOf(args[1]);

		Thread[] threads = new Thread[instruments.size()];
		int i =0;

		while (it.hasNext()) {
			final Map.Entry<Integer, String> entry = it.next();

			threads[i] = new Thread() {
				public void run() {
					TickPriceReplayer tpr = new TickPriceReplayer();
					tpr.processByDays(entry.getValue(), count);
				}
			};
			threads[i].start();
			i++;
		}

		for (int y = 0; y < threads.length; y++) {
			try {
				threads[y].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		System.out.println("Finished publishing ...");

		// Not sure why the class doesnt automatically end. Requires to call system.exit?!?!?!?!
		System.exit(0);

	}

}
