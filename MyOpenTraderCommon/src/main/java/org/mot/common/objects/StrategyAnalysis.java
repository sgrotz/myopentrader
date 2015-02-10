

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
  
  
  package org.mot.common.objects;

import java.io.Serializable;
import java.util.LinkedHashMap;


public class StrategyAnalysis implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8322419338831900758L;
	private String Name;
	private Double PnL;
	private int TradeCount;
	private int quantity;
	private Double price; 
	private Double txnCost;
	private Double bps;
	private String symbol;
	private Double stddev;
	private Double priceMin = 0.0;
	private Double priceMax = 0.0;
	
	private LinkedHashMap<String, Double> highLows;
	private LinkedHashMap<String, Double> trades;
	private Double[] rollingTimeWindowAverage1;
	private Double[] rollingTimeWindowAverage2;
	
	
	
	public StrategyAnalysis(String name, Double pnL, int tradeCount,
			int quantity, Double price, Double txnCost) {
		super();
		Name = name;
		PnL = pnL;
		TradeCount = tradeCount;
		this.quantity = quantity;
		this.price = price;
		this.txnCost = txnCost;
	}
	

	public StrategyAnalysis(String name) {
		super();
		Name = name;
	}
	
	
	
	
	

	public Double getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}
	public Double getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
	}
	public Double getStddev() {
		return stddev;
	}
	public void setStddev(Double stddev) {
		this.stddev = stddev;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Double getBps() {
		return bps;
	}
	public void setBps(Double bps) {
		this.bps = bps;
	}
	public Double[] getRollingTimeWindowAverage1() {
		return rollingTimeWindowAverage1;
	}
	public void setRollingTimeWindowAverage1(Double[] rollingTimeWindowAverage1) {
		this.rollingTimeWindowAverage1 = rollingTimeWindowAverage1;
	}
	public Double[] getRollingTimeWindowAverage2() {
		return rollingTimeWindowAverage2;
	}
	public void setRollingTimeWindowAverage2(Double[] rollingTimeWindowAverage2) {
		this.rollingTimeWindowAverage2 = rollingTimeWindowAverage2;
	}
	public LinkedHashMap<String, Double> getHighLows() {
		return highLows;
	}
	public void setHighLows(LinkedHashMap<String, Double> highLows) {
		this.highLows = highLows;
	}
	public LinkedHashMap<String, Double> getTrades() {
		return trades;
	}
	public void setTrades(LinkedHashMap<String, Double> trades) {
		this.trades = trades;
	}
	public Double getTxnCost() {
		return txnCost;
	}
	public void setTxnCost(Double txnCost) {
		this.txnCost = txnCost;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Double getPnL() {
		return PnL;
	}
	public void setPnL(Double pnL) {
		PnL = pnL;
	}
	public int getTradeCount() {
		return TradeCount;
	}
	public void setTradeCount(int tradeCount) {
		TradeCount = tradeCount;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	

	
	

}
