

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

import org.mot.common.db.StaticDataDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.objects.StaticData;
import org.mot.common.objects.WatchList;
import org.mot.common.tools.SeleniumGoogleReader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticDataCollector implements Job {

	Logger logger = LoggerFactory.getLogger(getClass());

	protected void startCollectors() {

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



		while (true) {
			WatchListDAO wld = new WatchListDAO();
			StaticDataDAO sdd = new StaticDataDAO();

			WatchList[] wl = wld.getWatchlistAsObject();

			for (int i = 0; i < wl.length; i++) {
				if (wl[i].getType().equals("STK")) {

					String symbol = wl[i].getSymbol();
					logger.debug("Running new Static Data Collector for " + symbol);

					SeleniumGoogleReader sgr = new SeleniumGoogleReader();

					StaticData sd = new StaticData();
					sd.setBeta(sgr.getFieldValue(symbol, "Beta"));
					sd.setDivyield(sgr.getFieldValue(symbol, "Div/yield"));
					sd.setEps(sgr.getFieldValue(symbol, "EPS"));
					sd.setMktcap(sgr.getFieldValue(symbol, "Mkt Cap"));
					sd.setOpen(sgr.getFieldValue(symbol, "Open"));
					sd.setPne(sgr.getFieldValue(symbol, "P/E"));
					sd.setRange(sgr.getFieldValue(symbol, "Range"));
					sd.setShares(sgr.getFieldValue(symbol, "Shares"));
					sd.setSymbol(symbol);
					sd.setVolavg(sgr.getFieldValue(symbol, "Vol/Avg"));
					sd.setYearRange(sgr.getFieldValue(symbol, "52week"));

					sdd.addNewStaticData(sd);
					sgr.disconnect();
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}


	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		logger.info("Starting new Static Data Collector ...");
		this.startCollectors();
		
	}

}
