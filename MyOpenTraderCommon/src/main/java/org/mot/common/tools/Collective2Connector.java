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
		
		c2.sendSignal("93669283", c2.pwd, "BTO", 10, "STOCK", "AAPL", "GTC");
		
		
	}
	
	
	private String baseUrl = "http://www.collective2.com/cgi-perl/signal.mpl?cmd=signal";
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
			baseUrl = props.getString("collective2connector.baseURL","http://www.collective2.com/cgi-perl/signal.mpl?cmd=signal");
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
				
				// There are only BTO (Buy-To-Open) and STC (Sell to Close) - ignore the others for now
				String action;
				if (order.getBUYSELL().equals("BUY")) {
					action = "BTO";
				} else {
					action = "STC";
				}
				 
				// Set the quantity and symbol from the order
				int quantity = order.getQuantity();
				String symbol = order.getSymbol();
				
				// Always set to good til cancel
				String duration = "GTC";
				
				this.sendSignal(systemID, this.pwd, action, quantity, instrument, symbol, duration);
			}
		
		}
	}
	
	
	
	private String sendSignal(String systemID, String pwd, String action, int quantity, String instrument, String symbol, String duration) {

		String response = null;
		String url = baseUrl + 
				"&systemid=" + systemID +
				"&pw=" + pwd +
				"&instrument=" + instrument + 
				"&action=" + action +
				"&quant=" + quantity +
				"&symbol=" + symbol + 
				"&duration=" + duration; 
		
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
