

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
  
  
  package org.mot.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.sql.Timestamp;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateBuilder {
	
	private Configuration config; 
	
	public DateBuilder() {  
		init();
	}
	
	Logger logger = LoggerFactory.getLogger(getClass());
	PropertiesFactory pf = PropertiesFactory.getInstance();
	
	private void init() {
		String pathToConfigDir = pf.getConfigDir();
		try {
			config = new PropertiesConfiguration(pathToConfigDir + "/date.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	
	public String addDaysToDate(String date, String pattern, int daysToAdd) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add(Calendar.DATE, daysToAdd);  // number of days to add
		return sdf.format(c.getTime());  // dt is now the new date
		
	}
	
	public String subtractDaysFromDate(String date, String pattern, int daysToSubtract) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add(Calendar.DATE, daysToSubtract * -1);  // number of days to add
		return sdf.format(c.getTime());  // dt is now the new date
		
	}


	public Date getDate() {
		return new Date();
	}
	
	public String getTimeStamp() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(config.getString("date.dateFormat", "yyyy-MM-dd HH:mm:ss"));
		return sdf.format(cal.getTime());
	}
	
	public Long getTimeStampAsMillis() {
		Date date = new Date();
		return date.getTime();
	}
	
	public String getTimeStampWithPattern(String pattern) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(cal.getTime());
	}	
	
	public String getTimeStampWithPattern(String pattern, String timezone) {
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(pattern);
		
		df.setTimeZone(TimeZone.getTimeZone(timezone));
		
		return df.format(date);
	}	
	
	public Date getYesterdaysDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}


	/**
	 * @param weekNumber - which week to look for
	 * @param pattern - define a pattern in which the result string will be formatted
	 * @return String value representing the first monday of the week
	 */
	public String getDateForWeekOfYear(int weekNumber, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekNumber);        
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		//System.out.println(sdf.format(cal.getTime())); 
		return sdf.format(cal.getTime());
	}


	/**
	 * @return an int number indicating which day of the week it is
	 */
	public int getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.getDate());
		return c.get(Calendar.DAY_OF_WEEK);
	}


	public Date getDateForTimezone (String timzeone) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timzeone));
		return calendar.getTime();
	}


	public int getMonthOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getDate());
		return cal.get(Calendar.MONTH);
	}
	
	
	public int getFirstDayOfMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	
	public int getWeekOfYear(Date input) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(input);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public int getWeekOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getDate());
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public int getYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getDate());
		return cal.get(Calendar.YEAR);
	}


	public Timestamp getTimestampFromDate() {

		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		return ts;
	}
	
	public Timestamp getTimestampFromDate(Date date) {

		Timestamp ts = new Timestamp(date.getTime());
		return ts;
	}	
	
	public Timestamp getTimestampFromDate(String date, String pattern) {

		DateFormat df = new SimpleDateFormat(pattern);
		Date startDate = null;
		Timestamp ts = null;
		try {
			startDate = df.parse(date);
			ts = new Timestamp(startDate.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			logger.warn("Time pattern is invalid - ignoring timestamp - trying date...");
			ts = getTimestampFromDate(date, "yyyyMMdd");
			
			// e.printStackTrace();
		}


		return ts;
	}
	
	public List<Date> getDaysBetweenDates(Date startdate, Date enddate)
	{
	    List<Date> dates = new ArrayList<Date>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(startdate);
	
	    while (calendar.getTime().before(enddate))
	    {
	        Date result = calendar.getTime();
	        dates.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}
	
	
	public List<String> getDaysBetweenDates(String startDate, String endDate, String pattern)
	{
	    List<String> dates = new ArrayList<String>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(this.convertStringToDate(startDate, pattern));
	    DateFormat df = new SimpleDateFormat(pattern);
	
	    while (calendar.getTime().before(this.convertStringToDate(endDate, pattern)))
	    {
	        Date result = calendar.getTime();
	        dates.add(df.format(result));
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}
	


	public Date convertTimestampToDate(Timestamp ts) {
		Date date = new Date(ts.getTime());
		return date; 
	}
	
	public Long convertTimestampToLong(Timestamp ts) {
		return ts.getTime(); 
	}
	
	public Timestamp convertLongToTimestamp(long timestamp) {
		return new Timestamp(timestamp); 
	}
		
	
	public Date convertStringToDate(String dateA, String pattern) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date1 = null;
		try {
			date1 = sdf.parse(dateA);
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date1;
		
	}


	public long calculateDifferenceInDays(String lastDate, String pattern) {
		String dateStart = lastDate;
		String dateStop = getTimeStampWithPattern(pattern);
 
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat(pattern);
 
		Date d1 = null;
		Date d2 = null;
 
		long diffDays = 0;
		
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);
 
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
 
			diffDays = diff / (24 * 60 * 60 * 1000);
 
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return diffDays;
	}
	
	/**
	 * @param lastDate
	 * @param pattern
	 * @return
	 */
	public long calculateDifferenceInDays(String firstDate, String secondDate, String pattern) {
		String dateStart = firstDate;
		String dateStop = secondDate;
 
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat(pattern);
 
		Date d1 = null;
		Date d2 = null;
 
		long diffDays = 0;
		
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);
 
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
 
			diffDays = diff / (24 * 60 * 60 * 1000);
 
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return diffDays;
	}
	
	public long calculateDifferenceInDays(Date lastDate) {
 
		Date d1 = lastDate;
		Date d2 = getDate();
 
		long diffDays = 0;
		
		try {
			
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			diffDays = diff / (24 * 60 * 60 * 1000);
 

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return diffDays;
	}
	
	
	public long calculateDifferenceInMinutes(Timestamp lastDate) {
		 
		Date d1 = lastDate;
		Date d2 = getDate();
 
		long diffMinutes = 0;
		
		if (lastDate == null) {
			// set it to a max value if empty
			diffMinutes = 9999999;
		} else {
			try {
				long diff = d2.getTime() - d1.getTime();
				diffMinutes = diff / (60 * 1000) % 60;
	 
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		
		return diffMinutes;
	}
	
	public long calculateDifferenceInSeconds(Timestamp lastDate) {
		 
		Date d1 = lastDate;
		Date d2 = getDate();
 
		long diffSeconds = 0;
		
		if (lastDate == null) {
			// set it to a max value if empty
			diffSeconds = 9999999;
		} else {
			try {
				
				//in milliseconds
				long diff = d2.getTime() - d1.getTime();
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				
	 
				diffSeconds = (diff / 1000 % 60) + (diffMinutes * 60) + (diffHours * 60 * 60);
	 
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		
		return diffSeconds;
	}
	
	public long calculateDifferenceInDays(Timestamp lastDate) {
		 
		Date d1 = lastDate;
		Date d2 = getDate();
 
		long diffDays = 0;
		
		if (lastDate == null) {
			diffDays = 360;
		} else {
			try {
				
				//in milliseconds
				long diff = d2.getTime() - d1.getTime();
				diffDays = diff / (24 * 60 * 60 * 1000);
	 
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		
		return diffDays;
	}
	
	
	/**
	 * @param dateA
	 * @param dateB
	 * @param pattern
	 * @return 	0 if both values are the same
	 * 			+1 if dateA is after dateB
	 * 			-1 if dateA is before dateB
	 */
	public int compareDates(String dateA, String dateB, String pattern) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	
    	int ret = 0;
		Date date1;
    	Date date2;
		try {
			date1 = sdf.parse(dateA);
			date2 = sdf.parse(dateB);
			
			ret = date1.compareTo(date2);
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    	
	}
	
	/**
	 * @param dateA
	 * @param dateB
	 * @return 	0 if both values are the same
	 * 			+1 if dateA is after dateB
	 * 			-1 if dateA is before dateB
	 */
	public int compareDates(Date dateA, Date dateB) {
    	return  dateA.compareTo(dateB);
	}
	
	

}
