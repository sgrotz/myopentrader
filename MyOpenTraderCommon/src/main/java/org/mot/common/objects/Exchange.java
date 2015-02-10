

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

public class Exchange implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String exchangeID;
	String exchange;
	String name;
	String open; 
	String close;
	String timezone; 
	
	
	
	
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getExchangeID() {
		return exchangeID;
	}
	public void setExchangeID(String exchangeID) {
		this.exchangeID = exchangeID;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getClose() {
		return close;
	}
	public void setClose(String close) {
		this.close = close;
	}
	
	
	
	public Exchange(String exchangeID, String exchange, String name,
			String open, String close) {
		super();
		this.exchangeID = exchangeID;
		this.exchange = exchange;
		this.name = name;
		this.open = open;
		this.close = close;
	}
	
	public Exchange() {
		super();
	}
	
	
	
	
}
