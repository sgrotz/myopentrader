

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mot.common.annotation.Cacheable;
import org.mot.common.cache.CacheModule;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorDIndicator;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;

/**
 * This is an implementation class for the TA4J libraries. They are very useful for technical analysis and 
 * provides out of the box several good indicators.
 *  
 * @author stephan
 * @see https://github.com/mdeverdelhan/ta4j
 */
public class TechnicalAnalysis {
	
	
	private DateBuilder db = new DateBuilder();
	private TickPriceDAO tpd = new TickPriceDAO();
	private CalculatorFactory cf = new CalculatorFactory();
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String pattern = "yyyy-MM-dd";
	
	private static TechnicalAnalysis instance;
	
	@Inject
	public TechnicalAnalysis() {
		
	}
	
	public static TechnicalAnalysis getInstance() {
		if (instance == null) {
			Injector injector = Guice.createInjector(new CacheModule());
	        instance = injector.getInstance(TechnicalAnalysis.class);
			
			//instance = new TechnicalAnalysis();
		}
		return instance;
	}
	

	
	/**
	 * This method is used to retrieve a timeseries from the database and convert it into a TA4J series, so that 
	 * further technical analysis can be done. 
	 * 
	 * @param symbol - symbol to search for
	 * @param startDate - start date (use pattern: yyyy-MM-dd)
	 * @param endDate - end date (use pattern: yyyy-MM-dd)
	 * @return a timeseries object
	 */
	@Cacheable (name="TechnicalAnalysis", ttl=600)
	public TimeSeries getSeriesByDate(String symbol, String startDate, String endDate) {
		
		List<String> days = db.getDaysBetweenDates(startDate, endDate, pattern);
		List<Tick> taList = new ArrayList<Tick>();
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		
		// Loop over all days
		Iterator<String> i = days.iterator();
		while (i.hasNext()) {
			
			String sd = i.next();
			String ed = db.addDaysToDate(sd, pattern, 1);
			logger.trace("Start: " + sd + " - End: " + ed);
			
			ArrayList<Double> ticks = tpd.getPriceForSymbolByDate(symbol, sd, ed, "BID");
			
			if (ticks.size()> 0 ) {
				
				Double[] values = new Double[ticks.size()];
				values = ticks.toArray(values);
				
				Decimal open = Decimal.valueOf(ticks.get(0));
				Decimal close = Decimal.valueOf(ticks.get(ticks.size()-1));
				Decimal high = Decimal.valueOf(cf.getMax(values));
				Decimal low = Decimal.valueOf(cf.getMin(values));
				
				// Default the volume to 0 for now - we dont listen to volume indication today
				Decimal volume = Decimal.valueOf(0.0);
				
				DateTime dt = formatter.parseDateTime(sd);
				
				taList.add(new Tick(dt, open, high, low, close, volume));		
			}
		}
		TimeSeries series = new TimeSeries(taList);
		
		return series;
	}
	
	/**
	 * This method returns a time series with data from the last x days. 
	 * 
	 * @param days - how many days to include in the series
	 * @param enforce - boolean value, if set to true, it ensures that at least x days are returned. (Useful to exclude weekends etc).
	 * @return a timeseries object
	 */
	
