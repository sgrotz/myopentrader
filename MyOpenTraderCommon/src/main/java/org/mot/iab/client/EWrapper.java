/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */



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
  
  
  package org.mot.iab.client;

public interface EWrapper extends AnyWrapper {

    ///////////////////////////////////////////////////////////////////////
    // Interface methods
    ///////////////////////////////////////////////////////////////////////
    void tickPrice( int tickerId, int field, double price, int canAutoExecute);
    void tickSize( int tickerId, int field, int size);
    void tickOptionComputation( int tickerId, int field, double impliedVol,
    		double delta, double optPrice, double pvDividend,
    		double gamma, double vega, double theta, double undPrice);
	void tickGeneric(int tickerId, int tickType, double value);
	void tickString(int tickerId, int tickType, String value);
	void tickEFP(int tickerId, int tickType, double basisPoints,
			String formattedBasisPoints, double impliedFuture, int holdDays,
			String futureExpiry, double dividendImpact, double dividendsToExpiry);
    void orderStatus( int orderId, String status, int filled, int remaining,
            double avgFillPrice, int permId, int parentId, double lastFillPrice,
            int clientId, String whyHeld);
    void openOrder( int orderId, Contract contract, Order order, OrderState orderState);
    void openOrderEnd();
    void updateAccountValue(String key, String value, String currency, String accountName);
    void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue,
            double averageCost, double unrealizedPNL, double realizedPNL, String accountName);
    void updateAccountTime(String timeStamp);
    void accountDownloadEnd(String accountName);
    void nextValidId( int orderId);
    void contractDetails(int reqId, ContractDetails contractDetails);
    void bondContractDetails(int reqId, ContractDetails contractDetails);
    void contractDetailsEnd(int reqId);
    void execDetails( int reqId, Contract contract, Execution execution);
    void execDetailsEnd( int reqId);
    void updateMktDepth( int tickerId, int position, int operation, int side, double price, int size);
    void updateMktDepthL2( int tickerId, int position, String marketMaker, int operation,
    		int side, double price, int size);
    void updateNewsBulletin( int msgId, int msgType, String message, String origExchange);
    void managedAccounts( String accountsList);
    void receiveFA(int faDataType, String xml);
    void historicalData(int reqId, String date, double open, double high, double low,
                      double close, int volume, int count, double WAP, boolean hasGaps);
    void scannerParameters(String xml);
    void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance,
    		String benchmark, String projection, String legsStr);
    void scannerDataEnd(int reqId);
    void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count);
    void currentTime(long time);
    void fundamentalData(int reqId, String data);
    void deltaNeutralValidation(int reqId, UnderComp underComp);
    void tickSnapshotEnd(int reqId);
    void marketDataType(int reqId, int marketDataType);
    void commissionReport(CommissionReport commissionReport);
    void position(String account, Contract contract, int pos, double avgCost);
    void positionEnd();
    void accountSummary(int reqId, String account, String tag, String value, String currency);
    void accountSummaryEnd(int reqId);
}

