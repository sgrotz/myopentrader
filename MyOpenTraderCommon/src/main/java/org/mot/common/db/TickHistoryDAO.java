

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
  
  
  package org.mot.common.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.mot.common.objects.TickHistory;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TickHistoryDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6066053686610107909L;
	private static final Logger logger = LoggerFactory.getLogger(TickHistoryDAO.class);
	private static DateBuilder db = new DateBuilder();
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	
	
	/**
	 * @param stock - Add a new stock history into the history table
	 * @param frequency - Set the frequency (1D, 1Y ...)
	 * @param date - Date as String
	 * @param open - Stocks Open value
	 * @param high - Stocks High value
	 * @param low - Stocks Low value
	 * @param close - Stocks Close value
	 * @param volume - Stocks Volume
	 * @param count - Stocks Count
	 * @return - always false ... need to fix that!!
	 */
	public boolean addNewTickHistory(String stock, String frequency, String date, double open, double high, double low, double close, int volume, int count) {
		
		Statement s;
		Connection connection = dcf.getConnection();
		try {
			
			if (checkIfTickExists(stock + date)) {
				logger.debug("Tick already exists - ignore ..." );				
			} else {
				
				s = connection.createStatement ();
				
	    		logger.debug("INSERT INTO tickhistory VALUES('" + stock + date +"', '"+ frequency 
						+"', '"+ stock + "',(?),'" + open + "','" + high + "','" + low + "','" + close + "','" 
						+ volume + "','" + count +"')");
	    		
				PreparedStatement ps = connection.prepareStatement("INSERT INTO tickhistory VALUES('" + stock + date +"', '"+ frequency 
						+"', '"+ stock + "',(?),'" + open + "','" + high + "','" + low + "','" + close + "','" 
						+ volume + "','" + count +"')");
				ps.setTimestamp(1, db.getTimestampFromDate(date, "yyyyMMdd  HH:mm:dd"));
				ps.executeUpdate();
				

		    	//int count = s.executeUpdate("INSERT INTO tickprices VALUES('" + db.getDate() + "', '" + stock + "','" + field + "','" + price +"')");
	    	
		    	s.close();
			}
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	finally {
			dcf.disconnect(connection);
		}

		return false; 
	}
	
	public boolean addNewTickHistory(String stock, String frequency, TickHistory tick) {
		
		Statement s;
		Connection connection = dcf.getConnection();
		try {

			
			if (checkIfTickExists(stock + tick.getDate())) {
				logger.debug("Tick already exists - ignore ..." );				
			} else {
				s = connection.createStatement ();
	    		logger.debug("INSERT INTO tickhistory VALUES('" + stock + tick.getDate() +"', '"+ frequency 
						+"', '"+ stock + "',(?),'" + tick.getOpen()+ "','" + tick.getHigh() + "','" + tick.getLow() + "','" + tick.getClose()+ "','" 
						+ tick.getVolume() + "','" + tick.getCount()+"')");
	    		
				PreparedStatement ps = connection.prepareStatement("INSERT INTO tickhistory VALUES('" + stock + tick.getDate() +"', '"+ frequency 
						+"', '"+ stock + "',(?),'" + tick.getOpen()+ "','" + tick.getHigh() + "','" + tick.getLow() + "','" + tick.getClose()+ "','" 
						+ tick.getVolume() + "','" + tick.getCount()+"')");
				ps.setTimestamp(1, db.getTimestampFromDate(tick.getDate(), "yyyyMMdd  HH:mm:dd"));
				ps.executeUpdate();
				

		    	//int count = s.executeUpdate("INSERT INTO tickprices VALUES('" + db.getDate() + "', '" + stock + "','" + field + "','" + price +"')");
	    	
		    	s.close();
			}
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	finally {
			dcf.disconnect(connection);
		}

		return false; 
	}
	
	
	
	/**
	 * @param ID - ID to search for
	 * @return true if value already exists - or false if not found
	 */
	private boolean checkIfTickExists (String ID) {
		// Returns true if object already exists

		Statement s;
		boolean found = false;
		Connection connection = dcf.getConnection();
		
        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistory` where ID = '" + ID + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistory` where ID = '" + ID + "'");
	    	
	    	if (rs.next()) {
	    		found = true;
	    	}
	    	
	    	s.close();
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	finally {
			dcf.disconnect(connection);
		}	
		
		return found;
	}
	
	/**
	 * @param stock - which stock to search for
	 * @return a list of OPEN prices for a particular stock
	 */
	public ArrayList<Double> getHistoricPriceForStock(String stock) {
		
		ArrayList<Double> result = new ArrayList<Double>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistory` where STOCK = '" + stock + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistory` where STOCK = '" + stock + "'");

	    	while(rs.next()) {
	    		result.add(rs.getDouble("OPEN"));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	finally {
			dcf.disconnect(connection);
		}	
		
        return result;
	}
	
	
	
	public TickHistory[] getHistoricPriceTableForStock(String stock, String frequency, int maxCount) {
	
		long startTime = System.currentTimeMillis();
		
		ArrayList<TickHistory> list = new ArrayList<TickHistory>();
		Statement s;
		Connection connection = dcf.getConnection();

        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' and FREQUENCY = '" + frequency + "' order by DATE asc limit " + maxCount);
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' and FREQUENCY = '" + frequency + "' order by DATE asc limit " + maxCount);
	    	
	     	while(rs.next()) {
	    		TickHistory entry = new TickHistory();
	    		
	    		entry.setSymbol(rs.getString("STOCK"));
	    		entry.setHigh(rs.getDouble("HIGH"));
	    		entry.setOpen(rs.getDouble("OPEN"));
	    		entry.setClose(rs.getInt("CLOSE"));
	    		entry.setDateAsLong(db.convertTimestampToLong(rs.getTimestamp("DATE")));
	    		list.add(entry);
	     	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	finally {
			dcf.disconnect(connection);
		} 	
		
        TickHistory[] result = new TickHistory[list.size()];
        result = list.toArray(result);
        
		long endTime = System.currentTimeMillis();
		logger.trace("Service took: " + (endTime - startTime) + " msecs");
        
        return result;
	}
	

	/**
	 * @param stock - Stock to query the historic prices for
	 * @param frequency - what frequency range to use (1 day - 1 min)
	 * @return list of historic prices
	 */
	public ArrayList<Double> getHistoricPriceForStock(String stock, String frequency, int count) {
		
		ArrayList<Double> result = new ArrayList<Double>();
		Statement s;
		Connection connection = dcf.getConnection();

        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' and FREQUENCY = '"+ frequency + "' order by DATE desc limit " + count);
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' and FREQUENCY = '"+ frequency + "' order by DATE desc limit " + count);

	    	while(rs.next()) {
	    		result.add(rs.getDouble("OPEN"));
	    	}
	    	
	    	s.close();  	

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	finally {
			dcf.disconnect(connection);
		}	
		
        return result;
	}
	
	
	/**
	 * @param stock - which stock to search for
	 * @return the last OPEN prices for a particular stock
	 */
	public Double getLastHistoricPriceForStock(String stock) {
		
		Double result = null;
		Statement s;
		Connection connection = dcf.getConnection();

        try {
        	
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' order by `DATE` desc limit 1");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' order by `DATE` desc limit 1");

	    	while(rs.next()) {
	    		result = rs.getDouble("OPEN");
	    	}
	    		
	    	s.close();	    	
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	finally {
			dcf.disconnect(connection);
		}
		
        return result;
	}
	
	
	public int getLastClosingPriceForStock(String stock) {
		
		int result = 0;
		Statement s;
		
		//Date yest = db.getYesterdaysDate();
		Connection connection = dcf.getConnection();

        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' order by `DATE` desc limit 1");
	    	//ResultSet rs = s.executeQuery("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' order by `DATE` desc limit 1");

			PreparedStatement ps = connection.prepareStatement("SELECT * FROM `tickhistory` where STOCK = '" + stock + "' order by `DATE` desc limit 1");
			//ps.setTimestamp(1, db.deriveTimestampFromDate(yest));
			ResultSet rs = ps.executeQuery();
	    	
	    	while(rs.next()) {
	    		result = rs.getInt("CLOSE");
	    	}
	    		
	    	s.close();	    	
	    	
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
        return result;
	}
	
}
