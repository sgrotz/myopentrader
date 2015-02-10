

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

import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.mot.common.conversion.TickConverter;
import org.mot.common.db.TickHistoryDAO;
import org.mot.common.db.TickHistoryRequestsDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.objects.TickHistory;



public class TickHistoryMessageListener implements MessageListener {


	TickHistoryDAO thd = new TickHistoryDAO();
	TickHistoryRequestsDAO thrd = new TickHistoryRequestsDAO();
	WatchListDAO wld = new WatchListDAO();
	TickConverter tc = new TickConverter();


	@Override
	public void onMessage(Message msg) {
		// TODO Auto-generated method stub

		BytesMessage message = (BytesMessage) msg;

		byte[] bytes;
		try {
			bytes = new byte[(int) message.getBodyLength()];
			message.readBytes(bytes);

			TickHistory tick = TickHistory.deserialize(bytes);


			thd.addNewTickHistory(thrd.getTickHistoryRequestStock(String.valueOf(tick.getReqID())), thrd.getTickHistoryRequestValues(String.valueOf(tick.getReqID())), 
					tick.getDate(), tick.getOpen(), tick.getHigh(), tick.getLow(), tick.getClose(), tick.getVolume(), tick.getCount());


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
