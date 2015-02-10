

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
import java.util.Date;

import org.mot.common.objects.Exchange;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.DateBuilder;
import org.mot.common.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExchangeDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(ExchangeDAO.class);
	static DateBuilder db = new DateBuilder(); 
	
	

	/**
	 * @param exchange to add to the database
	 * @return - true or false 
	 */
	public synchronized boolean addNewExchange(Exchange exchange) {

		Connection connection = dcf.getConnection();

		Statement s;
		try {
			IDGenerator id = new IDGenerator(); 
			
			s = connection.createStatement ();

			logger.debug("INSERT INTO exchange VALUES('" + id.getUniqueID() + "', '" + exchange.getExchange() + "','" + exchange.getName() + "','" + exchange.getOpen() + "','" + exchange.getClose() + "','" + exchange.getTimezone() +"')");

			PreparedStatement ps = connection.prepareStatement("INSERT INTO exchange VALUES('" + id.getUniqueID() + "', '" + exchange.getExchange() + "','" + exchange.getName() + "','" + exchange.getOpen() + "','" + exchange.getClose() + "','" + exchange.getTimezone() +"')");
			ps.executeUpdate();

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}

		return true;
	}



	/**
	 * @return a list of all exchanges
	 */
	public Exchange[] getAllExchangesAsObject() {

		Connection connection = dcf.getConnection();

		ArrayList<Exchange> list = new ArrayList<Exchange>();
		Statement s;
		

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `exchange` where ID like '%'");
			ResultSet rs = s.executeQuery("SELECT * FROM `exchange` where ID like '%'");

			while(rs.next()) {
				Exchange entry = mapExchange(rs);
				
				list.add(entry);
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	
		
		Exchange[] ret = new Exchange[list.size()];

		// convert to an array
		return list.toArray(ret);
	}


	private String getExchangeOpeningTime(String exchange) {

		Connection connection = dcf.getConnection();

		ArrayList<Exchange> list = new ArrayList<Exchange>();
		Statement s;
		

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `exchange` where EXCHANGE = '" + exchange + "'");
			ResultSet rs = s.executeQuery("SELECT * FROM `exchange` where EXCHANGE = '" + exchange + "'");

			while(rs.next()) {
				Exchange entry = mapExchange(rs);
				
				list.add(entry);
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	
		
		Exchange[] ret = new Exchange[list.size()];
		// convert to an array
		ret = list.toArray(ret);

		return ret[0].getOpen();
	}
	
	private String getExchangeTimezone(String exchange) {

		Connection connection = dcf.getConnection();

		ArrayList<Exchange> list = new ArrayList<Exchange>();
		Statement s;
		

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `exchange` where EXCHANGE = '" + exchange + "'");
			ResultSet rs = s.executeQuery("SELECT * FROM `exchange` where EXCHANGE = '" + exchange + "'");

			while(rs.next()) {
				Exchange entry = mapExchange(rs);
				
				list.add(entry);
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	
		
		Exchange[] ret = new Exchange[list.size()];
		// convert to an array
		ret = list.toArray(ret);
		
		return ret[0].getTimezone();
	}
	
	
	
	private String getExchangeClosingTime(String exchange) {

		Connection connection = dcf.getConnection();

		ArrayList<Exchange> list = new ArrayList<Exchange>();
		Statement s;
		

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `exchange` where EXCHANGE = '" + exchange + "'");
			ResultSet rs = s.executeQuery("SELECT * FROM `exchange` where EXCHANGE = '" + exchange + "'");

			while(rs.next()) {
				Exchange entry = mapExchange(rs);
				
				list.add(entry);
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	
		
		Exchange[] ret = new Exchange[list.size()];
		// convert to an array
		ret = list.toArray(ret);
		
		return ret[0].getClose();
	}
	
	
	
	public boolean isExchangOpen(String exchange) {
		
		Date openTime = db.convertStringToDate(this.getExchangeOpeningTime(exchange), "HH:mm");
		Date closeTime = db.convertStringToDate(this.getExchangeClosingTime(exchange), "HH:mm");
		Date currentTime = db.convertStringToDate(db.getTimeStampWithPattern("HH:mm", this.getExchangeTimezone(exchange)), "HH:mm");
		boolean ret = false;
		
		 /* @return 	0 if both values are the same
			 * 			+1 if dateA is after dateB
			 * 			-1 if dateA is before dateB
		 */
		
		// Check if past opening time
		int open = db.compareDates(currentTime, openTime);
		if (open > 0) {
			// If open is larger than 0 - it is past the open time
			int close = db.compareDates(currentTime, closeTime);
			
			if (close < 0) {
				// If clo
				ret = true;
			}
		}
		
		return ret;
	}
	

	private Exchange mapExchange(ResultSet rs) throws SQLException {
		Exchange entry = new Exchange();
		entry.setExchangeID(rs.getString("ID"));
		entry.setExchange(rs.getString("EXCHANGE"));
		entry.setName(rs.getString("NAME"));
		entry.setOpen(rs.getString("OPEN"));
		entry.setClose(rs.getString("CLOSE"));
		entry.setTimezone(rs.getString("TIMEZONE"));
		return entry;
	}
	


}
