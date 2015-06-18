

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

public class Strategy implements Serializable {
	
	public enum Status {
		ENABLED, DISABLED, CLOSING
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3580639067511153194L;
	
	private String ID;
	private String Name;
	private String Type;
	private String Symbol;
	private String LoadValues;
	private Status Status;
	private Double Amount; 
	private boolean simulated; 
	private Long timestamp;
	private String c2id;

	
	
	
	public Status getStatus() {
		return Status;
	}
	public void setStatus(Status status) {
		Status = status;
	}
	
	/**
	 * @return the c2id
	 */
	public String getC2id() {
		return c2id;
	}
	/**
	 * @param c2id the c2id to set
	 */
	public void setC2id(String c2id) {
		this.c2id = c2id;
	}
	public Double getAmount() {
		return Amount;
	}
	public void setAmount(Double amount) {
		Amount = amount;
	}
	public boolean isSimulated() {
		return simulated;
	}
	public void setSimulated(boolean simulated) {
		this.simulated = simulated;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getSymbol() {
		return Symbol;
	}
	public void setSymbol(String symbol) {
		Symbol = symbol;
	}
	public String getLoadValues() {
		return LoadValues;
	}
	public void setLoadValues(String loadValues) {
		LoadValues = loadValues;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public Strategy(String iD, String name, String type, String symbol,
			String loadValues, Status status, Long timestamp) {
		super();
		ID = iD;
		Name = name;
		Type = type;
		Symbol = symbol;
		LoadValues = loadValues;
		Status = status;
		this.timestamp = timestamp;
	}
	
	public Strategy(String iD, String name, String type, String symbol,
			String loadValues, Status status, Double amount, boolean simulated) {
		super();
		ID = iD;
		Name = name;
		Type = type;
		Symbol = symbol;
		LoadValues = loadValues;
		Status = status; 
		this.simulated = simulated;
		this.Amount = amount;
	}
	
	public Strategy() {
		super();
	}
	

	
	

}
