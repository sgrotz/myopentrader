

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

import java.util.ArrayList;

import org.mot.common.db.OrderDAO;
import org.mot.common.objects.StrategyAnalysis;
import org.mot.common.tools.EmailFactory;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.DateBuilder;
import org.mot.common.util.StrategyAnalyser;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class EmailEngine implements Job {
	
	OrderDAO od = new OrderDAO();
	DateBuilder db = new DateBuilder();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		// Get the last order date
		String lastOrderDate = od.getLastOrderDate();
		
		// Calculate the difference between the last order and todays date
		Long diffDays = db.calculateDifferenceInDays(lastOrderDate, db.getTimeStampWithPattern("yyyy-MM-dd HH:mm:ss.SSS"), "yyyy-MM-dd HH:mm:ss.SSS");
		
		// Only send an updated status email, if there were new orders. 
		// difference in days = 0 means within the last 24 hours...
		if (diffDays == 0) {
			StrategyAnalyser sa = new StrategyAnalyser();
			ArrayList<StrategyAnalysis> salist = sa.analyseAll();
			
			String mailBody = sa.getAnalysisAsString(salist);
			String date = new DateBuilder().getTimeStampWithPattern("yyyy-MM-dd");
			
			
			EmailFactory ef = new EmailFactory();
			ef.sendEmail(null, "Daily Trade Report for " + date, mailBody);
		}

		
	}
	
	
	public static void main(String[] args) {
		
		// Get Properties Instance
		PropertiesFactory pf = PropertiesFactory.getInstance();

		// First make sure to set the config directory
		pf.setConfigDir(args[0]);
		
		EmailEngine e = new EmailEngine();
		try {
			e.execute(null);
		} catch (JobExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

}
