

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
  
  
  package org.mot.common.tools;

import java.util.ArrayList;

import org.mot.common.db.TickHistoryDAO;
import org.mot.common.db.TickPriceDAO;

public class TrendCalculator {

	private String symbol;
	private int timeFrame;
	private String frequency;
	private String field;
	private Double priceDifference;
	private Double priceDifferenceInPct;
	private boolean historic;
		
	
	
	
	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}


	public Double getPriceDifferenceInPct() {
		return priceDifferenceInPct;
	}


	public Double getPriceDifference() {
		return priceDifference;
	}


	public String getSymbol() {
		return symbol;
	}


	public int getTimeFrame() {
		return timeFrame;
	}

	public String getFrequency() {
		return frequency;
	}


	/**
	 * @param symbol - Symbol to calculate trend for
	 * @param timeFrame - Units of time to go back
	 * @param frequency - internal frequency (1 day, 1 min etc)
	 */
	public TrendCalculator(String symbol, int timeFrame, String frequency, String field, boolean historic) {
		
		this.symbol = symbol; 
		this.timeFrame = timeFrame;
		this.frequency = frequency;
		this.historic = historic;
		this.field = field;
		
		calculate();
		
	}
	

	private synchronized void calculate () {
		
		ArrayList<Double> prices = null;
		Double lastPrice = null;
		
		if (historic) {
			TickHistoryDAO thd = new TickHistoryDAO();
			prices = thd.getHistoricPriceForStock(this.symbol, this.frequency, this.timeFrame);
			//lastPrice = thd.getLastHistoricPriceForStock(this.symbol);
		} else {
			TickPriceDAO tpd = new TickPriceDAO();
			prices = tpd.getPriceForSymbol(this.symbol, this.timeFrame, field);
			//lastPrice = tpd.getLastPriceForStock(this.symbol);
			
		}
		
		if (prices.size() != 0) {
			// Get the last price of the entire range
			lastPrice = prices.get(0);
			Double firstPrice = prices.get(prices.size() -1);
			
			this.priceDifference = lastPrice - firstPrice;
			
			this.priceDifferenceInPct = (this.priceDifference / lastPrice * 100);
		} else {
			// Make sure to always return at least 0.0 
			
			this.priceDifference = 0.0;
			
			this.priceDifferenceInPct = 0.0;
		}
		
	}
	
	
	
	
	
	
}
