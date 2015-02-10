

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

import org.mot.common.objects.Property;
import org.mot.common.util.DateBuilder;
import org.mot.common.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertyDAO {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	private IDGenerator idg = new IDGenerator();
	private DateBuilder db = new DateBuilder(); 

	public void addProperty(Property property) {
		
		if (property.getID() == null) {
			property.setID(idg.getUniqueID());
		}

		Connection connection = dcf.getConnection();
		Statement s;
		try {
			String query = "INSERT INTO property VALUES('" + property.getID() + "','" + property.getProperty() + "','" + property.getValue() + "',(?)," + property.isEnabled() + ")";
			s = connection.createStatement ();
			logger.debug(query);

			PreparedStatement ps = connection.prepareStatement(query);
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
	}
	
	public void updateProperty(String property, String value) {
		
		Property p = this.getProperty(property);
		
		if (p.getProperty() == null) {
			p.setEnabled(true);
			p.setProperty(property);
			p.setValue(value);
			this.addProperty(p);
		} else {
			p.setValue(value);
			this.updateProperty(p);
		}
		
	}
	
	
	public void updateProperty(Property property) {

		Connection connection = dcf.getConnection();
		Statement s;
		try {
			String query = "UPDATE property SET VALUES('" + property.getID() + "','" + property.getProperty() + "','" + property.getValue() + "',(?),'" + property.isEnabled() + "') WHERE ID='" + property.getID() + "'";
			s = connection.createStatement ();
			logger.debug(query);

			PreparedStatement ps = connection.prepareStatement(query);
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
	}
	
	
	
	public Property getProperty(String property) {		
		Connection connection = dcf.getConnection();

		Property config = new Property();
		Statement s;

		try {
			String query = "SELECT * FROM property WHERE PROPERTY ='" + property + "'";
			s = connection.createStatement ();
			logger.debug(query);
			ResultSet rs = s.executeQuery(query);

			while(rs.next()) {
				mapProperty(config, rs);
				
			}
			
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	

		return config;		
	}
	
	public boolean doesPropertyExist(String property) {		
		Connection connection = dcf.getConnection();

		boolean found = false;
		Statement s;

		try {
			String query = "SELECT * FROM property WHERE PROPERTY ='" + property + "'";
			s = connection.createStatement ();
			logger.debug(query);
			ResultSet rs = s.executeQuery(query);

			while(rs.next()) {
				found = true;
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

		return found;		
	}
	
	
	

	private void mapProperty(Property config, ResultSet rs)
			throws SQLException {
		config.setID(rs.getString("ID"));
		config.setEnabled(rs.getBoolean("ENABLED"));
		config.setProperty(rs.getString("PROPERTY"));
		config.setTimestamp(rs.getTimestamp("TIMESTAMP"));
		config.setValue(rs.getString("VALUE"));
	}
	
	
	

}
