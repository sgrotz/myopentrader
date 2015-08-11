

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

  
  
import java.io.IOException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mot.common.db.StrategyDAO;
import org.mot.common.objects.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Collective2Connector {

	public static void main (String[] args) {
		
		// Get Properties Instance
		PropertiesFactory pf = PropertiesFactory.getInstance();
		
		// First make sure to set the config directory
		pf.setConfigDir(args[0]);
		
		Collective2Connector c2 = new Collective2Connector();
		
		//c2.sendSignal(123, "93669283", c2.pwd, "BTO", 10, "STOCK", "AAPL", "GTC");
		
		c2.sendSignal(123, "93669283", c2.pwd, "STC", 10, "STOCK", "AAPL", "GTC");
		
		
	}
	
	
	private String baseUrl = "http://www.collective2.com/cgi-perl/signal.mpl?";
	private String pwd;
	Logger logger = LoggerFactory.getLogger(getClass());
	private PropertiesFactory pf = PropertiesFactory.getInstance();
	private StrategyDAO sd = new StrategyDAO();
	private boolean enabled = false; 
	
	
	public Collective2Connector() {
			
		// First make sure to get the config directory
		String confDir = pf.getConfigDir();
	
		try {
			// Read in the properties
			Configuration props = new PropertiesConfiguration(confDir + "/collective2.properties");
	
			// Overwrite with configuration file settings
			baseUrl = props.getString("collective2connector.baseURL","http://www.collective2.com/cgi-perl/signal.mpl?");
			pwd = props.getString("collective2connector.password");
			
			enabled = props.getBoolean("collective2connector.enabled");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendOrderToCollective2(Order order) {
		
		if (enabled) {
			// Make sure to lookup the system id, which belongs to a particular strategy.
			String systemID = sd.getStrategyC2IDByStrategy(order.getStrategy());
			
			// Only run if there is a system ID available.
			if (systemID != null) {
				// instrument should always be stock (we dont support anything else yet)
				String instrument = "stock";
				
				// As default use the order ID as signalID
				int ID = Integer.valueOf(order.getID());
				
				// There are only BTO (Buy-To-Open) and STC (Sell to Close) - ignore the others for now
				String action;
				if (order.getBUYSELL().equals("BUY")) {
					action = "BTO";
				} else {
					action = "STC";
					
					// If it is a sell order, make sure that the ID reflects the previous buy order. 
					ID = Integer.valueOf(order.getClosed());
				}
				 
				// Set the quantity and symbol from the order
				int quantity = order.getQuantity();
				String symbol = order.getSymbol();
				
				// Always set to good til cancel
				String duration = "GTC";
				
				this.sendSignal(ID, systemID, this.pwd, action, quantity, instrument, symbol, duration);
			}
		
		}
	}
	
	
	
	private String sendSignal(int ID, String systemID, String pwd, String action, int quantity, String instrument, String symbol, String duration) {

		String response = null;
		String url = baseUrl + 
				"systemid=" + systemID +
				"&pw=" + pwd +
				"&instrument=" + instrument + 
				"&action=" + action +
				"&quant=" + quantity +
				"&symbol=" + symbol + 
				"&duration=" + duration +
				"&signalid=" + ID; 
		
		logger.debug("Sending signal: " + url);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = null;  
	
		try {
			response1 = httpclient.execute(httpGet);
		    logger.debug(response1.getStatusLine().toString());
		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    		    
		    response = EntityUtils.toString(entity1);
		    
		    logger.debug(response);
		    EntityUtils.consume(entity1);
			
		    response1.close();
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return response;
	}
	
}
