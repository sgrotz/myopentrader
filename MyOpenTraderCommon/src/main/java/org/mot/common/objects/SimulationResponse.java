

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
import java.sql.Timestamp;


public class SimulationResponse implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3508547225901608546L;
	private String symbol;
	private String loadValues; 
	private String frequency;
	private int quantity;
	private int tradecount;
	private Double result;
	private Double txnCost;
	private Double pnl;
	private String className;
	private Timestamp timestamp;
	private String startDate;
	private String endDate;
	private int minProfit = 2;
	
	
	
	
	/**
	 * @return the minProfit
	 */
	public int getMinProfit() {
		return minProfit;
	}
	/**
	 * @param minProfit the minProfit to set
	 */
	public void setMinProfit(int minProfit) {
		this.minProfit = minProfit;
	}
	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	/**
	 * @return the loadValues
	 */
	public String getLoadValues() {
		return loadValues;
	}
	/**
	 * @param loadValues the loadValues to set
	 */
	public void setLoadValues(String loadValues) {
		this.loadValues = loadValues;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the tradecount
	 */
	public int getTradecount() {
		return tradecount;
	}
	/**
	 * @param tradecount the tradecount to set
	 */
	public void setTradecount(int tradecount) {
		this.tradecount = tradecount;
	}
	/**
	 * @return the result
	 */
	public Double getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Double result) {
		this.result = result;
	}
	/**
	 * @return the txnCost
	 */
	public Double getTxnCost() {
		return txnCost;
	}
	/**
	 * @param txnCost the txnCost to set
	 */
	public void setTxnCost(Double txnCost) {
		this.txnCost = txnCost;
	}
	/**
	 * @return the pnl
	 */
	public Double getPnl() {
		return pnl;
	}
	/**
	 * @param pnl the pnl to set
	 */
	public void setPnl(Double pnl) {
		this.pnl = pnl;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
