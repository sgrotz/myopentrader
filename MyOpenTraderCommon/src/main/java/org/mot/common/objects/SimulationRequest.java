

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class SimulationRequest implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5205603515405247838L;
	
	private String symbol; 
	private String className;
	private String frequency;
	private int quantity;
	//private int min;
	//private int max; 
	private String startDate;
	private String endDate;
	//private int increment; 
	private Double txnPct;
	//private int mvgAvg1;
	//private int mvgAvg2;
	private Double minProfit = 2.0;
	private String loadValues;
	
		
	

	/**
	 * @param symbol
	 * @param className
	 * @param frequency
	 * @param quantity
	 * @param startDate
	 * @param endDate
	 * @param txnPct
	 * @param minProfit
	 * @param loadValues
	 */
	public SimulationRequest(String symbol, String className, String frequency, int quantity, String startDate, String endDate, Double txnPct, Double minProfit, String loadValues) {
		super();
		this.symbol = symbol;
		this.className = className;
		this.frequency = frequency;
		this.quantity = quantity;
		this.startDate = startDate;
		this.endDate = endDate;
		this.txnPct = txnPct;
		this.minProfit = minProfit; 
		this.loadValues = loadValues;
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





	/**
	 * @return the txnPct
	 */
	public Double getTxnPct() {
		return txnPct;
	}





	/**
	 * @param txnPct the txnPct to set
	 */
	public void setTxnPct(Double txnPct) {
		this.txnPct = txnPct;
	}





	/**
	 * @return the minProfit
	 */
	public Double getMinProfit() {
		return minProfit;
	}





	/**
	 * @param minProfit the minProfit to set
	 */
	public void setMinProfit(Double minProfit) {
		this.minProfit = minProfit;
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





	public byte[] serialize() {
		// TODO Auto-generated method stub
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        
        try {
        	ObjectOutputStream o = new ObjectOutputStream(b);
			o.writeObject(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return b.toByteArray();

	}
	
    public static SimulationRequest deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (SimulationRequest)o.readObject();
    }
	
	
	
}
