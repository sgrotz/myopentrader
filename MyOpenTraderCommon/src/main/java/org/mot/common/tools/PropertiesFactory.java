

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
  
  
  package org.mot.common.tools;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mot.common.db.DatabaseConnectionFactory;

public class PropertiesFactory {

	private static PropertiesFactory instance; 
	private String configDir;
	private DatabaseConnectionFactory dcf;
	
	
	private PropertiesFactory() {
		
	}
	
	public static PropertiesFactory getInstance() {
		if (instance == null) {
			instance = new PropertiesFactory();
		}
		return instance;
	}

	
	public String getConfigDir() {
		return configDir;
	}

	
	public void setConfigDir(String configDir) {
		this.configDir = configDir;
	}
	
	
	public Configuration getConfiguration(String filename) {
		Configuration config = null;
		
		if (dcf.isDatabaseConfigurationEnabled()) {
			// If set to true, get the properties from the database
			config = this.getDatabaseConfiguration();
		} else {
			config = this.getFileConfiguration(filename);
		}
		
		return config;
	}
	
	
	private Configuration getDatabaseConfiguration() {
		Configuration config = null;
		
		BasicDataSource bds = dcf.getDataSource();	
		config = new DatabaseConfiguration(bds, "configuration", "property", "value");
		
		return config;
	}
	
	
	private Configuration getFileConfiguration(String filename) {
		Configuration config = null;
		
		// Read the properties from file
		try {
			config = new PropertiesConfiguration(this.getConfigDir() + "/" + filename);
		
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return config;
	}
	
	
}
