

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

public class TickHistory implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2797131235079162167L;
	
	int reqID;
	String date;
	Long dateAsLong;
	String symbol;
	double open;
	double high; 
	double low;
	double close;
	int volume;
	int count;
	double WAP;
	boolean hasGaps;

	
	
	public Long getDateAsLong() {
		return dateAsLong;
	}
	public void setDateAsLong(Long dateAsLong) {
		this.dateAsLong = dateAsLong;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public int getReqID() {
		return reqID;
	}
	public void setReqID(int reqID) {
		this.reqID = reqID;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getWAP() {
		return WAP;
	}
	public void setWAP(double wAP) {
		WAP = wAP;
	}
	public boolean isHasGaps() {
		return hasGaps;
	}
	public void setHasGaps(boolean hasGaps) {
		this.hasGaps = hasGaps;
	}
	
	
	public TickHistory(int reqID, String date, double open, double high,
			double low, double close, int volume, int count, double WAP,
			boolean hasGaps) {
		this.reqID = reqID;
		this.date = date;
		this.open = open;
		this.high = high; 
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.count = count; 
		this.WAP = WAP;
		this.hasGaps = hasGaps;
	}
	
	public TickHistory() {

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
	
    public static TickHistory deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (TickHistory)o.readObject();
    }
	
			
			
	
}
