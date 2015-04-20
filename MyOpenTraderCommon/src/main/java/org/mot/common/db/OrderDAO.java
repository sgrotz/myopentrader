

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
import java.util.ArrayList;

import org.mot.common.objects.Order;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderDAO {

	private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);
	DateBuilder db = new DateBuilder(); 
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	
	/**
	 * @param order
	 * @return
	 */
	synchronized public boolean addNewOrder(Order order) {
		
		if (order.getAvgBuyPrice() <= 0) {
			// Set the average price identical to the price. 
			order.setAvgBuyPrice(order.getPrice());
		}
		
		if (order.getClosed() == null) {
			order.setClosed("");
		}
		
		if (order.getBarrier() <= 0) {
			order.setBarrier(order.getPrice());
		}
		
		Connection connection = dcf.getConnection();
		
		Statement s;
		try {
			
			s = connection.createStatement ();
			
	    		logger.debug("INSERT INTO orders VALUES('" + order.getID() +"', '"+ order.getBUYSELL()
						+"', '"+ order.getQuantity() +"','NEW','" + order.getPrice() + "','" + order.getAvgBuyPrice() + "','"+ order.getBarrier() +"','" + order.getSymbol() + "',(?)," + order.isSimulated() + ",'" + order.getStrategy() +"')");
	    		
				PreparedStatement ps = connection.prepareStatement("INSERT INTO orders VALUES('" + order.getID() +"', '"+ order.getBUYSELL()
						+"', '"+ order.getQuantity() +"','NEW','" + order.getPrice() + "','" + order.getAvgBuyPrice() + "','"+ order.getBarrier() +"','" + order.getSymbol() + "',(?),'" + order.getClosed() + "'," + order.isSimulated() + ",'" + order.getStrategy() +"')");
				ps.setTimestamp(1, db.getTimestampFromDate());
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
	
	public void markOrderAsClosed(String orderID, String closeOrderID, boolean forced) {
				
		Connection connection = dcf.getConnection();
		
		// Make sure we reflect the closed status correctly
		String STATUS = "CLOSED";
		
		if (forced) {
			STATUS = "FCLOSED";
		}
		
		this.updateOrderStatus(orderID, STATUS);
		this.updateOrderStatus(closeOrderID, STATUS);
		
		Statement s;
		try {
			
			s = connection.createStatement ();
			
	    		logger.debug("update orders set CLOSED =" + orderID + " where ORDERID = " + closeOrderID);
	    		
				PreparedStatement ps = connection.prepareStatement("update orders set CLOSED =" + orderID + " where ORDERID = " + closeOrderID);
				ps.executeUpdate();

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}		
				
				
	}
	
	
	/**
	 * @param strategy
	 * @param simulated
	 */
	public void deleteOrdersByStrategy(String strategy, boolean simulated) {
		
		Statement s;
		Connection connection = dcf.getConnection();
		try {
			s = connection.createStatement ();
			
    		logger.debug("DELETE FROM orders WHERE strategy = '" + strategy + "' and simulated = " + simulated);
    		
			PreparedStatement ps = connection.prepareStatement("DELETE FROM orders WHERE strategy = '" + strategy + "' and simulated = " + simulated);
			//ps.setTimestamp(1, db.deriveTimestampFromDate());
			ps.executeUpdate();

	    	s.close();
	    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

	}
	
	
	/**
	 * @return an array of orders
	 */
	public Order[] getAllOrders() {
		
		ArrayList<Order> list = new ArrayList<Order>();
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			
	    		logger.debug("SELECT * FROM orders order by TIMESTAMP desc");

		    	ResultSet rs = s.executeQuery("SELECT * FROM orders order by TIMESTAMP desc");

		    	while (rs.next()) {
		    		Order order = mapResultSetToOrder(rs);

		    		list.add(order);
		    		
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	
	public String getFirstOrderDate() {
		
		String ret = null;
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			String query ="select * from orders order by TIMESTAMP asc limit 1";
			
	    		logger.debug(query);

		    	ResultSet rs = s.executeQuery(query);

		    	while (rs.next()) {
		    		ret = rs.getString("TIMESTAMP");
		    		
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		return ret; 
	}
	
	public String getLastOrderDate() {
		
		String ret = null;
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			String query ="select * from orders order by TIMESTAMP desc limit 1";
			
	    		logger.debug(query);

		    	ResultSet rs = s.executeQuery(query);

		    	while (rs.next()) {
		    		ret = rs.getString("TIMESTAMP");
		    		
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		return ret; 
	}
	
	public Double getOrderAmount(String ID) {
		
		Double ret = null;
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			
				String query = "select price from orders where ORDERID = '" + ID + "'";
	    		logger.debug(query);

		    	ResultSet rs = s.executeQuery(query);

		    	while (rs.next()) {
		    		ret = rs.getDouble("PRICE");
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		return ret; 
	}
	
	
	public Order[] getAllOpenOrders() {
		
		ArrayList<Order> list = new ArrayList<Order>();
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			
	    		logger.debug("SELECT * FROM orders where BUYSELL = 'BUY' and CLOSED = '' order by TIMESTAMP desc");

		    	ResultSet rs = s.executeQuery("SELECT * FROM orders where BUYSELL = 'BUY' and CLOSED = '' order by TIMESTAMP desc");

		    	while (rs.next()) {
		    		Order order = mapResultSetToOrder(rs);

		    		list.add(order);
		    		
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	
	public Order[] getAllOpenOrders(String strategy) {
		
		ArrayList<Order> list = new ArrayList<Order>();
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
				
			String query = "SELECT * FROM orders where BUYSELL = 'BUY' and CLOSED = '' and STRATEGY = '"+ strategy + "' order by TIMESTAMP desc";
		
    		logger.debug(query);
	    	ResultSet rs = s.executeQuery(query);

	    	while (rs.next()) {
	    		Order order = mapResultSetToOrder(rs);

	    		list.add(order);
	    		
	    	}

	    	s.close();
	    	dcf.disconnect(connection);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	
	public Order[] getAllClosedOrders() {
		
		ArrayList<Order> list = new ArrayList<Order>();
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			
				String query = "SELECT * FROM orders where STATUS = 'CLOSED' order by TIMESTAMP desc";
			
	    		logger.debug(query);
		    	ResultSet rs = s.executeQuery(query);

		    	while (rs.next()) {
		    		Order order = mapResultSetToOrder(rs);

		    		list.add(order);
		    		
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	
	
	/**
	 * Gets the strategy from all closed orders as an arraylist.
	 * 
	 * @return an arraylist of strategies as String
	 */
	public ArrayList<String> getAllClosedOrderStrategies() {
		
		ArrayList<String> list = new ArrayList<String>();
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			
				String query = "SELECT STRATEGY FROM orders where STATUS like '%CLOSED%' group by STRATEGY order by TIMESTAMP desc";
			
	    		logger.debug(query);
		    	ResultSet rs = s.executeQuery(query);

		    	while (rs.next()) {
		    		list.add(rs.getString("STRATEGY"));
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		return list; 
	}
	
	public ArrayList<String> getAllClosedOrderStrategiesBySymbol(String symbol) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			
				String query = "SELECT STRATEGY FROM orders where STATUS like '%CLOSED%' and STRATEGY like '" + symbol + "%' group by STRATEGY order by TIMESTAMP desc";
			
	    		logger.debug(query);
		    	ResultSet rs = s.executeQuery(query);

		    	while (rs.next()) {
		    		list.add(rs.getString("STRATEGY"));
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		return list; 
	}
	
	
	public Order[] getAllClosedOrdersByStrategy(String strategy) {
		
		ArrayList<Order> list = new ArrayList<Order>();
		
		Connection connection = dcf.getConnection();
		Statement s;
		try {
			s = connection.createStatement ();
			
				String query = "SELECT * FROM orders where STATUS like '%CLOSED%' and STRATEGY ='" + strategy + "' order by TIMESTAMP desc";
			
	    		logger.debug(query);
		    	ResultSet rs = s.executeQuery(query);

		    	while (rs.next()) {
		    		Order order = mapResultSetToOrder(rs);

		    		list.add(order);
		    		
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}
		
		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	

	
	
	/**
	 * @param symbol - Symbol to look for orders
	 * @param strategy - which strategy to use
	 * @return - an array of orders
	 */
	public Order[] getAllOrdersByStrategy(String symbol, String strategy) {
		
		ArrayList<Order> list = new ArrayList<Order>();
		Statement s;
		Connection connection = dcf.getConnection();
		
		try {
			s = connection.createStatement ();
			
	    		logger.debug("SELECT * FROM orders where SYMBOL ='" + symbol + "' and STRATEGY='"+ strategy + "'");

		    	ResultSet rs = s.executeQuery("SELECT * FROM orders where SYMBOL ='" + symbol + "' and STRATEGY='"+ strategy + "'");

		    	while (rs.next()) {
		    		Order order = mapResultSetToOrder(rs);

		    		list.add(order);
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	
	/**
	 * @param symbol - Symbol to look for orders
	 * @param strategy - which strategy to use
	 * @param buySell - buy sell indicator (can be BUY or SELL)
	 * @return - an array of orders
	 */
	public Order[] getAllOrdersByStrategyAndBuySell(String symbol, String strategy, String buySell) {
		
		ArrayList<Order> list = new ArrayList<Order>();
		Statement s;
		Connection connection = dcf.getConnection();
		
		try {
			s = connection.createStatement ();
			
	    		logger.debug("SELECT * FROM orders where SYMBOL ='" + symbol + "' and STRATEGY='"+ strategy + "'");

		    	ResultSet rs = s.executeQuery("SELECT * FROM orders where SYMBOL ='" + symbol + "' and STRATEGY='"+ strategy + "'");

		    	while (rs.next()) {
		    		Order order = mapResultSetToOrder(rs);

		    		list.add(order);
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	
	
	
	public Order[] getAllOrdersByStrategy(String strategy) {
		
		ArrayList<Order> list = new ArrayList<Order>();
		Statement s;
		Connection connection = dcf.getConnection();
		
		try {
			s = connection.createStatement ();
			
	    		logger.debug("SELECT * FROM orders where STRATEGY='"+ strategy + "'");

		    	ResultSet rs = s.executeQuery("SELECT * FROM orders where STRATEGY='"+ strategy + "'");

		    	while (rs.next()) {
		    		Order order = mapResultSetToOrder(rs);

		    		list.add(order);
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	
	
	public Order[] getAllOrdersBySymbol(String symbol, String BUYSELL) {
		
		ArrayList<Order> list = new ArrayList<Order>();
		Statement s;
		Connection connection = dcf.getConnection();
		
		try {
			s = connection.createStatement ();
			
	    		logger.debug("SELECT * FROM orders where SYMBOL ='"+ symbol + "' and BUYSELL = '" + BUYSELL + "'");

		    	ResultSet rs = s.executeQuery("SELECT * FROM orders where SYMBOL ='"+ symbol + "' and BUYSELL = '" + BUYSELL + "'");

		    	while (rs.next()) {
		    		Order order = mapResultSetToOrder(rs);

		    		list.add(order);
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		Order[] orders = new Order[list.size()];

		return list.toArray(orders); 
	}
	
	
	
	


	private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
		Order order = new Order();
		order.setBUYSELL(rs.getString("BUYSELL"));
		order.setID(rs.getString("ORDERID"));
		order.setQuantity(rs.getInt("QUANTITY"));
		order.setStatus(order.status.valueOf(rs.getString("STATUS")));
		order.setPrice(rs.getDouble("PRICE"));
		order.setSymbol(rs.getString("SYMBOL"));
		order.setTimestamp(rs.getTimestamp("TIMESTAMP"));
		order.setSimulated(rs.getBoolean("SIMULATED"));
		order.setStrategy(rs.getString("STRATEGY"));
		order.setAvgBuyPrice(rs.getDouble("AVGPRICE"));
		order.setBarrier(rs.getDouble("BARRIER"));
		order.setClosed(rs.getString("CLOSED"));
		return order;
	}
	
	
	
	/**
	 * @param stock
	 * @param strategy
	 * @return the amount of open orders
	 */
	public int getOpenOrderQuantity(String stock, String strategy) {

		int count = 0;
		Statement s;
		Connection connection = dcf.getConnection();
		try {

			s = connection.createStatement ();
			
	    		logger.debug("SELECT BUYSELL, QUANTITY FROM orders where SYMBOL ='" + stock + "' and STRATEGY='"+ strategy + "'");

		    	ResultSet rs = s.executeQuery("SELECT BUYSELL, QUANTITY FROM orders where SYMBOL ='" + stock + "' and STRATEGY='"+ strategy + "'");

		    	while (rs.next()) {
		    		if(rs.getString("BUYSELL").equals("BUY")) {
		    			count = count + rs.getInt("QUANTITY");
		    		} else {
		    			count = count - rs.getInt("QUANTITY");
		    		}
		    		
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return count; 
	}
	
	
	
	public Order getLastOrder(String symbol, String strategy) {
		Order order = new Order();
		Statement s;
		Connection connection = dcf.getConnection();
		try {
			
			s = connection.createStatement ();
			
	    		logger.debug("SELECT * FROM orders where SYMBOL ='" + symbol + "' and STRATEGY='"+ strategy + "' order by TIMESTAMP desc limit 1");
		    	ResultSet rs = s.executeQuery("SELECT * FROM orders where SYMBOL ='" + symbol + "' and STRATEGY='"+ strategy + "' order by TIMESTAMP desc limit 1");

		    	while (rs.next()) {
		    		order = mapResultSetToOrder(rs);
		    	}

		    	s.close();
		    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return order; 
	}
	
	
	public int updateOrderStatus(String ID, String status) {
		
		Statement s;
		int rs = 0;
		Connection connection = dcf.getConnection();
		
		try {
			s = connection.createStatement ();
			
    		logger.debug("UPDATE orders set STATUS = '" + status + "' where ORDERID ='" + ID+ "'");
	    	 rs = s.executeUpdate("UPDATE orders set STATUS = '" + status + "' where ORDERID ='" + ID+ "'");

	    	s.close();
	    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return rs; 
	}
	
	public int updateOrderPrice(String ID, Double price) {
		
		Statement s;
		int rs = 0;
		Connection connection = dcf.getConnection();
		
		try {
			s = connection.createStatement ();
			
    		logger.debug("UPDATE orders set AVGPRICE = '" + price + "' where ORDERID ='" + ID+ "'");
	    	 rs = s.executeUpdate("UPDATE orders set AVGPRICE = '" + price + "' where ORDERID ='" + ID+ "'");

	    	s.close();
	    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return rs; 
	}
	
	public int updateOrderBarrier(String ID, Double barrier) {
		
		Statement s;
		int rs = 0;
		Connection connection = dcf.getConnection();
		
		
		try {
			s = connection.createStatement ();
			
    		logger.debug("UPDATE orders set BARRIER = '" + barrier + "' where ORDERID ='" + ID+ "'");
	    	 rs = s.executeUpdate("UPDATE orders set AVGPRICE = '" + barrier + "' where ORDERID ='" + ID+ "'");

	    	s.close();
	    	dcf.disconnect(connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dcf.disconnect(connection);
		}

		return rs; 
	}
	
	
}
