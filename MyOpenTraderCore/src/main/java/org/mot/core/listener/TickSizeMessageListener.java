

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
  
  
  package org.mot.core.listener;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.mot.common.conversion.TickConverter;
import org.mot.common.db.TickSizeDAO;
import org.mot.common.esper.EsperFactory;
import org.mot.common.objects.TickSize;



public class TickSizeMessageListener implements MessageListener {

	TickConverter tc = new TickConverter(); 
	EsperFactory ef = EsperFactory.getInstance();
	
	Logger logger = Logger.getLogger(getClass());
	
	@Override 
	public void onMessage(Message msg) {
		// TODO Auto-generated method stub
		
		BytesMessage message = (BytesMessage) msg;
		
		TickSizeDAO tsd = new TickSizeDAO();
		
			byte[] bytes;
			try {
				bytes = new byte[(int) message.getBodyLength()];
				message.readBytes(bytes);
				
				TickSize ts = TickSize.deserialize(bytes);
				
				// For some odd reason, it can happen that the first couple of messages are interrupted. Ignore them...
				// Will throw an java.io.StreamCorruptedException: invalid stream header: 00000000
				if (ts != null) {
					logger.trace("Start of processing tick: " + ts.getTimestamp());
					
					// Send volume event to Esper
					//ef.sendEsperEvent(ts);
					
					if (!ts.isReplay()) {
						// Add Tick to Database
						
						tsd.addTickSize(ts);
						
					}
					logger.trace("Finished processing tick: " + ts.getTimestamp());
				}
				
			} catch (Exception e) {
				
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
