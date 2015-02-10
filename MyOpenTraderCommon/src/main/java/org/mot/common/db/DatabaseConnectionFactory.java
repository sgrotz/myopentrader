

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
import java.sql.SQLException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseConnectionFactory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	Logger logger = LoggerFactory.getLogger(getClass());
	private static BasicDataSource ds; 
	private Boolean databaseProperties;
	
	
	//private static Connection conn = null;
	private Configuration config; 
	private static DatabaseConnectionFactory cf;
	
	private DatabaseConnectionFactory() {
		logger.debug("Creating new Database Connection Factory ...");
		PropertiesFactory pf = PropertiesFactory.getInstance();
		
		String pathToConfigDir = pf.getConfigDir();
		try {
			config = new PropertiesConfiguration(pathToConfigDir + "/database.properties");
	
			// Set the user name and password.
			String userName = config.getString("database.username");
			String password = config.getString("database.password");
			int minidle = config.getInt("database.connection.min", 3);
			int maxIdle = config.getInt("database.connection.maxIdle", 40);
			int max = config.getInt("database.connection.max", 50);
			int evictor = config.getInt("database.connection.evictor.millis", 500);
			int timeout = config.getInt("database.connection.timeout", 120);
			Boolean autoCommit = config.getBoolean("database.connection.autoCommit", true);
			String url = config.getString("database.url");
			databaseProperties = config.getBoolean("database.configuration.enabled", false);
			
	        ds = new BasicDataSource();
	        ds.setDriverClassName(config.getString("database.driver"));
	        ds.setUsername(userName);
	        ds.setPassword(password);
	        ds.setUrl(url);
	        ds.setDefaultQueryTimeout(timeout);
	        //ds.setEnableAutoCommitOnReturn(true);
	        ds.setMinIdle(minidle);
	        ds.setMaxIdle(maxIdle);
	        ds.setTimeBetweenEvictionRunsMillis(evictor);
	        ds.setMaxTotal(max);
	        //ds.setTimeBetweenEvictionRunsMillis(500);
	        ds.setDefaultAutoCommit(autoCommit);
	        //ds.setMaxWaitMillis(2000);
			
			// conn = ds.getConnection();
			
			
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public static DatabaseConnectionFactory getInstance() {
		if (cf == null) {
			cf = new DatabaseConnectionFactory();
		}
		return cf;
	}
	

	public BasicDataSource getDataSource() {
		return ds;
	}
	
	
	public Connection getConnection() {
		
		Connection conn = null;
		
		try {
				logger.trace("Getting new connection from database ...");
				
		        BasicDataSource bds = (BasicDataSource) ds;
		        logger.trace("DBConnections - NumActive: " + bds.getNumActive());
		        logger.trace("DBConnections - NumIdle: " + bds.getNumIdle());
		        
		        conn = ds.getConnection();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
		
	}
	
	
	public boolean isDatabaseConfigurationEnabled() {
		return databaseProperties;
	}
	
	
	public boolean isConnected(Connection connection) {
		
		boolean ret = false;
		try {
			ret = !connection.isClosed();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret; 
		
	}
	
	public void disconnect(Connection connection) {
		try {
			logger.trace("Disconnecting from Database ...");
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		System.setProperty("PathToConfigDir", args[0]);
		
		DatabaseConnectionFactory cf = new DatabaseConnectionFactory();
		System.out.println("Connected: " + cf.isConnected(cf.getConnection()));
		
		cf.disconnect(cf.getConnection());
		
	}

	
}
