

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
import org.mot.common.util.DateBuilder;
import org.mot.common.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StrategyDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7416097641739382791L;
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(StrategyDAO.class);
	static DateBuilder db = new DateBuilder(); 
	static TickConverter tc = new TickConverter();
	
	

	/**
	 * @param strategy - strategy to add to the database
	 * @return - true or false 
	 */
	public synchronized boolean addNewStrategy(Strategy strategy) {

		Connection connection = dcf.getConnection();

		Statement s;
		try {
			IDGenerator id = new IDGenerator(); 
			
			s = connection.createStatement ();

			logger.debug("INSERT INTO strategies VALUES('" + id.getUniqueID() + "', '" + strategy.getName() + "','" + strategy.getType() + "','" + strategy.getSymbol() + "','" + strategy.getLoadValues() + "','" + strategy.isEnabled() + "','" + strategy.getAmount() + "','" + strategy.isSimulated() + "','" + db.getTimestampFromDate()+"')");

			PreparedStatement ps = connection.prepareStatement("INSERT INTO strategies VALUES('" + id.getUniqueID() + "', '" + strategy.getName() + "','" + strategy.getType() + "','" + strategy.getSymbol() + "','" + strategy.getLoadValues() + "','" + strategy.isEnabled() + "',(?))");
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

		return true;
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
				Strategy entry = mapStrategyToEntry(rs);
				
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


	


	private Strategy mapStrategyToEntry(ResultSet rs) throws SQLException {
		Strategy entry = new Strategy();
		entry.setEnabled(true);
		entry.setID(rs.getString("ID"));
		entry.setLoadValues(rs.getString("LOADVALUES"));
		entry.setName(rs.getString("NAME"));
		entry.setTimestamp(db.convertTimestampToLong(rs.getTimestamp("TIMESTAMP")));
		entry.setType(rs.getString("TYPE"));
		entry.setSymbol(rs.getString("SYMBOL"));
		entry.setAmount(rs.getDouble("AMOUNT"));
		entry.setSimulated(rs.getBoolean("SIMULATED"));
		return entry;
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
				Strategy entry = mapStrategyToEntry(rs);
				
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

	
	public String getStrategyClassNameByStrategy(String strategy) {

		Connection connection = dcf.getConnection();

		Statement s;
		String ret = null;

		try {
			s = connection.createStatement ();
			String query = "SELECT TYPE FROM strategies where NAME ='" + strategy + "'";
			logger.debug(query);
			ResultSet rs = s.executeQuery(query);

			while(rs.next()) {
				ret = rs.getString("TYPE");
				break;
			}
			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	

		// convert to an array
		return ret;
	}


}
