

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
import org.mot.common.objects.StaticData;
import org.mot.common.objects.Tick;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StaticDataDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7416097641739382791L;
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(StaticDataDAO.class);
	static DateBuilder db = new DateBuilder(); 
	static TickConverter tc = new TickConverter();
	
	

	public synchronized boolean addNewStaticData(StaticData sd) {

		Connection connection = dcf.getConnection();

		Statement s;
		try {
			s = connection.createStatement ();

			logger.debug("INSERT INTO staticdata VALUES('" + db.getTimestampFromDate() + "', '" + sd.getSymbol() + "','" + sd.getRange() + "','" + sd.getYearRange() + "','" + sd.getOpen() + "','" + sd.getVolavg() + "','" + sd.getMktcap() + "','" + sd.getPne()+ "','" + sd.getDivyield() + "','" + sd.getEps()+ "','" + sd.getShares()+ "','" + sd.getBeta() +"')");

			PreparedStatement ps = connection.prepareStatement("INSERT INTO staticdata VALUES((?), '" + sd.getSymbol() + "','" + sd.getRange() + "','" + sd.getYearRange() + "','" + sd.getOpen() + "','" + sd.getVolavg() + "','" + sd.getMktcap() + "','" + sd.getPne()+ "','" + sd.getDivyield() + "','" + sd.getEps()+ "','" + sd.getShares()+ "','" + sd.getBeta() +"')");
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



	public StaticData getLastStaticDataAsObject(String symbol) {

		Connection connection = dcf.getConnection();

		Statement s;
		StaticData entry = new StaticData();

		try {
			s = connection.createStatement ();
			logger.debug("SELECT * FROM `staticdata` where SYMBOL = '" + symbol + "' order by TIMESTAMP desc limit 1");
			ResultSet rs = s.executeQuery("SELECT * FROM `staticdata` where SYMBOL = '" + symbol + "' order by TIMESTAMP desc limit 1");

			while(rs.next()) {
				entry.setBeta(rs.getString("BETA"));
				entry.setDivyield(rs.getString("DIVYIELD"));
				entry.setEps(rs.getString("EPS"));
				entry.setMktcap(rs.getString("MKTCAP"));
				entry.setOpen(rs.getString("OPEN"));
				entry.setPne(rs.getString("PNE"));
				entry.setRange(rs.getString("RANGE"));
				entry.setShares(rs.getString("SHARES"));
				entry.setSymbol(symbol);
				entry.setVolavg(rs.getString("VOLAVG"));
				entry.setYearRange(rs.getString("YEARRANGE"));

			}

			s.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Make sure the connection always gets closed!
			dcf.disconnect(connection);
		}	

		return entry;
	}



}
