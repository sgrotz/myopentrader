

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
  
  
  package org.mot.feeder.iab.wrapper;


import javax.jms.MessageProducer;

import org.mot.common.conversion.TickConverter;
import org.mot.common.db.OrderDAO;
import org.mot.common.db.PortfolioDAO;
import org.mot.common.db.TickHistoryRequestsDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.mq.ActiveMQFactory;
import org.mot.common.objects.Portfolio;
import org.mot.common.objects.Tick;
import org.mot.common.objects.TickHistory;
import org.mot.common.objects.TickSize;
import org.mot.common.util.DateBuilder;
import org.mot.iab.client.CommissionReport;
import org.mot.iab.client.Contract;
import org.mot.iab.client.ContractDetails;
import org.mot.iab.client.EWrapper;
import org.mot.iab.client.Execution;
import org.mot.iab.client.Order;
import org.mot.iab.client.OrderState;
import org.mot.iab.client.TickType;
import org.mot.iab.client.UnderComp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarketDataWrapper implements EWrapper {

	
	private static final Logger logger = LoggerFactory.getLogger(MarketDataWrapper.class);
	
	ActiveMQFactory mq = ActiveMQFactory.getInstance();
	DateBuilder dbi = new DateBuilder();
	TickConverter tc = new TickConverter(); 
	WatchListDAO wfw = new WatchListDAO();
	OrderDAO od = new OrderDAO();

	PortfolioDAO pd = new PortfolioDAO();

	 
	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		logger.trace("Connection Closed:");
	} 

	@Override
	public void error(Exception arg0) {
		// TODO Auto-generated method stub
	
		logger.error("Error: " + arg0);
	}

	@Override
	public void error(String arg0) {
		// TODO Auto-generated method stub
		
		logger.error("Error: " + arg0);
		
	}

	@Override
	public void error(int arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		//logger.trace("error multiple int:");
		
		logger.error("Error: " + arg0 + "/"+arg1 + " - msg: " + arg2);
	}

	@Override
	public void accountDownloadEnd(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountSummary(int arg0, String arg1, String arg2, String arg3,
			String arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountSummaryEnd(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bondContractDetails(int arg0, ContractDetails arg1) {
		// TODO Auto-generated method stub 
		
	}

	@Override
	public void commissionReport(CommissionReport arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contractDetails(int arg0, ContractDetails arg1) {
		// TODO Auto-generated method stub
		
		System.out.println("ContractDetails " + arg1.toString());
		
	}

	@Override
	public void contractDetailsEnd(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("ContractDetailsEND");
		
	}

	@Override
	public void currentTime(long arg0) {
		// TODO Auto-generated method stub
		System.out.println("Current time: " + arg0);
	}

	@Override
	public void deltaNeutralValidation(int arg0, UnderComp arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execDetails(int arg0, Contract arg1, Execution arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execDetailsEnd(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fundamentalData(int reqID, String data) {
		// TODO Auto-generated method stub
		
		System.out.println("Fundamental data: " + data);
		
	}

	@Override
	public void historicalData(int reqID, String date, double open, double high,
			double low, double close, int volume, int count, double WAP,
			boolean hasGaps) {
		// TODO Auto-generated method stub
		TickHistoryRequestsDAO thrd = new TickHistoryRequestsDAO();
		
		logger.debug("Received historical data: " + thrd.getTickHistoryRequestStock(String.valueOf(reqID)) + " " + date + " - " + open + "/" + high + "/"+ low + "/"+ close + " -- " + WAP);
		
		TickHistory th = new TickHistory( reqID,  date,  open,  high,
				 low,  close,  volume,  count,  WAP,
				 hasGaps);	
		
		if (!date.startsWith("finished-")) {
			MessageProducer mp = mq.createMessageProducer("tickHistoryChannel", 0, false);
			
			mq.publishTickHistory(th, mp);
			//thd.addNewTickHistory(thrd.getTickHistoryRequestStock(String.valueOf(reqID)), thrd.getTickHistoryRequestValues(String.valueOf(reqID)), 
			//		date, open, high, low, close, volume, count);
			
			mq.closeMessageProducer(mp);
		}
		
		
	}

	@Override
	public void managedAccounts(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void marketDataType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextValidId(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openOrder(int arg0, Contract arg1, Order arg2, OrderState arg3) {
		// TODO Auto-generated method stub
		
		//System.out.println("OpenOrder triggered for ..." + arg1.m_localSymbol + " " + arg3.m_status);
		//arg1.m_localSymbol
		
	}

	@Override
	public void openOrderEnd() {
		// TODO Auto-generated method stub
		
		//System.out.println("OpenOrderEND triggered ...");
	}

	@Override
	public void orderStatus(int orderId, String status, int filled, int remaining,
            double avgFillPrice, int permId, int parentId, double lastFillPrice,
            int clientId, String whyHeld) {
		// TODO Auto-generated method stub
		
		logger.debug("OrderStatus triggered for ..." + orderId + " status: " + status + " filled: " + filled + " remaining: " + remaining);
		
		od.updateOrderStatus(String.valueOf(orderId), status);
		od.updateOrderPrice(String.valueOf(orderId), avgFillPrice);
		
	}

	@Override
	public void position(String arg0, Contract arg1, int arg2, double arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void realtimeBar(int arg0, long arg1, double arg2, double arg3,
			double arg4, double arg5, long arg6, double arg7, int arg8) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveFA(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerData(int arg0, int arg1, ContractDetails arg2,
			String arg3, String arg4, String arg5, String arg6) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerDataEnd(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerParameters(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickEFP(int arg0, int arg1, double arg2, String arg3,
			double arg4, int arg5, String arg6, double arg7, double arg8) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickGeneric(int tickerID, int tickType, double value) {
		// TODO Auto-generated method stub
		logger.info(tickerID + " - " + TickType.getField(tickType) + " - value " + value);
		
	}

	@Override
	public void tickOptionComputation(int arg0, int arg1, double arg2,
			double arg3, double arg4, double arg5, double arg6, double arg7,
			double arg8, double arg9) {
		// TODO Auto-generated method stub
		logger.debug("tickOptionComputation:");
		
	}

	@Override
	public void tickPrice(int tickerID, int field, double price, int canAutoExecute) {
		// TODO Auto-generated method stub
		
		if (price != 0) {
			
			String priceField = tc.convertPriceFieldToValue(field);
			//System.out.println("Price field is " + priceField + " field is " + field);
			
			if (!priceField.equals(null) && (price > 0)) {
				logger.debug("Received Tick Price: " + price + " field: " + priceField + " for Ticker: " + wfw.getStockForID(tickerID));
				
				MessageProducer mp = mq.createMessageProducer("tickPriceChannel", 0, false);
				
				Tick tick = new Tick(wfw.getStockForID(tickerID), wfw.getCurrencyForID(tickerID), wfw.getTypeForID(tickerID), tickerID, dbi.getTimestampFromDate(), field, priceField, price, canAutoExecute, false);
				mq.publishTick(tick, mp);
				
				mq.closeMessageProducer(mp);
			}

		} else {
			logger.debug("Received a tick with invalid price - will ignore ...");
		}
		//sdw.addNewTickPrice(wfw.getStockForID(cf.getConnection(), tickerID), price, tc.convertPriceFieldToValue(field));
		
	}

	@Override
	public void tickSize(int tickerID, int field, int size) {
		// TODO Auto-generated method stub
		
		logger.trace("Received Tick Size: " + size + " field: " + field);
		//logger.trace("Received Tick Size: " + size + " for field: " + tc.convertSizeFieldToValue(field)  + " for Ticker: " + wfw.getStockForID(cf.getConnection(), tickerID));
		//System.out.println("Received Tick Size: " + size + " field: " + field + " for " + tickerID);
		
		if ((size != 0) && (field == 8)) {
			
			String priceField = tc.convertSizeFieldToValue(field);
			String symbol = wfw.getStockForID(tickerID);
			Long timestamp = dbi.getTimeStampAsMillis();
			//System.out.println("Price field is " + priceField + " field is " + field);
			
			if (!priceField.equals(null) && (size > 0)) {
				logger.debug("Received Tick Size: " + size + " field: " + priceField + " for Ticker: " + symbol);
					
				MessageProducer mp = mq.createMessageProducer("tickSizeChannel", 0, false);
				TickSize ts = new TickSize(symbol, priceField, size, timestamp);
			
				mq.publishTickSize(ts, mp);
				mq.closeMessageProducer(mp);
			}

		} else {
			logger.debug("Received a tick with invalid price - will ignore ...");
		}
		
		
	}

	@Override
	public void tickSnapshotEnd(int arg0) {
		// TODO Auto-generated method stub
		logger.trace("tickSnapshotEnd:");
		
	}

	@Override
	public void tickString(int tickerID, int tickType, String value) {
		// TODO Auto-generated method stub
		logger.trace("Received Tick String: " + value + " TickType " + TickType.getField(tickType));
		
	}

	@Override
	public void updateAccountTime(String timestamp) {
		// TODO Auto-generated method stub
		// System.out.println("update Account Time triggered ... " + timestamp);
	}

	@Override
	public void updateAccountValue(String key, String value, String currency,
			String account) {
		// TODO Auto-generated method stub
		
		// System.out.println("Account Value: " + key + " is set to: " + value + " in " + currency + " (Account: " + account + ")");
		
	}

	@Override
	public void updateMktDepth(int arg0, int arg1, int arg2, int arg3,
			double arg4, int arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMktDepthL2(int arg0, int arg1, String arg2, int arg3,
			int arg4, double arg5, int arg6) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNewsBulletin(int msgID, int msgType, String message, String exchange) {
		// TODO Auto-generated method stub
		
		logger.trace("Received new News Bulletin: " + msgID + " " + msgType + " " + message + " from " + exchange);
		
		
	}

	@Override
	public void updatePortfolio(Contract contract, int position, double marketPrice,
			double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
		// TODO Auto-generated method stub
		
		logger.debug("Portfolio updated for: " + contract.m_symbol + " Position: " + position + " Price: " + marketPrice + " marketValue: " + marketValue 
				+ " Avg Cost: " + averageCost + " uPNL: " + unrealizedPNL + " PNL " + realizedPNL);
		
		//(Long timestamp, String symbol, String type, int position,
		//double price, double avgPrice, double marketValue, double uPNL,
		//double pNL)
		Portfolio p = new Portfolio(dbi.getTimeStampAsMillis(), contract.m_symbol, contract.m_secType, position, marketPrice, averageCost, marketValue, unrealizedPNL, realizedPNL);
		
		pd.addNewPortfolio(p);
		
		
		
	}

}
