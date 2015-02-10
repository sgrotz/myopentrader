

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import org.mot.common.objects.WatchList;
import org.mot.common.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchListDAO {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	
	public void addStockToWatchlist(String stock, String name) {
		
		if (isStockOnWatchlist(stock)) {
			logger.warn("Stock " + stock + " is already on the watchlist ...");
			
		} else {
			Connection connection = dcf.getConnection();
	        try {
	    		Statement s = connection.createStatement ();
	    		logger.debug("INSERT INTO watchlist VALUES('" + new IDGenerator().getUniqueIntID() + "', '" + stock + "', '" + name + "', true, null)");
		    	s.executeUpdate("INSERT INTO watchlist VALUES('" + new IDGenerator().getUniqueIntID() + "', '" + stock + "', '" + name + "', true, null)");
	    	
		    	s.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				dcf.disconnect(connection);
			}	
		}
        
	}
	
	public ArrayList<String> getWatchlist(String type) {
		
		ArrayList<String> result = new ArrayList<String>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ENABLED = true and TYPE = '"+ type + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ENABLED = true and TYPE = '"+ type + "'");
	
	    	while(rs.next()) {
	    		result.add(rs.getString("SYMBOL"));
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
	
	public Hashtable<Integer, String> getWatchlistAsTable() {
		
		Hashtable<Integer, String> result = new Hashtable<Integer, String>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ENABLED = true");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ENABLED = true");
	
	    	while(rs.next()) {
	    		result.put(rs.getInt("ID"), rs.getString("SYMBOL"));
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
	
	/**
	 * SELECT * FROM `watchlist` where ENABLED = true and TYPE = '" + type + "'"
	 * @param type
	 * @return
	 */
	public Hashtable<Integer, String> getWatchlistAsTable(String type) {
		
		Hashtable<Integer, String> result = new Hashtable<Integer, String>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {

			s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ENABLED = true and TYPE = '" + type + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ENABLED = true and TYPE = '" + type + "'");
	
	    	while(rs.next()) {
	    		result.put(rs.getInt("ID"), rs.getString("SYMBOL"));
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
	
	
	
	public WatchList[] getWatchlistAsObject() {
		
		long startTime = System.currentTimeMillis();
		
		ArrayList<WatchList> list = new ArrayList<WatchList>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {
        	s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ENABLED = true");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ENABLED = true");

	    	while(rs.next()) {	    		
	    		list.add(mapWatchlist(rs));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
        
        WatchList[] ret = new WatchList[list.size()];
        
		long endTime = System.currentTimeMillis();
		logger.trace("Service took: " + (endTime - startTime) + " msecs");

		return list.toArray(ret);
	}

	
	private WatchList mapWatchlist(ResultSet rs) throws SQLException {
		WatchList wl = new WatchList();
		wl.setCurrency(rs.getString("CURRENCY"));
		wl.setID(rs.getString("ID"));
		wl.setSymbol(rs.getString("SYMBOL"));
		wl.setExchange(rs.getString("EXCHANGE"));
		wl.setType(rs.getString("TYPE"));
		wl.setName(rs.getString("NAME"));
		wl.setEnabled(rs.getBoolean("ENABLED"));
		
		return wl;
	}
	
	
	public WatchList[] getWatchlistByExecutorAsObject(String executor) {
		
		long startTime = System.currentTimeMillis();
		
		ArrayList<WatchList> list = new ArrayList<WatchList>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {
        	String query; 
			if (executor.equals("ALL")) {
				// Overwrite the executor to be an empty string
				query = "SELECT * FROM `watchlist` where ENABLED = true";
			} else if (executor.equals("OTHERS")) {
				query = "SELECT * FROM `watchlist` where ENABLED = true and EXECUTOR is null";
			} else {
				query = "SELECT * FROM `watchlist` where ENABLED = true and EXECUTOR = '" + executor + "'";
			}
        	
        	s = connection.createStatement ();
			logger.debug(query);
	    	ResultSet rs = s.executeQuery(query);

	    	while(rs.next()) {
	    		list.add(mapWatchlist(rs));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
        
        WatchList[] ret = new WatchList[list.size()];
        
		long endTime = System.currentTimeMillis();
		logger.trace("Service took: " + (endTime - startTime) + " msecs");

		return list.toArray(ret);
	}
	
	
	public ArrayList<Integer> getWatchlistByID() {
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {

			s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ENABLED = true");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ENABLED = true");
	
	    	while(rs.next()) {
	    		result.add(rs.getInt("ID"));
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
	
	
	public boolean isStockOnWatchlist (String stock) {
		boolean found = false;
		
		if (getIDForStock(stock) != 0) {
			found = true;
		}
		
		return found;
	}
	
	
	public int getIDForStock(String stock) {
	
		Connection connection = dcf.getConnection();
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		Statement s;
		int r = 0;
		
        try {
        	s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where SYMBOL = '" + stock + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where SYMBOL = '" + stock + "'");

	    	while(rs.next()) {
	    		result.add(rs.getInt("ID"));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}	

        if (result.isEmpty()) {
        	return r;
        } else {
        	return result.get(0);
        }
		
	}
	
	public int getCountForObjects(String type) {
		
		Connection connection = dcf.getConnection();
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		Statement s;
		int r = 0;
        try {
        
			s = connection.createStatement ();
			logger.debug("SELECT count(*) as count FROM `watchlist` where TYPE = '" + type + "'");
	    	ResultSet rs = s.executeQuery("SELECT count(*) as count FROM `watchlist` where TYPE = '" + type + "'");

	    	while(rs.next()) {
	    		result.add(rs.getInt("count"));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}	

        if (result.isEmpty()) {
        	return r;
        } else {
        	return result.get(0);
        }
		
	}
	
	
	public String getStockForID(int ID) {
		
		ArrayList<String> result = new ArrayList<String>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {

			s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ID = '" + ID + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ID = '" + ID + "'");

	    	while(rs.next()) {
	    		result.add(rs.getString("SYMBOL"));
	    	}
	    	
	    	s.close();
    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}	

        if (result.isEmpty()) {
        	return null;
        } else {
        	return result.get(0);
        }
		
	}
	
	public String getCurrencyForID(int ID) {
		
		ArrayList<String> result = new ArrayList<String>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {

			s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ID = '" + ID + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ID = '" + ID + "'");

	    	while(rs.next()) {
	    		result.add(rs.getString("CURRENCY"));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}	

        if (result.isEmpty()) {
        	return null;
        } else {
        	return result.get(0);
        }
		
	}
	
	public String getCurrencyForSymbol(String symbol) {
		
		ArrayList<String> result = new ArrayList<String>();
		Statement s;
		Connection connection = dcf.getConnection();
		
        try {

			s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where SYMBOL = '" + symbol + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where SYMBOL = '" + symbol + "'");

	    	while(rs.next()) {
	    		if (rs.getString("CURRENCY") == null) {
	    			// Default to USD, if there is no currency specified
	    			result.add("USD");
	    		} else {
	    			result.add(rs.getString("CURRENCY"));
	    		}
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}	

        if (result.isEmpty()) {
        	return null;
        } else {
        	return result.get(0);
        }
		
	}
	
	
	public String getTypeForID(int ID) {
		
		ArrayList<String> result = new ArrayList<String>();
		Statement s;
		Connection connection = dcf.getConnection();

        try {
 		
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ID = '" + ID + "'");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ID = '" + ID + "'");

	    	while(rs.next()) {
	    		result.add(rs.getString("TYPE"));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}	

        if (result.isEmpty()) {
        	return null;
        } else {
        	return result.get(0);
        }
		
	}
	
	public String getExchangeForID(int ID) {
		
		ArrayList<String> result = new ArrayList<String>();
		Statement s;
		Connection connection = dcf.getConnection();

        try {
        	s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where ID = '" + ID + "' limit 1");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where ID = '" + ID + "' limit 1");

	    	while(rs.next()) {
	    		result.add(rs.getString("EXCHANGE"));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		} 	

        if (result.isEmpty()) {
        	return null;
        } else {
        	return result.get(0);
        }
		
	}
	
	public String getExchangeForSymbol(String symbol) {
		
		ArrayList<String> result = new ArrayList<String>();
		Statement s;
		Connection connection = dcf.getConnection();

        try {
        	s = connection.createStatement ();
			logger.debug("SELECT * FROM `watchlist` where SYMBOL = '" + symbol + "' limit 1");
	    	ResultSet rs = s.executeQuery("SELECT * FROM `watchlist` where SYMBOL = '" + symbol + "' limit 1");

	    	while(rs.next()) {
	    		result.add(rs.getString("EXCHANGE"));
	    	}
	    	
	    	s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		} 	

        if (result.isEmpty()) {
        	return null;
        } else {
        	return result.get(0);
        }
		
	}
	
	
	
	

	public static void main(String[] args) {
		System.setProperty("PathToConfigDir", args[0]);
		
		WatchListDAO wlw = new WatchListDAO();
		
		wlw.addStockToWatchlist( "AAPL", "APPLE");
		wlw.addStockToWatchlist( "GOOG", "GOOGLE");
		wlw.addStockToWatchlist("IBM", "IBM");
		wlw.addStockToWatchlist( "GS", "Goldman");
		wlw.addStockToWatchlist( "GM", "General Motors");
		wlw.addStockToWatchlist( "MS", "Morgan Stanley");
		/*		wlw.addStockToWatchlist(cf.getConnection(), "GS");
		wlw.addStockToWatchlist(cf.getConnection(), "TSLA");*/
		System.out.println("All available stocks on the watchlist: " + wlw.getWatchlist("STK"));
		System.out.println("All available stock IDs on the watchlist: " + wlw.getWatchlistByID());
		System.out.println("Stock ID for AAPL" + wlw.getIDForStock( "AAPL"));
		
	}

}
