

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

import org.mot.common.conversion.TickConverter;
import org.mot.common.objects.Strategy;
import org.mot.common.objects.Strategy.Status;
import org.mot.common.objects.StrategyAnalysis;
import org.mot.common.util.DateBuilder;
import org.mot.common.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StrategyAnalysisDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7416097641739382791L;
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(StrategyAnalysisDAO.class);
	static DateBuilder db = new DateBuilder(); 
	
	

	/**
	 * @param strategy - strategy to add to the database
	 * @return - true or false 
	 */
	public void addNewStrategyAnalysis(StrategyAnalysis sa) {

		Connection connection = dcf.getConnection();

		Statement s;
		try {
			
			s = connection.createStatement ();

			logger.debug("INSERT INTO strategyanalysis VALUES('" + db.getTimestampFromDate() + "', '" + sa.getName() + "','" + sa.getPnL() + "','" + sa.getTradeCount() + "','" + sa.getQuantity() + "')");

			PreparedStatement ps = connection.prepareStatement("INSERT INTO strategyanalysis VALUES((?), '" + sa.getName() + "','" + sa.getPnL() + "','" + sa.getTradeCount() + "','" + sa.getQuantity() + "')");
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
	 * @param sa
	 */
	public void addNewStrategyAnalysis(StrategyAnalysis[] sa) {

		Connection connection = dcf.getConnection();

		Statement s;
		try {
			
			for (int p = 0; p < sa.length; p++) {
				s = connection.createStatement ();
	
				logger.debug("INSERT INTO strategyanalysis VALUES('" + db.getTimestampFromDate() + "', '" + sa[p].getName() + "','" + sa[p].getPnL() + "','" + sa[p].getTradeCount() + "','" + sa[p].getQuantity() + "')");
	
				PreparedStatement ps = connection.prepareStatement("INSERT INTO strategyanalysis VALUES((?), '" + sa[p].getName() + "','" + sa[p].getPnL() + "','" + sa[p].getTradeCount() + "','" + sa[p].getQuantity() + "')");
				ps.setTimestamp(1, db.getTimestampFromDate());
				ps.executeUpdate();
	
				s.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}

	}
	


	/**
	 * @return a list of all strategies currently enabled
	 */
	public Strategy[] getAllStrategiesAsObject() {

		Connection connection = dcf.getConnection();

		ArrayList<Strategy> list = new ArrayList<Strategy>();
		Statement s;
		

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `strategies` where ENABLED = true");
			ResultSet rs = s.executeQuery("SELECT * FROM `strategies` where ENABLED = true");

			while(rs.next()) {
				Strategy entry = new Strategy();
				entry.setStatus(Status.ENABLED);
				entry.setID(rs.getString("ID"));
				entry.setLoadValues(rs.getString("LOADVALUES"));
				entry.setName(rs.getString("NAME"));
				entry.setTimestamp(db.convertTimestampToLong(rs.getTimestamp("TIMESTAMP")));
				entry.setType(rs.getString("TYPE"));
				entry.setSymbol(rs.getString("SYMBOL"));
				
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
		
		Strategy[] ret = new Strategy[list.size()];

		// convert to an array
		return list.toArray(ret);
	}
	
	
	/**
	 * @param symbol - symbol to look for strategies
	 * @return - list of strategies, associated with particular symbol
	 */
	public Strategy[] getStrategiesForSymbolAsObject(String symbol) {

		Connection connection = dcf.getConnection();

		ArrayList<Strategy> list = new ArrayList<Strategy>();
		Statement s;
		

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `strategies` where ENABLED = true and SYMBOL ='" + symbol + "'");
			ResultSet rs = s.executeQuery("SELECT * FROM `strategies` where ENABLED = true and SYMBOL ='" + symbol + "'");

			while(rs.next()) {
				Strategy entry = new Strategy();
				entry.setStatus(Status.ENABLED);
				entry.setID(rs.getString("ID"));
				entry.setLoadValues(rs.getString("LOADVALUES"));
				entry.setName(rs.getString("NAME"));
				entry.setTimestamp(db.convertTimestampToLong(rs.getTimestamp("TIMESTAMP")));
				entry.setType(rs.getString("TYPE"));
				entry.setSymbol(rs.getString("SYMBOL"));
				
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
		
		Strategy[] ret = new Strategy[list.size()];

		// convert to an array
		return list.toArray(ret);
	}



}
