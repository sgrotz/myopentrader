

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
  
  
  package org.mot.core.simulation.listener;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.mot.common.conversion.LoadValueConverter;
import org.mot.common.db.SimulationResponseDAO;
import org.mot.common.objects.LoadValue;
import org.mot.common.objects.SimulationRequest;
import org.mot.common.objects.SimulationResponse;
import org.mot.common.tools.PropertiesFactory;

public class SimulationMessageListener implements MessageListener{		
		

	Logger logger = Logger.getLogger(getClass());
	private SimulationResponseDAO srd = new SimulationResponseDAO();
	private LoadValueConverter lvc = new LoadValueConverter();
	
	
	@SuppressWarnings("unchecked")
		@Override
		public void onMessage(Message msg) {
			// TODO Auto-generated method stub
			
			
			BytesMessage message = (BytesMessage) msg;

			// Read in properties from file
			Configuration simulationProperties;

			byte[] bytes;
			try {
				PropertiesFactory pf = PropertiesFactory.getInstance();
				simulationProperties = new PropertiesConfiguration(pf.getConfigDir()  + "/simulation.properties");
				final Double minProfit = simulationProperties.getDouble("simulation.default.db.requireMinProfit", 2.0);
				final Double txnpct = simulationProperties.getDouble("simulation.order.txncost.pct", 0.0024);
				
			
				bytes = new byte[(int) message.getBodyLength()];
				message.readBytes(bytes);
				
				SimulationRequest sr = SimulationRequest.deserialize(bytes);
				sr.setMinProfit(minProfit);
				sr.setTxnPct(txnpct);
				
				String className = sr.getClassName();
				
				Class<?> c;
				try {
					c = Class.forName(className);
					Object inst = c.newInstance();
					
					// Make sure the startup method is called for initialization...
					//Method m1= c.getDeclaredMethod("startup", String.class, String.class, LoadValue[].class, Boolean.class, Integer.class );
					//m1.invoke(inst, sr.getClassName(), sr.getSymbol(), lvc.convertStringToValues(sr.getLoadValues()), true, sr.getQuantity());
				
					// Call the backtest Processing class...
					Method m= c.getDeclaredMethod("processSimulationRequests", SimulationRequest.class);
					Object ret= m.invoke(inst, sr);
					ArrayList<SimulationResponse> simulationResponses = (ArrayList<SimulationResponse>) ret;
					
					// Iterate over the expressions
					Iterator<SimulationResponse> it = simulationResponses.iterator();
					while (it.hasNext()) {
						SimulationResponse u = it.next();
						this.writeToDB(u);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				msg.acknowledge();
				
			} catch (JMSException | ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				
			}
			
			
		}
		
	
		private void writeToDB(SimulationResponse sr) {
			Double pnl = sr.getPnl();

			// Check if the profit exceeds the minimum. (Default 2)
			if (pnl > sr.getMinProfit()) {

				// Write to simulation table
				srd.addSimulationResponse(sr);
			}

		}

}
