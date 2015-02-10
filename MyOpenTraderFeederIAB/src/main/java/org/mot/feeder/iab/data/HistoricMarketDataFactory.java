

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
  
  
  package org.mot.feeder.iab.data;

import org.mot.common.db.TickHistoryRequestsDAO;
import org.mot.common.util.DateBuilder;
import org.mot.common.util.IDGenerator;
import org.mot.iab.client.Contract;
import org.mot.iab.client.EClientSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HistoricMarketDataFactory {
	
	private DateBuilder db = new DateBuilder();
	Logger logger = LoggerFactory.getLogger(getClass());
	private IDGenerator id = new IDGenerator();
	
	private String stock;
	private EClientSocket ecs; 
	
	
	/**
	 * Use this constructor to create a new historical market data subscriber
	 * 
	 * @param reference - pass in the reference integer value
	 * @param client - provide the EClientSocket client
	 * @param contract - Which contract to use (see contract specification)
	 * @param timestamp - up to which timestamp to collect the data
	 * @param timeAmount - How long back to get the data (1Y, 6M ...) - ignore the unit
	 * @param timeUnit - Which unit (Y, M, D ...)
	 * @param frequency - at what interval (1 min, ticks, daily)
	 * @param type - ASK, BID, ASK_BID etc
	 */
	public HistoricMarketDataFactory(String stock, EClientSocket client, Contract contract, String timestamp, String timeAmount, String timeUnit, 
			String frequency, String type) {
		
		this.stock = stock;
		this.ecs = client;
		
		processData(client, contract, timestamp, timeAmount, timeUnit, frequency, type);

				
	}
	
	public HistoricMarketDataFactory(String stock, EClientSocket client, Contract contract) {
		
		this.stock = stock;
		this.ecs = client; 
		
		TickHistoryRequestsDAO thrd = new TickHistoryRequestsDAO();
		

		try {
			
			
			/*
			long diffSecs = db.calculateDifferenceInSeconds(thrd.getLastTickHistoryUpdate(stock, "1 secs"));
			if (diffSecs > 1800) {
				// Request Data for the last 15 mins in 1 sec frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", "1800", "S", "1 secs", "BID_ASK");
				thrd.updateLastTimestamp(stock, "1 secs");
			} else if (diffSecs < 300) {
				// ignore - do nothing
			} else {
				// Request Data for the calculated seconds in 1 sec frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", String.valueOf(diffSecs), "S", "1 secs", "BID_ASK");
				thrd.updateLastTimestamp(stock, "1 secs");
			}

			
			
			
			long diff2 = db.calculateDifferenceInSeconds(thrd.getLastTickHistoryUpdate(stock, "1 min"));
			if (diff2 > 14400) {
				// Request Data for the last 2 days in 1 min frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", "2", "D", "1 min", "BID_ASK");
				thrd.updateLastTimestamp(stock, "1 min");
			} else if (diff2 < 300) {
				// ignore - do nothing
			} else {
				// Request Data since the last time it ran in 1 min frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", String.valueOf(diff2), "D", "1 min", "BID_ASK");
				thrd.updateLastTimestamp(stock, "1 min");
			}
			*/

			
			long diff3 = db.calculateDifferenceInDays(thrd.getLastTickHistoryUpdate(stock, "1 day"));
			if (diff3 == 0) { 
				// Do nothing
			} else if (diff3 < 30) {
				// Request Data since the last time it ran in 1 min frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", String.valueOf(diff3), "D", "1 day", "BID_ASK");
				thrd.updateLastTimestamp(stock, "1 day");
			} else if (diff3 > 365) {
				// Request Data for the last 2 days in 1 min frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", "1", "Y", "1 day", "BID_ASK");		
				thrd.updateLastTimestamp(stock, "1 day"); 
			} else if (diff3 > 180) {
				// Request Data for the last 2 days in 1 min frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", "1", "Y", "1 day", "BID_ASK");		
				thrd.updateLastTimestamp(stock, "1 day");
			} else if (diff3 > 90) {
				// Request Data for the last 2 days in 1 min frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", "6", "M", "1 day", "BID_ASK");
				thrd.updateLastTimestamp(stock, "1 day");
			} else if (diff3 > 60) {
				// Request Data for the last 2 days in 1 min frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", "3", "M", "1 day", "BID_ASK");
				thrd.updateLastTimestamp(stock, "1 day");				
			} else if (diff3 > 30) {
				// Request Data for the last 2 days in 1 min frequency
				processData(client, contract, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", "2", "M", "1 day", "BID_ASK");
				thrd.updateLastTimestamp(stock, "1 day");
			} else {
				// do nothing
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

				
	}
	
	
	private void processData(EClientSocket client, Contract contract, String timestamp, String timeAmount, String timeUnit, 
			String frequency, String type) {
		
/*		ecs.reqHistoricalData(this.Reference, con, db.getTimeStampWithPattern("yyyyMMdd HH:mm:ss") + " UTC", 
				db.calculateDifferenceInDays(wfw.getLastHistoryTimestamp(cf.getConnection(), this.Reference), "yyyyMMdd") + 
				" D", "1 min", "BID_ASK", 1, 1);*/
		
		String refID = new TickHistoryRequestsDAO().getTickHistoryStockRequest(this.stock, frequency);
		
		int reference = 0;
		
		if (refID == null) {
			reference = Integer.valueOf(id.getUniqueIntID());
			new TickHistoryRequestsDAO().addNewTickHistoryRequest(db.getTimestampFromDate(), String.valueOf(reference), this.stock, frequency);
		} else {
			reference = Integer.valueOf(refID);
		}
		
		logger.warn("Requesting historic data for: " + timestamp + " " + timeAmount + " " + timeUnit + " " 
				+frequency + " " + type + " as requestID: " + reference);
		
		// First make sure the historical data is in sync
		ecs.reqHistoricalData(reference, contract, timestamp, timeAmount + " " + timeUnit, frequency, type, 1, 1);

	}
	

	
	

}
