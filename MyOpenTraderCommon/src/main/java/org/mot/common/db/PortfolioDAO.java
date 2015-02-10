

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
import org.mot.common.objects.Portfolio;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PortfolioDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7416097641739382791L;
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(PortfolioDAO.class);
	static DateBuilder db = new DateBuilder(); 
	static TickConverter tc = new TickConverter();



	public synchronized boolean addNewPortfolio(Portfolio pf) {

		

		if (!doesPortfolioExist(pf.getSymbol())) {

			Connection connection = dcf.getConnection();
			Statement s;
			try {
				s = connection.createStatement ();

				/*
			INSERT INTO mot.portfolio(
					   `TIMESTAMP`
					  ,SYMBOL
					  ,TYPE
					  ,POSITION
					  ,PRICE
					  ,AVERAGEPRICE
					  ,MARKETVALUE
					  ,UPNL
					  ,PNL
					) VALUES (
					   NULL -- TIMESTAMP - IN timestamp
					  ,NULL -- SYMBOL - IN varchar(30)
					  ,NULL -- TYPE - IN varchar(30)
					  ,NULL -- POSITION - IN int(11)
					  ,NULL -- PRICE - IN double
					  ,0   -- AVERAGEPRICE - IN double
					  ,0   -- MARKETVALUE - IN double
					  ,0   -- UPNL - IN double
					  ,0   -- PNL - IN double
					)*/

				logger.debug("INSERT INTO portfolio VALUES('" + db.getTimestampFromDate() + "', '" 
						+ pf.getSymbol() + "','" 
						+ pf.getType() + "','" 
						+ pf.getPosition()+ "','"
						+ pf.getPrice()+ "','"
						+ pf.getAvgPrice()+ "','"
						+ pf.getMarketValue()+ "','"
						+ pf.getPNL()+ "','"
						+ pf.getUPNL()  
						+"')");

				PreparedStatement ps = connection.prepareStatement("INSERT INTO portfolio VALUES((?), '" 
						+ pf.getSymbol() + "','" 
						+ pf.getType() + "','" 
						+ pf.getPosition()+ "','"
						+ pf.getPrice()+ "','"
						+ pf.getAvgPrice()+ "','"
						+ pf.getMarketValue()+ "','"
						+ pf.getPNL()+ "','"
						+ pf.getUPNL()  
						+"')");
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
		} else {
			return updatePortfolio(pf);
		}

	}


	public synchronized boolean updatePortfolio(Portfolio pf) {

		Connection connection = dcf.getConnection();

		Statement s;
		try {
			s = connection.createStatement ();

			logger.debug(
					"UPDATE mot.portfolio SET `TIMESTAMP` = '" + db.getTimestampFromDate() + "'" 
					 + ",SYMBOL = '" + pf.getSymbol() + "'" 
					 + ",TYPE = '"+ pf.getType() + "'" 
					 + ",POSITION = '"+ pf.getPosition()+ "'"
					 + ",PRICE = '" + pf.getPrice()+ "'"
					 + ",AVERAGEPRICE = '"+ pf.getAvgPrice()+ "'"
					 + ",MARKETVALUE = '"+ pf.getMarketValue()+ "'"
					 + ",UPNL = '" + pf.getPNL()+ "'"
					 + ",PNL = '"+ pf.getUPNL() + "'"
					 + " where SYMBOL = '" + pf.getSymbol() + "'");

			PreparedStatement ps = connection.prepareStatement(
					"UPDATE mot.portfolio SET `TIMESTAMP` = (?)" 
							 + ",SYMBOL = '" + pf.getSymbol() + "'" 
							 + ",TYPE = '"+ pf.getType() + "'" 
							 + ",POSITION = '"+ pf.getPosition()+ "'"
							 + ",PRICE = '" + pf.getPrice()+ "'"
							 + ",AVERAGEPRICE = '"+ pf.getAvgPrice()+ "'"
							 + ",MARKETVALUE = '"+ pf.getMarketValue()+ "'"
							 + ",UPNL = '" + pf.getPNL()+ "'"
							 + ",PNL = '"+ pf.getUPNL() + "'"
							 + " where SYMBOL = '" + pf.getSymbol() + "'");
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


	public boolean doesPortfolioExist(String symbol) {

		Connection connection = dcf.getConnection();

		Statement s;
		boolean exists = false;


		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `portfolio` where SYMBOL = '" + symbol + "' order by TIMESTAMP desc");
			ResultSet rs = s.executeQuery("SELECT * FROM `portfolio` where SYMBOL = '" + symbol + "' order by TIMESTAMP desc");

			while(rs.next()) {
				exists = true;				
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
		return exists;
	}


	public Portfolio[] getPortfolioAsObject() {

		Connection connection = dcf.getConnection();
		ArrayList<Portfolio> list = new ArrayList<Portfolio>();

		Statement s;


		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `portfolio` order by TIMESTAMP desc");
			ResultSet rs = s.executeQuery("SELECT * FROM `portfolio` order by TIMESTAMP desc");

			while(rs.next()) {
				Portfolio entry = mapEntryToPortfolio(rs);

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

		Portfolio[] ret = new Portfolio[list.size()];

		// convert to an array
		return list.toArray(ret);
	}



	public Portfolio getPortfolioBySymbolAsObject(String symbol) {
	
		Connection connection = dcf.getConnection();
	
		Statement s;
	
		Portfolio entry = new Portfolio();
	
		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `portfolio` where SYMBOL ='" + symbol + "' order by TIMESTAMP desc limit 1");
			ResultSet rs = s.executeQuery("SELECT * FROM `portfolio` where SYMBOL ='" + symbol + "' order by TIMESTAMP desc limit 1");
	
			while(rs.next()) {
				entry = mapEntryToPortfolio(rs);
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
		return entry;
	}

	
	

	private Portfolio mapEntryToPortfolio(ResultSet rs) throws SQLException {
		/*
		INSERT INTO mot.portfolio(
				   `TIMESTAMP`
				  ,SYMBOL
				  ,TYPE
				  ,POSITION
				  ,PRICE
				  ,AVERAGEPRICE
				  ,MARKETVALUE
				  ,UPNL
				  ,PNL
				) VALUES (
				   NULL -- TIMESTAMP - IN timestamp
				  ,NULL -- SYMBOL - IN varchar(30)
				  ,NULL -- TYPE - IN varchar(30)
				  ,NULL -- POSITION - IN int(11)
				  ,NULL -- PRICE - IN double
				  ,0   -- AVERAGEPRICE - IN double
				  ,0   -- MARKETVALUE - IN double
				  ,0   -- UPNL - IN double
				  ,0   -- PNL - IN double
				)*/



		Portfolio entry = new Portfolio();
		entry.setTimestamp(db.convertTimestampToLong(rs.getTimestamp("TIMESTAMP")));
		entry.setSymbol(rs.getString("SYMBOL"));
		entry.setType(rs.getString("TYPE"));
		entry.setPosition(rs.getInt("POSITION"));
		entry.setPrice(rs.getDouble("PRICE"));
		entry.setAvgPrice(rs.getDouble("AVERAGEPRICE"));
		entry.setMarketValue(rs.getDouble("MARKETVALUE"));
		entry.setUPNL(rs.getDouble("UPNL"));
		entry.setPNL(rs.getDouble("PNL"));

		return entry;
	}



}
