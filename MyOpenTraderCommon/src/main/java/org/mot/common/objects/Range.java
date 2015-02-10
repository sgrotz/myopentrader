

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mot.common.math.CalculatorFactory;

public class Range implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double[] values;
	private Double maxPriceRange;
	private Double maxTickRange;
	private Double MinPrice;
	private Double MaxPrice;
	private Double AvgPrice;
	private int count; 
	private LinkedHashMap<Double,Integer> priceRange;
	private LinkedHashMap<Integer,Integer> tickRange;
	
	// set to 8 digits rounding by default
	private int roundToDigits = 8;

	// Sort descending by default
	private Boolean sortAscending = false;
	private Boolean ignoreNull = true;

	private CalculatorFactory cf = new CalculatorFactory();


	/**
	 * @param values - double array containing the prices
	 * @param sortAscending - shall the results get sorted ascending?
	 * @param ignoreNull - Shall 0.0 be ignored in the results?
	 * @param roundToDigits - Round the result
	 */
	public Range(Double[] values, Boolean sortAscending, Boolean ignoreNull, int roundToDigits) {
		this.values = values; 
		this.sortAscending = sortAscending; 
		this.ignoreNull = ignoreNull;
		this.roundToDigits = roundToDigits;
		this.setMaxPrice(cf.getMax(values));
		this.setMinPrice(cf.getMin(values));
		this.setAvgPrice(cf.getMean(values));
	}
	
	
	/**
	 * @return the minPrice
	 */
	public Double getMinPrice() {
		return MinPrice;
	}

	/**
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(Double minPrice) {
		MinPrice = minPrice;
	}

	/**
	 * @return the maxPrice
	 */
	public Double getMaxPrice() {
		return MaxPrice;
	}

	/**
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(Double maxPrice) {
		MaxPrice = maxPrice;
	}

	/**
	 * @return the avgPrice
	 */
	public Double getAvgPrice() {
		return AvgPrice;
	}

	/**
	 * @param avgPrice the avgPrice to set
	 */
	public void setAvgPrice(Double avgPrice) {
		AvgPrice = avgPrice;
	}

	/**
	 * @return the values
	 */
	public Double[] getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(Double[] values) {
		this.values = values;
	}

	/**
	 * @return the maxPriceRange
	 */
	public Double getMaxPriceRange() {
		return maxPriceRange;
	}

	/**
	 * @param maxPriceRange the maxPriceRange to set
	 */
	public void setMaxPriceRange(Double maxPriceRange) {
		this.maxPriceRange = maxPriceRange;
	}

	/**
	 * @return the maxTickRange
	 */
	public Double getMaxTickRange() {
		return maxTickRange;
	}

	/**
	 * @param maxTickRange the maxTickRange to set
	 */
	public void setMaxTickRange(Double maxTickRange) {
		this.maxTickRange = maxTickRange;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the priceRange
	 */
	public LinkedHashMap<Double, Integer> getPriceRange() {
		return priceRange;
	}

	/**
	 * @param priceRange the priceRange to set
	 */
	public void setPriceRange(LinkedHashMap<Double, Integer> priceRange) {
		this.priceRange = priceRange;
	}

	/**
	 * @return the tickRange
	 */
	public LinkedHashMap<Integer, Integer> getTickRange() {
		return tickRange;
	}

	/**
	 * @param tickRange the tickRange to set
	 */
	public void setTickRange(LinkedHashMap<Integer, Integer> tickRange) {
		this.tickRange = tickRange;
	}

	/**
	 * @return the roundToDigits
	 */
	public int getRoundToDigits() {
		return roundToDigits;
	}

	/**
	 * @param roundToDigits the roundToDigits to set
	 */
	public void setRoundToDigits(int roundToDigits) {
		this.roundToDigits = roundToDigits;
	}

	/**
	 * @return the sortAscending
	 */
	public Boolean getSortAscending() {
		return sortAscending;
	}

	/**
	 * @param sortAscending the sortAscending to set
	 */
	public void setSortAscending(Boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	/**
	 * @return the ignoreNull
	 */
	public Boolean getIgnoreNull() {
		return ignoreNull;
	}

	/**
	 * @param ignoreNull the ignoreNull to set
	 */
	public void setIgnoreNull(Boolean ignoreNull) {
		this.ignoreNull = ignoreNull;
	}

	public String printPriceRange() {

		String result = "** The prices were ranging from (Price/Count): ";
		HashMap<Double, Integer> tmp = this.getPriceRange();

		for (Entry<Double, Integer> entry : tmp.entrySet()) {
			result = result + entry.getKey() + "/" + entry.getValue() + " - ";
		}

		result = result + "-- Max Price: " + this.getMaxPriceRange();

		return result;

	}
	
	public String printTickRange() {

		String result = "** The ticks were ranging from (Tick/Count): ";
		HashMap<Integer, Integer> tmp = this.getTickRange();

		for (Entry<Integer, Integer> entry : tmp.entrySet()) {
			result = result + entry.getKey() + "/" + entry.getValue() + " - ";
		}

		result = result + "-- Max Tick count: " + this.getMaxTickRange();

		return result;

	}
	
	


}
