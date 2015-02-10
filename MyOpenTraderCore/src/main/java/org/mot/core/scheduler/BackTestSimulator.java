

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
  
  
  package org.mot.core.scheduler;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mot.common.db.AverageCalculationDAO;
import org.mot.common.db.PropertyDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.DateBuilder;
import org.mot.core.simulation.SimulationLoader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BackTestSimulator implements Job {

	/*
	 * This scheduler is used for the backtest simulation. The simulation is automatically kicked off upon startup, 
	 * making sure that we test the previous performance of particular stocks. This is the same behaviour as using the 
	 * MovingAverageSimulatorLoader (in the CORE package), just automated. 
	 * 
	 * The scheduler differentiates in 3 stages: 
	 * 1) last trading day (DAY)
	 * 2) the last week (WEEK)
	 * 3) the last month (MONTH)
	 * 
	 * Therefore each stock will be tested once across the last trading day, also across the last week and lastly also 
	 * across the entire last month.
	 * 
	 * E.g - The best performance for AAPL yesterday was 10/30, but for the last week 13/34 and for last month 25/57
	 * This will be used to dynamically amend the internal triggers.
	 * 
	 * 
	 * (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		this.init();
		
	}
	
	SimulationLoader msa = new SimulationLoader();
	AverageCalculationDAO acd = new AverageCalculationDAO();
	String frequency ;
	int quantity;
	int rangemin;
	int rangemax;
	int maxIncrement;
	Double txnpct;
	Double minProfit = 2.0;
	String pattern = "yyyy-MM-dd";
	PropertyDAO cd = new PropertyDAO();
	String className;
	
	DateBuilder db = new DateBuilder();

	
	private void init() {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		
		Configuration simulationProperties;
		try {
			simulationProperties = new PropertiesConfiguration(pf.getConfigDir()  + "/simulation.properties");


			frequency = simulationProperties.getString("simulation.default.frequency", "TICK");
			quantity = simulationProperties.getInt("simulation.default.quantity", 10);
			minProfit = simulationProperties.getDouble("simulation.default.minProfit", 2.0);
			className = simulationProperties.getString("simulation.default.class", "org.mot.client.sg.mvgAvg.SimpleMvgAvgComparison");
			txnpct = simulationProperties.getDouble("simulation.order.txncost.pct", 0.0024);
			
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Map<Integer, String> instruments = new Hashtable<Integer, String>(); 

		
		WatchListDAO wld = new WatchListDAO();		
		instruments = wld.getWatchlistAsTable("STK");

		Iterator<Entry<Integer, String>> it = instruments.entrySet().iterator();

		while (it.hasNext()) {
			final Map.Entry<Integer, String> entry = it.next();
			String symbol = entry.getValue();

			this.triggerYesterdaysReplay(symbol);
			this.triggerLastWeekReplay(symbol);
			//this.triggerLastMonthReplay(symbol);
			
		}
	}
	
	
	private void triggerLastMonthReplay(String symbol) {
		
		int month = db.getMonthOfYear() + 1;
		String startDate = db.getFirstDayOfMonth(month) + "-"+ month + "-" + db.getYear();
		String endDate = db.addDaysToDate(startDate, pattern, 30);
		String frequency = "MONTH." + month;
		String property = symbol + ".MONTH." + month;

		if (!cd.doesPropertyExist(property)) {
			msa.loadSimulationRequests(symbol, className, frequency, quantity, startDate, endDate, txnpct, minProfit);
			cd.updateProperty(property, "true");
		}
	}
	
	
	private void triggerLastWeekReplay(String symbol) {
		
		int week = db.getWeekOfYear();
		String startDate = db.getDateForWeekOfYear(week -1, pattern);
		String endDate = db.addDaysToDate(startDate, pattern, 5);
		String frequency = "WEEK." + startDate + "." + endDate;
		String property = symbol + ".WEEK." + startDate + "." + endDate;
		
		if (!cd.doesPropertyExist(property)) {
			msa.loadSimulationRequests(symbol, className, frequency, quantity, startDate, endDate, txnpct, minProfit);
			cd.updateProperty(property, "true");
		}
		
		
	}
	
	private void triggerYesterdaysReplay(String symbol) {
		
		String day = db.getTimeStampWithPattern("dd");
		int month = db.getMonthOfYear() +1;
		String startDate = db.getYear() + "-" + month + "-" + (Integer.valueOf(day) - 1);
		String endDate = db.addDaysToDate(startDate, pattern, 1);
		String frequency = "DAY." + startDate + "." + endDate;
		String property = symbol + ".DAY." + startDate + "." + endDate;
		
		if (!cd.doesPropertyExist(property)) {
			msa.loadSimulationRequests(symbol, className, frequency, quantity, startDate, endDate, txnpct, minProfit);
			cd.updateProperty(property, "true");
		}
	}
	
	
	

}
