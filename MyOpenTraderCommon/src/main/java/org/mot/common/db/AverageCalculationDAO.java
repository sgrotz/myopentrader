

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
import java.util.Hashtable;

import org.mot.common.conversion.TickConverter;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AverageCalculationDAO {

	private static final Logger logger = LoggerFactory.getLogger(AverageCalculationDAO.class);

	DateBuilder db = new DateBuilder();
	TickConverter tc = new TickConverter(); 
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();

	public boolean addCalculationData(String stock, int value1, int value2, String range, int quantity, int tradeCount, double result, double txnCost, double pnl, String className, String startDate, String endDate) {

		Connection connection = dcf.getConnection();
		Statement s;
		try {

			s = connection.createStatement ();
			Timestamp ts = db.getTimestampFromDate();
			
			String query = "INSERT INTO avgcalc VALUES('" + stock + "','" + value1 + "','" + value2 + "','" + range + "','" + quantity + "','" + tradeCount + "','" + result +"','" + txnCost + "','" + pnl  + "','"  + className +"','" + ts  +"','" + startDate +"','" + endDate + "')";

			logger.debug(query);

			PreparedStatement ps = connection.prepareStatement(query);
			ps.executeUpdate();

			s.close();
			dcf.disconnect(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return false;
	}

	public boolean updateCalculationData(String stock, int value1, int value2, String range, double result) {

		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();

			Timestamp ts = db.getTimestampFromDate();

			logger.debug("UPDATE avgcalc set TIMERANGE='" + range + "', RESULT='" + result + "', TIMESTAMP='" + ts +"' where STOCK='" + stock + "' and VALUE1='" + value1 + "' and VALUE2='" + value2+ "'");

			PreparedStatement ps = connection.prepareStatement("UPDATE avgcalc set TIMERANGE='" + range + "', RESULT='" + result + "', TIMESTAMP='" + ts +"' where STOCK='" + stock + "' and VALUE1='" + value1 + "' and VALUE2='" + value2+ "'");
			ps.executeUpdate();

			s.close();
			dcf.disconnect(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return false;
	}

	public void updateOrInsertCalculationData(String stock, int value1, int value2, String range, int quantity, int tradeCount, double result, double txnCost, double pnl, boolean ignoreUpdates, String className, String startDate, String endDate) {

		/*		if (doesCalculationDataExist(stock, value1, value2, range)) {
			if (!ignoreUpdates) {
				updateCalculationData(stock, value1, value2, range, result);
			}
		} else {*/
		addCalculationData(stock, value1, value2, range, quantity, tradeCount, result, txnCost, pnl, className, startDate, endDate);
		//}

	}

	public boolean doesCalculationDataExist(String stock, int value1, int value2, String range) {
		Statement s;
		boolean found = false;
		Connection connection = dcf.getConnection();

		try {
			s = connection.createStatement ();

			logger.debug("SELECT * FROM `avgcalc` where STOCK='" + stock + "' and VALUE1='" + value1 + "' and VALUE2='" + value2+ "' and TIMERANGE='" + range + "'");
			ResultSet rs = s.executeQuery("SELECT * FROM `avgcalc` where STOCK='" + stock + "' and VALUE1='" + value1 + "' and VALUE2='" + value2+ "' and TIMERANGE='" + range + "'");

			if (rs.next()) {
				found = true;
			}
			s.close();
			dcf.disconnect(connection);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return found;
	}

	public boolean doesCalculationDataExist(String stock, String frequency) {
		Statement s;
		boolean found = false;
		Connection connection = dcf.getConnection();

		try {
			s = connection.createStatement ();

			String query = "SELECT * FROM `avgcalc` where STOCK='" + stock + "' and TIMERANGE='" + frequency + "'";

			logger.debug(query);
			ResultSet rs = s.executeQuery(query);

			if (rs.next()) {
				found = true;
			}
			s.close();
			dcf.disconnect(connection);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return found;
	}



	public Hashtable<Integer, Integer>  getBestAverageCombination(String stock, String range, int top){

		Hashtable<Integer, Integer> res = new Hashtable<Integer, Integer>();
		Statement s;
		Connection connection = dcf.getConnection();

		try {

			s = connection.createStatement ();

			//SELECT * FROM `avgcalc` WHERE `STOCK` LIKE 'AAPL' AND `TIMERANGE` LIKE 'TICK' ORDER BY `avgcalc`.`RESULT` DESC
			logger.debug("SELECT * FROM `avgcalc` where STOCK='" + stock + "' and TIMERANGE='" + range + "' order by RESULT DESC limit " + top);
			ResultSet rs = s.executeQuery("SELECT * FROM `avgcalc` where STOCK='" + stock + "' and TIMERANGE='" + range + "' order by RESULT DESC limit " + top);

			while (rs.next()) {
				res.put(rs.getInt("VALUE1"), rs.getInt("VALUE2"));
			}
			s.close();
			dcf.disconnect(connection);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	finally {
			dcf.disconnect(connection);
		}

		return res;

	}

}
