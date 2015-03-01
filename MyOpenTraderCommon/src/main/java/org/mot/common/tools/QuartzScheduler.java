

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

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzScheduler {
	
	private static QuartzScheduler instance;
	private Scheduler sched;
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private QuartzScheduler() {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		try {
			sched = schedFact.getScheduler();
			
			// Make sure the scheduler only starts 120 seconds into the startup.
			sched.startDelayed(120);
			sched.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static QuartzScheduler getInstance() {
		if (instance == null) {
			instance = new QuartzScheduler();
		}
		return instance;
	}
	
	
	/**
	 * Use this method to create a new repeating job (every x seconds)
	 * 
	 * @param className
	 * @param name
	 * @param group
	 * @param seconds
	 */
	public void scheduleNewRepeatingJob(String className, String name, String group, int seconds) {
		try {
			logger.info("** Starting new repeating scheduled job for " + className + " - repeating every " + seconds + " secs... **");
			Class jobClass = Class.forName(className);
			sched.scheduleJob(this.createJob(jobClass, name, group), this.createSimpleRepeatingTrigger(name, group, seconds));
		} catch (SchedulerException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Use this method to create a new daily job (e.g at 2 pm every day)
	 * 
	 * @param className
	 * @param name
	 * @param group
	 * @param cronExpression
	 */
	public void scheduleCronJob(String className, String name, String group, String cronExpression) {
		try {
			logger.info("** Starting new daily scheduled job for " + className + " - running at " + cronExpression + " ... **");
			Class jobClass = Class.forName(className);
			sched.scheduleJob(this.createJob(jobClass, name, group), this.createCronTrigger(name, group, cronExpression));
		} catch (SchedulerException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void scheduleNewJob(String className, String name, String group) {
		try {
			logger.info("** Starting new single scheduled job for " + className + " **");
			Class jobClass = Class.forName(className);
			sched.scheduleJob(this.createJob(jobClass, name, group), this.createSimpleSingleTrigger(name, group));
		} catch (SchedulerException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private JobDetail createJob(Class jobClass, String name, String group) {
		return newJob(jobClass)
			      .withIdentity(name, group) // name "myJob", group "group1"
			      .build();
	}
	
	private Trigger createSimpleRepeatingTrigger(String name, String group, int Seconds) {
		  return newTrigger()
			      .withIdentity(name, group)
			      .startNow()
			      .withSchedule(simpleSchedule()
			          .withIntervalInSeconds(Seconds)
			          .repeatForever())
			      .build();
	}
	
	private Trigger createCronTrigger(String name, String group, String cronExpression) {
		  return newTrigger()
			      .withIdentity(name, group)
			      .withSchedule(cronSchedule(cronExpression))
			      .startNow()
			      .build();
	}
	
	
	private Trigger createSimpleSingleTrigger(String name, String group) {
		  return newTrigger()
			      .withIdentity(name, group)
			      .startNow()
			      .build();
	}

}
