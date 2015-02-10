

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
import java.sql.Timestamp;

import org.mot.common.util.DateBuilder;
import org.mot.common.util.IDGenerator;

public class Order implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3858782474474127542L;
	
	private String ID;
	private String symbol;
	private int quantity;
	private double price;
	private double avgBuyPrice;
	private String BUYSELL;
	private Timestamp timestamp;
	private boolean simulated;
	private String strategy;
	private Double barrier = 0.0;
	private String currency = "USD";
	private String closed;
	private boolean forced = false;

	public status status;
	public enum status {
		NEW, CLOSED, FCLOSED
	};
	
	public Order(String ID, String symbol, String buysell, Integer amount, Double price, Timestamp timestamp, boolean simulated) {
		this.ID = ID; 
		this.symbol=symbol;
		this.BUYSELL=buysell;
		this.quantity=amount;
		this.price=price;
		this.timestamp=timestamp;
		this.simulated=simulated;
	}
	
	public Order(String symbol, String buysell, Integer amount, Double price, boolean simulated, String strategy, String currency) {
		this.ID = idg.getUniqueIntID(); 
		this.symbol=symbol;
		this.BUYSELL=buysell;
		this.quantity=amount;
		this.price=price;
		this.timestamp=db.getTimestampFromDate();
		this.simulated=simulated;
		this.strategy = strategy;
		this.currency = currency;
	}
	
	public Order() {
		this.ID = idg.getUniqueIntID();
	}
	
	
	
	/**
	 * @return the status
	 */
	public status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(status status) {
		this.status = status;
	}

	public boolean isForced() {
		return forced;
	}
	public void setForced(boolean forced) {
		this.forced = forced;
	}
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getBarrier() {
		return barrier;
	}
	public void setBarrier(Double barrier) {
		this.barrier = barrier;
	}
	public double getAvgBuyPrice() {
		return avgBuyPrice;
	}
	public void setAvgBuyPrice(double avgBuyPrice) {
		this.avgBuyPrice = avgBuyPrice;
	}

	
	

	private static IDGenerator idg = new IDGenerator();
	private static DateBuilder db = new DateBuilder(); 

	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public boolean isSimulated() {
		return simulated;
	}
	public void setSimulated(boolean simulated) {
		this.simulated = simulated;
	}
	public String getBUYSELL() {
		return BUYSELL;
	}
	public void setBUYSELL(String bUYSELL) {
		BUYSELL = bUYSELL;
	}
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
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
	
    public static Order deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (Order)o.readObject();
    }
	

}
