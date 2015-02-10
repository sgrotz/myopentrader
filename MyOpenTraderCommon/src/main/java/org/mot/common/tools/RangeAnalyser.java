

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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.PropertyConfigurator;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.Range;

public class RangeAnalyser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		pf.setConfigDir(args[0]);

		System.setProperty("PathToConfigDir", pf.getConfigDir());
		PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
		

		TickPriceDAO tpd = new TickPriceDAO();
		
		ArrayList<Double> arraylist = tpd.getPriceForSymbolByDate("MS", "2014-11-13", "2014-11-18", "ASK");
		
		Double[] array = new Double[arraylist.size()];
		arraylist.toArray(array);
		
		
		//Double[] tmp = {1.0, 2.0, 3.0, 4.0, 5.0, 4.5, 5.0, 5.5, 6.0, 6.5, 6.0, 5.5, 5.0, 6.0, 7.0, 6.0, 7.0};

		//Range range = new Range(array, false, true, 12);
		RangeAnalyser ra = new RangeAnalyser(array, false, true, 12);
		Range range = ra.getRange();
		
		System.out.println("Total range count: " + range.getCount());
		System.out.println("Min Price: " + range.getMinPrice() + " - Max Price: " + range.getMaxPrice() + " - Average Price: " + range.getAvgPrice());
		System.out.println(range.printPriceRange());
		System.out.println(range.printTickRange());
		
		System.exit(0);
		
	}
	
	
	private CalculatorFactory cf = new CalculatorFactory();
	private Range range;
	
	/**
	 * @param values - double array containing the prices
	 * @param sortAscending - shall the results get sorted ascending?
	 * @param ignoreNull - Shall 0.0 be ignored in the results?
	 * @param roundToDigits - Round the result
	 */
	public RangeAnalyser(Double[] array, Boolean sortAscending, Boolean ignoreNull, int roundToDigits) {
		range = new Range(array, false, true, 12);
		this.analyse(range);
		range.setPriceRange(this.sortPriceRange(range.getPriceRange(), range.getSortAscending()));
		range.setTickRange(this.sortTickRange(range.getTickRange(), range.getSortAscending()));
	}
	
	private Range getRange() {
		return this.range;
	}
	
	public LinkedHashMap<Double, Integer> getPriceRange() {
		LinkedHashMap<Double,Integer> priceRange = this.range.getPriceRange();	
		return priceRange; 
	}
	
	
	private void analyse(Range rangeObject) {

		Double[] range = rangeObject.getValues();
		LinkedHashMap<Double,Integer> priceRange = new LinkedHashMap<Double,Integer>();
		LinkedHashMap<Integer,Integer> tickRange = new LinkedHashMap<Integer,Integer>();
		
		if (range.length > 1) { 
			rangeObject.setCount(range.length);
			double length = 0;
			int diff = 0;		
			double maxPrice = 0;
			double maxTick = 0;

			for (int i = 0; i < range.length -1; i++) {

				// If range +1 is larger then range, add 1 to length
				if (range[i] < range[i+1]) {
					length = cf.round(length + range[i+1] - range[i], rangeObject.getRoundToDigits());
					diff++;
				} 
				// if the values are the same or range +1 is smaller than range - make an entry to the array
				else {
					// Check if there is already an existing value
					Integer priceValue = priceRange.get(length);
					if (rangeObject.getIgnoreNull() && length == 0.0) {
						// Ignore - if value is 0.0 and we need to ignore it. do nothing
					} else {
						// Add to list
						if (priceValue == null) {
							// If value is empty - add initial 1
							priceRange.put(length, 1);
						} else {
							// If there is an existing value - add 1
							priceRange.put(length, priceValue +1);
						}
	
						if (length > maxPrice) {
							maxPrice = length;
						}
					}
					
					// Do the same for the tick range as well
					Integer tickValue = tickRange.get(diff);
					if (rangeObject.getIgnoreNull() && diff == 0) {
						// Ignore - if value is 0.0 and we need to ignore it. do nothing
					} else {
						// Add to list
						if (tickValue == null) {
							// If value is empty - add initial 1
							tickRange.put(diff, 1);
						} else {
							// If there is an existing value - add 1							
							tickRange.put(diff, tickValue + 1);
						}
	
						if (diff > maxTick) {
							maxTick = diff;
						}
					}
					

					// Reset counter to 0
					length = 0.0;
					diff = 0;
				}


			}
			rangeObject.setMaxPriceRange(maxPrice);
			rangeObject.setMaxTickRange(maxTick);
			rangeObject.setPriceRange(priceRange);
			rangeObject.setTickRange(tickRange);
		}

	}

	

	// sort ASC = true
	private  LinkedHashMap<Double, Integer> sortPriceRange(Map<Double, Integer> unsortMap, final boolean order)
	{

		List<Entry<Double, Integer>> list = new LinkedList<Entry<Double, Integer>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Double, Integer>>()
				{
			public int compare(Entry<Double, Integer> o1,
					Entry<Double, Integer> o2)
			{
				if (order)
				{
					return o1.getValue().compareTo(o2.getValue());
				}
				else
				{
					return o2.getValue().compareTo(o1.getValue());

				}
			}
				});

		// Maintaining insertion order with the help of LinkedList
		LinkedHashMap<Double, Integer> sortedMap = new LinkedHashMap<Double, Integer>();
		for (Entry<Double, Integer> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}


	// sort ASC = true
	private  LinkedHashMap<Integer, Integer> sortTickRange(Map<Integer, Integer> unsortMap, final boolean order)
	{

		List<Entry<Integer, Integer>> list = new LinkedList<Entry<Integer, Integer>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Integer, Integer>>()
				{
			public int compare(Entry<Integer, Integer> o1,
					Entry<Integer, Integer> o2)
			{
				if (order)
				{
					return o1.getValue().compareTo(o2.getValue());
				}
				else
				{
					return o2.getValue().compareTo(o1.getValue());

				}
			}
				});

		// Maintaining insertion order with the help of LinkedList
		LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
		for (Entry<Integer, Integer> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
	

	
	
	

	
}
