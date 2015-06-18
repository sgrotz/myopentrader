
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import org.mot.common.conversion.TickConverter;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.Tick;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TickPriceDAO implements Serializable {

	private static final long serialVersionUID = 7416097641739382791L;
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(TickPriceDAO.class);
	static DateBuilder db = new DateBuilder(); 
	static TickConverter tc = new TickConverter();
	CalculatorFactory cf = new CalculatorFactory();
	

	/**
	 * Atomic method to add a new tick price to the database.
	 * 
	 * @param stock - stock name to add
	 * @param price - price field
	 * @param field - field name (ASK, BID, CLOSE, LAST ...)
	 * @param timestamp - current timestamp as long value
	 * @return
	 */
	public synchronized boolean addNewTickPrice(String stock, double price, String field, Timestamp timestamp) {

		Connection connection = dcf.getConnection();
		
		if (timestamp == null) {
			timestamp = db.getTimestampFromDate();
		}

		Statement s;
		try {
			s = connection.createStatement ();
			
			String query = "INSERT INTO tickprices VALUES('" + timestamp + "', '" + stock + "','" + field + "','" + price +"')";

			logger.debug(query);

			PreparedStatement ps = connection.prepareStatement(query);
			ps.executeUpdate();

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		return false;
	}
	
	

	/**
	 * Add a new tick price to the database.
	 * 
	 * @param stock - stock the tick belongs to
	 * @param tick - tick object
	 * @return
	 */
	public boolean addNewTickPrice(String stock, Tick tick) {
		Connection connection = dcf.getConnection();

		Statement s;
		try {
			s = connection.createStatement ();

			logger.debug("INSERT INTO tickprices VALUES((?), '" + stock + "','" + tick.getField() 
					+ "','" + tick.getPrice() +"')");

			PreparedStatement ps = connection.prepareStatement("INSERT INTO tickprices VALUES((?), '" + stock + "','" + tick.getField() 
					+ "','" + tick.getPrice() +"')");
			ps.setTimestamp(1, tick.getTimestamp());
			ps.executeUpdate();

			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return false;
	}

	
	/**
	 * This method will return a list of prices for a particular stock. 
	 * 
	 * @param stock - which stock to search for
	 * @param count - how many records to return
	 * @param field - field to search for
	 * @return - an arraylist with Double values
	 */
	public ArrayList<Double> getPriceForSymbol(String stock, int count, String field) {
		Connection connection = dcf.getConnection();

		ArrayList<Double> result = new ArrayList<Double>();
		Statement s;

		try {
			s = connection.createStatement ();
			String query = "SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' order by TIMESTAMP desc limit " + count;
			logger.debug(query);
			ResultSet rs = s.executeQuery(query);

			while(rs.next()) {
				result.add(rs.getDouble("PRICE"));
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			dcf.disconnect(connection);
		}	

		return result;
	}	
	
	
	/**
	 * This method is particularly useful, if you only want to receive the price information in an array, instead of 
	 * having to loop over the tick array. 
	 * 
	 * @param symbol - stock to search for
	 * @param startDate - start date in format of '2014-07-01'
	 * @param endDate - end date in format of '2014-07-31'
	 * @param field - which field to search for (ASK/BID)
	 * @return
	 */
	public ArrayList<Double> getPriceForSymbolByDate(String symbol, String startDate, String endDate, String field) {

		long start = System.currentTimeMillis();
		Connection connection = dcf.getConnection(); 

		ArrayList<Double> result = new ArrayList<Double>();
		Statement s;

		try {
			s = connection.createStatement ();
			String query = "SELECT * FROM `tickprices` where STOCK = '" + symbol + "' and FIELD = '" + field + "' and TIMESTAMP >= '" + startDate + "' AND timestamp <= '" + endDate + "' order by TIMESTAMP";
			logger.debug(query);
			//System.out.println(query);
			ResultSet rs = s.executeQuery(query);

			while(rs.next()) {
				result.add(rs.getDouble("PRICE"));
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			dcf.disconnect(connection);
		}	
		
		long end = System.currentTimeMillis();
		//System.out.println("getPriceForSymbolByDate took " + (end - start) + " msecs to process " + symbol + ", " + startDate + ", " + endDate + ", " + field);
		logger.debug("getPriceForSymbolByDate took " + (end - start) + " msecs to process " + symbol + ", " + startDate + ", " + endDate + ", " + field);

		return result;
	}

		
	/**
	 * Get the last available tick for a symbol.
	 * 
	 * @param stock - which symbol to look for
	 * @param field - which field to use for search.
	 * @return - only the double price value
	 */
	public Double getLastPriceForSymbol(String stock, String field) {

		Connection connection = dcf.getConnection();

		Double result = null;
		Statement s;

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' order by TIMESTAMP desc limit 1");
			ResultSet rs = s.executeQuery("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' order by TIMESTAMP desc limit 1");

			while(rs.next()) {
				result = rs.getDouble("PRICE");
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			dcf.disconnect(connection);
		}	

		return result;
	}
	

	
	/**
	 * Get the last available tick for a symbol.
	 * 
	 * @param stock - which symbol to look for
	 * @param field - which field to use for search.
	 * @return - a tick object
	 */
	public Tick getLastPriceForSymbolAsObject(String stock, String field) {

		Connection connection = dcf.getConnection();

		Tick result = new Tick();
		Statement s;

		try {
			s = connection.createStatement ();
			String query = "SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' order by TIMESTAMP desc limit 1";
			logger.debug(query);
			ResultSet rs = s.executeQuery(query);

			while(rs.next()) {
				result.setSymbol(rs.getString("STOCK"));
				result.setPriceField("ASK");
				result.setPrice(rs.getDouble("PRICE"));
				result.setTimestamp(rs.getTimestamp("TIMESTAMP"));
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	

		return result;
	}

	
	/**
	 * Get all ticks by symbol, field and limit results by count. This method is also ordered descending by price.
	 * 
	 * @param stock - which stock to search for
	 * @param field - which field to look for (ASK, BID...)
	 * @param count - how many objects to return
	 * @return
	 */
	public Tick[] getTicksForSymbolOrderByPrice(String stock, String field, int count) {

		Connection connection = dcf.getConnection();

		ArrayList<Tick> list = new ArrayList<Tick>();
		Statement s;

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' order by PRICE desc limit " + count);
			ResultSet rs = s.executeQuery("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' order by PRICE desc limit " + count);

			int ID = new WatchListDAO().getIDForStock(stock);
			
			while(rs.next()) {
				//Tick(String symbol, int tickerID, long timestamp, int field, String priceField, double price, int canAutoExecute)
				Tick entry = mapResultToTick(rs, ID);

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

		Tick[] ret = new Tick[list.size()];

		return list.toArray(ret);
	}



	/**
	 * Get all ticks by symbol, field and limit results by count.
	 * 
	 * @param stock - which stock to search for
	 * @param field - which field to look for (ASK, BID...)
	 * @param count - how many objects to return
	 * @return
	 */
	public Tick[] getTicksForSymbol(String stock, String field, int count) {

		Connection connection = dcf.getConnection();

		ArrayList<Tick> list = new ArrayList<Tick>();
		Statement s;

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' and PRICE != '0.0' order by TIMESTAMP desc limit " + count);
			ResultSet rs = s.executeQuery("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' and PRICE != '0.0' order by TIMESTAMP desc limit " + count);

			int ID = new WatchListDAO().getIDForStock(stock);
			
			while(rs.next()) {
				Tick entry = mapResultToTick(rs, ID);

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

		Tick[] ret = new Tick[list.size()];

		return list.toArray(ret);
	}
	
	

	/**
	 * Get all ticks by symbol and limit results by count.
	 * 
	 * @param stock - which stock to search for
	 * @param count - how many objects to return
	 * @return
	 */
	public Tick[] getTicksForSymbol(String stock, int count) {

		Connection connection = dcf.getConnection();

		ArrayList<Tick> list = new ArrayList<Tick>();
		Statement s;

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD is not null order by TIMESTAMP desc limit " + count);
			ResultSet rs = s.executeQuery("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD is not null order by TIMESTAMP desc limit " + count);

			int ID = new WatchListDAO().getIDForStock(stock);
			//int fieldID = new TickConverter().convertPriceValueToField(field);
			
			while(rs.next()) {
				//Tick(String symbol, int tickerID, long timestamp, int field, String priceField, double price, int canAutoExecute)
				Tick entry = mapResultToTick(rs, ID);

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

		Tick[] ret = new Tick[list.size()];

		return list.toArray(ret);
	}

	/**
	 * Returns all ticks for the past x days. 
	 * 
	 * @param stock - which stock to search for
	 * @param days - for how many days to get the ticks. (Today minus X days)
	 * @return - list of ticks in an array
	 */
	public Tick[] getTicksForSymbolByDays(String stock, int days) {

		Connection connection = dcf.getConnection();

		ArrayList<Tick> list = new ArrayList<Tick>();
		Statement s;

		try {
			s = connection.createStatement ();
			String query = "SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD is not null and TIMESTAMP >= DATE(NOW()) - INTERVAL " + days + " DAY order by TIMESTAMP asc";
			logger.debug(query);
			ResultSet rs = s.executeQuery(query);

			int ID = new WatchListDAO().getIDForStock(stock);
			//int fieldID = new TickConverter().convertPriceValueToField(field);
			
			while(rs.next()) {
				Tick entry = mapResultToTick(rs, ID);

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

		Tick[] ret = new Tick[list.size()];

		return list.toArray(ret);
	}
	
	
	public Tick[] getTicksForSymbolBetweenTimestamps(String symbol, String field, Timestamp startTime, Timestamp endTime, int modulo) {

		Connection connection = dcf.getConnection();
 
		ArrayList<Tick> list = new ArrayList<Tick>();
		Statement s;

		try {
			s = connection.createStatement ();
			String query = "SELECT * FROM `tickprices` where STOCK = '" + symbol + "' and FIELD = '" + field + "' and TIMESTAMP BETWEEN '" + startTime + "' and '" + endTime + "' order by TIMESTAMP asc";
			logger.debug(query);
			ResultSet rs = s.executeQuery(query);

			int ID = new WatchListDAO().getIDForStock(symbol);
			
			int u = 0;
			while(rs.next()) {
				if (u % modulo == 0) {
					Tick entry = mapResultToTick(rs, ID);

					list.add(entry);
				}
				u++;
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	

		Tick[] ret = new Tick[list.size()];

		return list.toArray(ret);
	}
	
	
	
	/**
	 * Get all of todays ticks. 
	 * 
	 * @param stock - which symbol to search for 
	 * @param field - which field to search for
	 * @return an array of ticks
	 */
	public Tick[] getTodaysTicksForSymbol(String stock, String field) {

		Connection connection = dcf.getConnection();

		ArrayList<Tick> list = new ArrayList<Tick>();
		Statement s;

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' and DATE(`TIMESTAMP`) = CURDATE() order by TIMESTAMP desc");
			ResultSet rs = s.executeQuery("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' and DATE(`TIMESTAMP`) = CURDATE() order by TIMESTAMP desc");

			int ID = new WatchListDAO().getIDForStock(stock);
			
			while(rs.next()) {
				Tick entry = mapResultToTick(rs, ID);
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

		Tick[] ret = new Tick[list.size()];
		return list.toArray(ret);
	}
	
	
	/**
	 * Get all of todays ticks, flattened using modulo, only selecting each nth record. 
	 * 
	 * @param stock - which symbol to search for 
	 * @param field - which field to search for
	 * @param modulo - modulo, or each nth record
	 * @return an array of ticks
	 */
	public Tick[] getTodaysTicksForSymbol(String stock, String field, int modulo) {

		Connection connection = dcf.getConnection();

		ArrayList<Tick> list = new ArrayList<Tick>();
		Statement s;

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' and DATE(`TIMESTAMP`) = CURDATE() order by TIMESTAMP desc");
			ResultSet rs = s.executeQuery("SELECT * FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' and DATE(`TIMESTAMP`) = CURDATE() order by TIMESTAMP desc");
			
			int u = 0;
			int ID = new WatchListDAO().getIDForStock(stock);
			
			while(rs.next()) {
				// Only add, if it is mod(modulo)
				if (u % modulo == 0) {
					//Tick(String symbol, int tickerID, long timestamp, int field, String priceField, double price, int canAutoExecute)
					Tick entry = mapResultToTick(rs, ID);
	
					list.add(entry);
				}
				u++;
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	

		Tick[] ret = new Tick[list.size()];

		return list.toArray(ret);
	}

	
	
	/**
	 * Get the tick count by symbol and field
	 * @param symbol - which symbol to search for
	 * @param field - which field (BID, ASK, CLOSE ...)
	 * @return
	 */
	public int getTickCount(String stock, String field) {
		
		Connection connection = dcf.getConnection();

		int ret = 0;
		Statement s;

		try {
			s = connection.createStatement ();
			logger.debug("SELECT count(*) as COUNT FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' and DATE(`TIMESTAMP`) = CURDATE()");
			ResultSet rs = s.executeQuery("SELECT count(*) as COUNT FROM `tickprices` where STOCK = '" + stock + "' and FIELD = '" + field + "' and DATE(`TIMESTAMP`) = CURDATE()");
			
			while(rs.next()) {

				ret = rs.getInt("COUNT");
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	

		return ret;
	}
	
	public Hashtable<Double, Integer>  getTickScatter(String stock, String field) {
		
			Connection connection = dcf.getConnection();
	
			Hashtable<Double, Integer> pricelist = new Hashtable<Double, Integer>();
			
			Statement s;
	
			try {
				s = connection.createStatement ();
				logger.debug("select price, count(stock) as total from tickprices where stock = '" + stock + "' and field = '" + field + "' group by price order by total desc limit 100");
				ResultSet rs = s.executeQuery("select price, count(stock) as total from tickprices where stock = '" + stock + "' and field = '" + field + "' group by price order by total desc limit 100");
				
				while(rs.next()) {
					pricelist.put(rs.getDouble("PRICE"), rs.getInt("TOTAL"));
					//list.put(rs.getDouble("PRICE"), rs.getInt("TOTAL"));
				}
	
				s.close();
	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// Make sure the connection always gets closed!
				dcf.disconnect(connection);
			}	
	
			return pricelist;
//		}
	}
	
	
	public Double getMaxTickPriceForSymbol(String symbol, int count, String field) {
		ArrayList<Double> res = this.getPriceForSymbol(symbol, count, field);
		Double[] tmp = new Double[res.size()];
		 
		return cf.getMax(res.toArray(tmp));
		
	}

	public Double getMinTickPriceForSymbol(String symbol, int count, String field) {
		ArrayList<Double> res = this.getPriceForSymbol(symbol, count, field);
		Double[] tmp = new Double[res.size()];
		 
		return cf.getMin(res.toArray(tmp));
	}
	
	
	public int getTickCount(String date) {
		
		Connection connection = dcf.getConnection();

		int result = 0;
		Statement s;

		try {
			s = connection.createStatement ();
			String query = "select count(*) as TOTAL from tickprices where `TIMESTAMP` <= '" + date + "'";
			logger.debug(query);
			//System.out.println(query);
			ResultSet rs = s.executeQuery(query);

			while(rs.next()) {
				result = rs.getInt("TOTAL");
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			dcf.disconnect(connection);
		}	

		return result;	
	}
	
	
	/**
	 * Use this method to clean the tick table
	 * @param date - Provide a date, which will be last available records. Everything prior will be archived.
	 */
	public void archiveTicks(String date) {
		
		Connection connection = dcf.getConnection();

		Statement s;

		try {
			s = connection.createStatement ();
			String query = "insert into tickprices_archive select * from tickprices where `TIMESTAMP` <= '"+ date + "'";
			logger.debug(query);
			//System.out.println(query);
			PreparedStatement ps = connection.prepareStatement(query);
			ps.executeUpdate();
			
			int tickCount = this.getTickCount(date);
			int batchSize = 25000;

			for (int i = 0; i <= tickCount/batchSize; i++) {
				// Make sure to also delete the ticks from the tick table in batches of 10000
				PreparedStatement ps2 = connection.prepareStatement("delete from tickprices where `TIMESTAMP` <= '"+ date + "' limit " + batchSize);
				ps2.executeUpdate();
			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			dcf.disconnect(connection);
		}	
				
	}



	private Tick mapResultToTick(ResultSet rs, int ID) throws SQLException {
		Tick entry = new Tick();
		entry.setPriceField(rs.getString("FIELD"));
		entry.setTickerID(ID);
		entry.setField(new TickConverter().convertPriceValueToField(rs.getString("FIELD")));
		entry.setSymbol(rs.getString("STOCK"));
		entry.setPrice(rs.getDouble("PRICE"));
		entry.setTimestamp(rs.getTimestamp("TIMESTAMP"));
		return entry;
	}

}
