

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
  
  
  package org.mot.feeder.iab.listener;

import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.mot.common.objects.Order;
import org.mot.feeder.iab.client.Contract;
import org.mot.feeder.iab.client.EClientSocket;
import org.mot.feeder.iab.wrapper.IABConnector;



public class OrderMessageListener implements MessageListener {


	
	Logger logger = Logger.getLogger(getClass());
	
	@Override 
	public void onMessage(Message msg) {
		// TODO Auto-generated method stub
		
		BytesMessage message = (BytesMessage) msg;
		
		
		byte[] bytes;
		try {
			bytes = new byte[(int) message.getBodyLength()];
			message.readBytes(bytes);
			
			Order order = Order.deserialize(bytes);
			
			logger.trace("Start of processing order: " + order.getTimestamp());
			
			// Place order on exchange!
			EClientSocket ecs = IABConnector.getInstance();
			
			// Create a new contract
			Contract con = new Contract();
			
			// Default the currency to USD
			con.m_currency = "USD";
			con.m_secType = "STK";
			con.m_exchange = "SMART";
			con.m_symbol = order.getSymbol();
			
			int orderID = Integer.valueOf(order.getID());
			
			org.mot.feeder.iab.client.Order iabOrder = new org.mot.feeder.iab.client.Order();
			iabOrder.m_action = order.getBUYSELL();
			
			iabOrder.m_orderType = "MKT";
			iabOrder.m_orderId = orderID;
			
			// AS A PRECAUTION - OVERWRITE THE QUANTITY BY DEFAULT TO 1
			iabOrder.m_totalQuantity = 1;
			//iabOrder.m_totalQuantity = order.getQuantity();
			
			ecs.placeOrder(orderID, con, iabOrder);
			
			logger.trace("Finished processing order: " + order.getTimestamp());
			
		} catch (JMSException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				msg.acknowledge();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
