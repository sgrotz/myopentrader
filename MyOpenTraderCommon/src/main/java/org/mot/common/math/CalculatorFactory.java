

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
  
  
  package org.mot.common.math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


/**
 * @author stephan
 *
 */
public class CalculatorFactory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		// TODO Auto-generated method stub
		
		CalculatorFactory cf = new CalculatorFactory();

		Double[] testArray = new Double[] {1.0,2.0,3.0,4.0,5.0,4.0, 5.0, 6.0, 5.5, 6.5,7.0,8.0,9.0,10.0};
		//Number[] testArray = new Number[] {1.0,2.0,1.0,2.0,1.0,2.0,1.0,2.0,1.0,2.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};
		System.out.println("Average (over sliding window size 10): " + cf.getAverage(testArray, 10));
		System.out.println("Average (Mean): " + cf.getMean(testArray));
		
		System.out.println("Get SubList: " + Arrays.toString(cf.getSubList(testArray, 2,2)));
		System.out.println("Rolling Average: " + Arrays.toString(cf.getRollingTimeWindowAverage(testArray, 3)));
		System.out.println("Std. Deviation: " + cf.getTimeWindowDeviation(testArray, 2));
		System.out.println("Random int between 10 - 40 is: " + cf.getRandom(10, 40));
		System.out.println("Single Stochastic Oscillator is: " + cf.getStochasticOscillator(10.0, 9.2, 11.0));
		System.out.println("Stochastic Oscillator is: " + Arrays.toString(cf.getStochasticOscillatorRange(testArray, 3)));

		
	}


	public Double getAverage(Double[] data, Integer windowSize) {
		// Create a DescriptiveStats instance and set the window size to 100
		DescriptiveStatistics stats = new DescriptiveStatistics();
		stats.setWindowSize(windowSize);
		
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				stats.addValue(data[i]);
			} else {
				break;
			}
		}
		return stats.getMean();
	}

	
	
	public Double getMean(Double[] data) {
		Double sum = 0.0;
		for( Double i : data) {
		    sum += i;
		}
		
		return sum / data.length;
	}
	
	
	
	public Double getMean(ArrayList<Double> array) {
		Double[] db = new Double[array.size()];
		db = array.toArray(db);
		
		return this.getMean(db);
	}
	


	public Double[] getRollingTimeWindowAverage(Double[] data, Integer windowSize) {	
		// create a new list to store the averages
		Double[] averageList = new Double[data.length];

		// Loop over all items in the array
		for (int i = 0; i < data.length; i++) {
			if (i < windowSize ) {
				// if the window is less than the iteration - just reuse the array value
				averageList[i] = data[i];
			} else {
				// For all other values - calculate the average
				//averageList[i] = getAverage(getSubList(data, i - windowSize, windowSize), windowSize);
				averageList[i] = getMean(getSubList(data, i - windowSize, windowSize));
			}
			
		}
		
		return averageList;
	}

	public Double getTimeWindowDeviation(Number[] data, Integer windowSize) {
		// Create a DescriptiveStats instance and set the window size to 100
		DescriptiveStatistics stats = new DescriptiveStatistics();
		stats.setWindowSize(windowSize);

		if (windowSize == 0) {
			windowSize = data.length;
		}

		for (int i = 0; i < data.length; i++) {
			
			if (data[i] != null) {
				stats.addValue((Double) data[i]);
			} else {
				break;
			}
		}

		return stats.getStandardDeviation();
	}
	
	
	public Double getMax(Double[] data) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		for (int i = 0; i < data.length; i++) {
			
			if (data[i] != null) {
				stats.addValue((Double) data[i]);
			} else {
				break;
			}
		}
		
		return stats.getMax();
	}
	
	
	public Double getMin(Double[] data) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		for (int i = 0; i < data.length; i++) {
			
			if (data[i] != null) {
				stats.addValue((Double) data[i]);
			} else {
				break;
			}
		}
		return stats.getMin();
	}
	
	
	
	/**
	 * @param data - original data array, to extract sublist from
	 * @param index - at which point to start the sublist
	 * @param size - size of the sublist
	 * @return
	 */
	public Double[] getSubList(Double[] data, int index, int size) {
		
		Double[] returnNumber = new Double[size];
		returnNumber = Arrays.copyOfRange(data, index, index + size);
		
		return returnNumber;
	}
	
	/**
	 * @param value -- double value to round
	 * @param places -- decimal places to round to
	 * @return - Double value
	 */
	public Double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	
	
	/**
	 * @param start - range where to start randomizing
	 * @param max - max value to randomize
	 * @return - integer value between start and max
	 */
	public int getRandom(int start, int max) {
		Random generator = new Random(); 
		return generator.nextInt(max) + start;
	}
	
	
	/**
	 * This method is deprecated - please use org.mot.common.math.TechnicalAnalysis instead. 
	 * Is used to calculate the stochastic oscillator
	 * 
	 * @param close - Current close price
	 * @param low - lowest price
	 * @param high - highest price
	 * @return - double value containing a value between 0 - 100
	 */
	@Deprecated
	public Double getStochasticOscillator(Double close, Double low, Double high) {
		
		Double ret = (close - low) / (high - low) * 100;
		//System.out.println("Close price: " + close + " - low: " + low + " - high: " + high + " --- OSC: " + ret);
		return ret;
	}
	
	
	/**
	 * This method is deprecated - please use org.mot.common.math.TechnicalAnalysis instead. 
	 * Is used to calculate the stochastic Oscillator over a range of numbers
	 * 
	 * @param prices - array of prices
	 * @return - double array with indicator between 0 - 100
	 */
	@Deprecated
	public Double[] getStochasticOscillatorRange(Double[] prices, int windowSize) {
		
		Double[] ret = new Double[prices.length]; 
		Double high = this.getMax(prices);
		Double low = this.getMin(prices);
		
		for (int i = 0; i < prices.length; i++ ) {
			ret[i] = this.getStochasticOscillator(prices[i], low, high);
		}
		
		return ret = this.getRollingTimeWindowAverage(ret, windowSize);
	}
	

	public Double getDifferenceInPct(Double v1, Double v2) {
		return (v1 - v2) / ((v1 + v2) /2) *100;
	}
	
	
}