	public TimeSeries getSeriesByDays(String symbol, int days, boolean enforce) {
		
		long start = System.currentTimeMillis();
		String today = db.getTimeStampWithPattern(pattern);
		TimeSeries series = null;
		
		// Default seriesSize and iteration to 0
		int seriesSize = 0;
		int iteration = 0;
		
		// Loop until the seriesSize matches the requested amount of days or until we have done 30 iterations (otherwise we end up in infinite loop!)
		while ((seriesSize < days) && (iteration < 30)) {
			String startDate = db.subtractDaysFromDate(today, pattern, days + iteration);
			
			series = this.getSeriesByDate(symbol, startDate, today);
			seriesSize =  series.getTickCount();
			
			// Increase the iteration by the difference between days and current seriesSize
			iteration = iteration + (days - seriesSize);
		}	
		
		System.out.println("getSeriesByDays for " + symbol + "," + days + " took: " + (System.currentTimeMillis() - start) + " msecs");
		return series;
		
	}
	
	
	public TimeSeries getSeriesByDays(String symbol, String startDate, int days, boolean enforce) {
		
		String today = db.getTimestampFromDate(startDate, pattern).toString();
		TimeSeries series = null;
		
		// Default seriesSize and iteration to 0
		int seriesSize = 0;
		int iteration = 0;
		
		// Loop until the seriesSize matches the requested amount of days or until we have done 30 iterations (otherwise we end up in infinite loop!)
		while ((seriesSize < days) && (iteration < 30)) {
			String sd = db.subtractDaysFromDate(today, pattern, days + iteration);
			
			series = this.getSeriesByDate(symbol, sd, today);
			seriesSize =  series.getTickCount();
			
			// Increase the iteration by the difference between days and current seriesSize
			iteration = iteration + (days - seriesSize);
		}	
		
		return series;
		
	}
	
	
	
	
	/**
	 * Calculates the stochastic oscillator K indicator for the past 3 days.
	 * 
	 * @param days - how many days to go back (today minus x days)
	 * @param startDate - the date when to beginn looking for data
	 * @param symbol - which symbol to look for
	 * @return Double value
	 */
	//@CacheResult ()
	public Double getStochasticOscillatorKIndicator (String symbol, String startDate, int days) {
		
		long start = System.currentTimeMillis();
		Double ret = 0.0;
		
		logger.debug("Running stochastic oscillator calculation for " + symbol + " the last " + days + " days!");
		
		TimeSeries series = this.getSeriesByDays(symbol, startDate, days, true);
		int timeFrame = series.getTickCount();
		
		if (timeFrame > 0) {
			StochasticOscillatorKIndicator soki = new StochasticOscillatorKIndicator(series, timeFrame);
			ret =soki.getValue(timeFrame).toDouble(); 
		} 
		
		long end = System.currentTimeMillis();
		System.out.println("getStochasticOscillatorKIndicator - Processing symbol " + symbol + "(" + startDate + ", " + days + ") took " + (end - start) + " msecs...");

		return ret;
	}
	

	
	
	public static void main(String[] args) {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		pf.setConfigDir(args[0]);

		System.setProperty("PathToConfigDir", pf.getConfigDir());
		PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
		
		TechnicalAnalysis ta = TechnicalAnalysis.getInstance();
		
		// create a new series
		//TimeSeries series = ta.getSeriesByDate("AAPL", "2014-11-17", "2014-12-25");
		TimeSeries series = ta.getSeriesByDays("AAPL", 5, true);
		System.out.println("Series contains " + series.getTickCount() + " ticks");
		
		int lastTick = series.getTickCount() - 1;
		
		// Create a close price indicator
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		System.out.println("First value: " + series.getFirstTick().getClosePrice() + " - " + closePrice.getValue(0));
		System.out.println("Last value: " + series.getLastTick().getClosePrice() + " - " + closePrice.getValue(lastTick));
		
		SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
		System.out.println("5-ticks-SMA value at the last (" + lastTick + ") index: " + shortSma.getValue(lastTick).toDouble());
		
		StochasticOscillatorKIndicator soki = new StochasticOscillatorKIndicator(series, 3);
		System.out.println("StochasticOscillatorKIndicator value at the last (" + lastTick + ") index: " + soki.getValue(lastTick));
		StochasticOscillatorDIndicator sodi	= new StochasticOscillatorDIndicator(soki);
		System.out.println("StochasticOscillatorDIndicator value at the last (" + lastTick + ") index: " + sodi.getValue(lastTick));
		//System.out.println(sodi.size());
		
		System.exit(0);
	}

}
