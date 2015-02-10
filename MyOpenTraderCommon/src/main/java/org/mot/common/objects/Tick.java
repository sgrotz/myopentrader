

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


public class Tick implements Serializable {

	/** 
	 * 
	 */
	private static final long serialVersionUID = -7364813308101434083L;
	
	private String symbol;
	private String currency;
	private int tickerID;
	private int field;
	private double price;
	private String priceField; 
	private int canAutoExecute;
	private Timestamp timestamp;
	private String type;
	private boolean replay;
	
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isReplay() {
		return replay;
	}
	
	public void setReplay(boolean replay) {
		this.replay = replay;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getPriceField() {
		return priceField;
	}
	public void setPriceField(String priceField) {
		this.priceField = priceField;
	}

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String stock) {
		this.symbol = stock;
	}
	public int getTickerID() {
		return tickerID;
	}
	public void setTickerID(int tickerID) {
		this.tickerID = tickerID;
	}
	public int getField() {
		return field;
	}
	public void setField(int field) {
		this.field = field;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCanAutoExecute() {
		return canAutoExecute;
	}
	public void setCanAutoExecute(int canAutoExecute) {
		this.canAutoExecute = canAutoExecute;
	}
	
	/**
	 * @param tickerID
	 * @param field
	 * @param price
	 * @param canAutoExecute
	 */
	public Tick(int tickerID, int field, double price, int canAutoExecute) {
		this.tickerID = tickerID; 
		this.field = field;
		this.price = price;
		this.canAutoExecute = canAutoExecute;
		
	}
	
	public Tick() {
		
	}
	
	/**
	 * @param symbol
	 * @param currency
	 * @param type
	 * @param tickerID
	 * @param timestamp
	 * @param field
	 * @param priceField
	 * @param price
	 * @param canAutoExecute
	 * @param replay
	 */
	public Tick(String symbol, String currency, String type, int tickerID, Timestamp timestamp, int field, String priceField, double price, int canAutoExecute, boolean replay) {
		this.symbol = symbol;
		this.currency = currency;
		this.priceField = priceField;
		this.tickerID = tickerID; 
		this.field = field;
		this.price = price;
		this.type = type;
		this.canAutoExecute = canAutoExecute;
		this.timestamp = timestamp;
		this.replay = replay;
		
	}
	
	public String toString() {
		return "Tick details: " + this.getSymbol() + " - " + this.getPriceField() + "@" + this.getPrice() + " - with Timestamp: " + this.getTimestamp();
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
	
    public static Tick deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (Tick)o.readObject();
    }
	
	
	
}
