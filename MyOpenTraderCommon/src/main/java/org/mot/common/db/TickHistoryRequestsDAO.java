

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TickHistoryRequestsDAO {

	private static final Logger logger = LoggerFactory.getLogger(TickHistoryRequestsDAO.class);
	DateBuilder db = new DateBuilder();
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	
	/**
	 * Writes a new History Request to the table...
	 * 
	 * @param date - current timestamp
	 * @param requestID - Internal request ID
	 * @param value - Request Value
	 */
	public void addNewTickHistoryRequest(Timestamp date, String requestID, String stock, String value) {
		Statement s;
		Connection connection = dcf.getConnection();
		try {
			s = connection.createStatement ();
			logger.debug("INSERT INTO tickhistoryrequests VALUES((?),'" + requestID + "','" + stock + "','" + value + "')");

			PreparedStatement ps = connection.prepareStatement("INSERT INTO tickhistoryrequests VALUES((?),'" + requestID + "','" + stock + "','" + value + "')");
			ps.setTimestamp(1, db.getTimestampFromDate());
			ps.executeUpdate();
			
    			    	//int count = s.executeUpdate("INSERT INTO tickprices VALUES('" + db.getDate() + "', '" + stock + "','" + field + "','" + price +"')");
    	
	    	s.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}
	}
	
	
	public void updateLastTimestamp(String stock, String frequency) {
		Statement s;

		Connection connection = dcf.getConnection();
		try {
			s = connection.createStatement ();
			logger.debug("UPDATE tickhistoryrequests SET TIMESTAMP = (?) where STOCK = '" + stock + "' and FREQUENCY ='" + frequency + "'");

			PreparedStatement ps = connection.prepareStatement("UPDATE tickhistoryrequests SET TIMESTAMP = (?) where STOCK = '" + stock + "' and FREQUENCY ='" + frequency + "'");;
			ps.setTimestamp(1, db.getTimestampFromDate());
			ps.executeUpdate();
			
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}
	}
	
	
	/**
	 * Use this methods to look up history request values
	 * 
	 * @param requestID - The request ID to look for
	 * @return the String value of the history request
	 */
	public String getTickHistoryRequestValues(String requestID) {
		String res = null;
		Statement s;

    	Connection connection = dcf.getConnection();
        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistoryrequests` where REQID = '" + requestID + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistoryrequests` where REQID = '" + requestID + "'");

	    	while (rs.next()){
	    		res = rs.getString("FREQUENCY");
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	
		
        return res;
	}
	
	/**
	 * Use this methods to look up history request values
	 * 
	 * @param requestID - The request ID to look for
	 * @return the String value of the history request
	 */
	public String getTickHistoryRequestStock(String requestID) {
		String res = null;
		Statement s;

    	Connection connection = dcf.getConnection();
        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistoryrequests` where REQID = '" + requestID + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistoryrequests` where REQID = '" + requestID + "'");

	    	while (rs.next()){
	    		res = rs.getString("STOCK");
	    	}
	    		
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		} 	
		
        return res;
	}
	
	/**
	 * Use this method to look up the request ID, belonging to a particular stock
	 * 
	 * @param stock - stock/instrument to look for
	 * @param frequency - frequency declaration (1min, ticks, daily)
	 * @return - the request ID to use
	 */
	public String getTickHistoryStockRequest(String stock, String frequency) {
		String res = null;
		Statement s;

    	Connection connection = dcf.getConnection();
        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistoryrequests` where STOCK = '" + stock + "' and FREQUENCY = '" + frequency + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistoryrequests` where STOCK = '" + stock + "' and FREQUENCY = '" + frequency + "'");

	    	while(rs.next()) {
	    		res = rs.getString("REQID");
	    	}
	   
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	
		
        return res;
	}
	
	
	public Timestamp getLastTickHistoryUpdate(String stock, String frequency) {
		Timestamp res = null;
		Statement s;
    	Connection connection = dcf.getConnection();
		
        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickhistoryrequests` where STOCK = '" + stock + "' and FREQUENCY = '" + frequency + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `tickhistoryrequests` where STOCK = '" + stock + "' and FREQUENCY = '" + frequency + "'");

	    	while(rs.next()) {
	    		res = rs.getTimestamp("TIMESTAMP");
	    	}
	   
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	
		
        return res;
	}


	
}
