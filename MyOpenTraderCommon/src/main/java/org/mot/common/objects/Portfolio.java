

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

public class Portfolio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5229348832621529868L;
	
	private Long Timestamp;
	private String Symbol;
	private String Type; 
	private int Position;
	private double Price;
	private double avgPrice;
	private double MarketValue;
	private double UPNL;
	private double PNL;
	
	
	
	public Portfolio() {} ;
	
	public Portfolio(Long timestamp, String symbol, String type, int position,
			double price, double avgPrice, double marketValue, double uPNL,
			double pNL) {
		super();
		Timestamp = timestamp;
		Symbol = symbol;
		Type = type;
		Position = position;
		Price = price;
		this.avgPrice = avgPrice;
		MarketValue = marketValue;
		UPNL = uPNL;
		PNL = pNL;
	}
	
	
	public Long getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(Long timestamp) {
		Timestamp = timestamp;
	}
	public String getSymbol() {
		return Symbol;
	}
	public void setSymbol(String symbol) {
		Symbol = symbol;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public int getPosition() {
		return Position;
	}
	public void setPosition(int position) {
		Position = position;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}
	public double getMarketValue() {
		return MarketValue;
	}
	public void setMarketValue(double marketValue) {
		MarketValue = marketValue;
	}
	public double getUPNL() {
		return UPNL;
	}
	public void setUPNL(double uPNL) {
		UPNL = uPNL;
	}
	public double getPNL() {
		return PNL;
	}
	public void setPNL(double pNL) {
		PNL = pNL;
	}
	
	


}
