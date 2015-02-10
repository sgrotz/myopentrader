

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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.DateBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This is the TickArchiver job class - it is used to periodically clean up the tickprice table and move old ticks 
 * (default is everything older than 90 days) to the tickArchive table. 
 * 
 * @author stephan
 *
 */
public class TickArchiver implements Job {

	DateBuilder db = new DateBuilder();
	TickPriceDAO tpd = new TickPriceDAO();
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		String pattern = "yyyy-MM-dd";
		int daysToArchive = 90; //Default to 90 days
		
		// check the configuration for how many days to archive
		PropertiesFactory pf = PropertiesFactory.getInstance();
		String pathToConfigDir = pf.getConfigDir();
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(pathToConfigDir + "/scheduler.properties");
			
			daysToArchive = config.getInt("tickarchiver.archiveAfterDays", 90);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		String archiveUpTo = db.subtractDaysFromDate(db.getTimeStampWithPattern(pattern), pattern, daysToArchive);
		
		tpd.archiveTicks(archiveUpTo);
		
	}

}
