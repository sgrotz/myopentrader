package org.mot.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.mot.common.objects.SimulationResponse;
import org.mot.common.util.DateBuilder;

public class SimulationResponseDAO {
	
	Logger logger = Logger.getLogger(getClass());
	private static DatabaseConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
	DateBuilder db = new DateBuilder();
	
	
	public boolean addSimulationResponse(SimulationResponse sr) {

		Connection connection = dcf.getConnection();
		Statement s;
		try {

			s = connection.createStatement ();
			Timestamp ts = db.getTimestampFromDate();
			
			String query = "INSERT INTO simulation VALUES('" + sr.getSymbol() + "','" + sr.getLoadValues() + "','" + sr.getFrequency() 
					+ "','" + sr.getQuantity()+ "','" + sr.getTradecount()+ "','" + sr.getResult()+ "','" + sr.getTxnCost()+"','" + sr.getPnl()
					+ "','" + sr.getClassName()+ "','"  + ts  +"','" + sr.getStartDate()+"','" + sr.getEndDate() + "')";

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

}
