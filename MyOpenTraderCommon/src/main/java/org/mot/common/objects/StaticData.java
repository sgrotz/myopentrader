

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

public class StaticData implements Serializable {
	
	/*
    tokens.put("Range", 	"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[1]/td[2]");
	tokens.put("52week", 	"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[2]/td[2]");
	tokens.put("Open", 		"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[3]/td[2]");
	tokens.put("Vol/Avg", 	"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[4]/td[2]");
	tokens.put("Mkt Cap", 	"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[5]/td[2]");
	tokens.put("P/E", 		"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[6]/td[2]");
	
	tokens.put("Div/yield", "//div[@id='market-data-div']/div[2]/div/table[2]/tbody/tr[1]/td[2]");
	tokens.put("EPS", 		"//div[@id='market-data-div']/div[2]/div/table[2]/tbody/tr[2]/td[2]");
	tokens.put("Shares", 	"//div[@id='market-data-div']/div[2]/div/table[2]/tbody/tr[3]/td[2]");
	tokens.put("Beta", 		"//div[@id='market-data-div']/div[2]/div/table[2]/tbody/tr[4]/td[2]");
	*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4616512794118272838L;
	
	public String symbol;
	public String range;
	public String yearRange;
	public String open;
	public String volavg;
	public String mktcap;
	public String pne;
	public String divyield;
	public String eps;
	public String shares;
	public String beta;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getYearRange() {
		return yearRange;
	}
	public void setYearRange(String yearRange) {
		this.yearRange = yearRange;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getVolavg() {
		return volavg;
	}
	public void setVolavg(String volavg) {
		this.volavg = volavg;
	}
	public String getMktcap() {
		return mktcap;
	}
	public void setMktcap(String mktcap) {
		this.mktcap = mktcap;
	}
	public String getPne() {
		return pne;
	}
	public void setPne(String pne) {
		this.pne = pne;
	}
	public String getDivyield() {
		return divyield;
	}
	public void setDivyield(String divyield) {
		this.divyield = divyield;
	}
	public String getEps() {
		return eps;
	}
	public void setEps(String eps) {
		this.eps = eps;
	}
	public String getShares() {
		return shares;
	}
	public void setShares(String shares) {
		this.shares = shares;
	}
	public String getBeta() {
		return beta;
	}
	public void setBeta(String beta) {
		this.beta = beta;
	}
	public StaticData(String symbol, String range, String yearRange,
			String open, String volavg, String mktcap, String pne,
			String divyield, String eps, String shares, String beta) {
		super();
		this.symbol = symbol;
		this.range = range;
		this.yearRange = yearRange;
		this.open = open;
		this.volavg = volavg;
		this.mktcap = mktcap;
		this.pne = pne;
		this.divyield = divyield;
		this.eps = eps;
		this.shares = shares;
		this.beta = beta;
	}
	
	public StaticData() {
		// TODO Auto-generated constructor stub
	}
	
	

}
