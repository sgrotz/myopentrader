

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

import org.mot.common.objects.StrategyAnalysis;
import org.mot.common.tools.EmailFactory;
import org.mot.common.util.DateBuilder;
import org.mot.common.util.StrategyAnalyser;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class EmailEngine implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		
		StrategyAnalyser sa = new StrategyAnalyser();
		ArrayList<StrategyAnalysis> salist = sa.analyseAll();
		
		String mailBody = sa.getAnalysisAsString(salist);
		String date = new DateBuilder().getTimeStampWithPattern("yyyy-MM-dd");
		
		
		EmailFactory ef = new EmailFactory();
		ef.sendEmail(null, "Daily Trade Report for " + date, mailBody);		

		
	}

}
