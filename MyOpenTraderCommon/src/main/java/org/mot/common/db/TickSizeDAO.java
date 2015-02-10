package org.mot.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.mot.common.objects.TickSize;
import org.mot.common.util.DateBuilder;


public class TickSizeDAO {
	
	private DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	Logger logger = Logger.getLogger(getClass());
	DateBuilder db = new DateBuilder(); 
	
	public boolean addTickSize(TickSize tickSize) {
		Connection connection = dcf.getConnection();

		Statement s;
		try {
			s = connection.createStatement ();
			
			String query = "INSERT INTO ticksize VALUES((?), '" + tickSize.getSymbol() + "','" + tickSize.getField() 
					+ "','" + tickSize.getSize() + "')";

			logger.debug(query);

			PreparedStatement ps = connection.prepareStatement(query);
			ps.setTimestamp(1, db.convertLongToTimestamp(tickSize.getTimestamp()));
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

}
