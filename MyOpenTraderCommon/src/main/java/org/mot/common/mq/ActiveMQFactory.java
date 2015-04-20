

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


package org.mot.common.mq;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mot.common.objects.Order;
import org.mot.common.objects.SimulationRequest;
import org.mot.common.objects.Tick;
import org.mot.common.objects.TickHistory;
import org.mot.common.objects.TickSize;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQFactory { 

	private static ActiveMQFactory instance = null;
	private static Connection connection;
	private Session session;
	private Configuration config;
	PropertiesFactory pf = PropertiesFactory.getInstance();
	String pathToConfigDir = pf.getConfigDir();
	Logger logger = LoggerFactory.getLogger(getClass());


	public ActiveMQFactory() {
		connect();
	}

	public static ActiveMQFactory getInstance() {
		if (instance == null) {
			return new ActiveMQFactory();
		} else {
			return instance;
		}
	}

	private void connect(){
		// Get the cache configuration
		int count = 0;
		int max = 5;
		long sleep = 2000;

		while (true) {
			try {
				config = new PropertiesConfiguration(pathToConfigDir + "/activemq.properties");

				String wireformat = config.getString("activemq.wireFormat");
				ActiveMQConnectionFactory connectionFactory = null; 
				String url = config.getString("activemq.connection.URL");
				String user = config.getString("activemq.user");
				String password = config.getString("activemq.password");

				if (wireformat != null) {
					// Create new connectionFactory with wireFormat specification
					//connectionFactory = new PooledConnectionFactory(config.getString("activemq.connectionFactory") + "?" + wireformat);
					connectionFactory = new ActiveMQConnectionFactory(url + "?" + wireformat);
				} else {
					// Create new connectionFactory 
					connectionFactory = new ActiveMQConnectionFactory(url);
				}

				// Set the connection Factory properties
				/* connectionFactory.setMaxConnections(10000);
			connectionFactory.setIdleTimeout(120);
			connectionFactory.initConnectionsPool();
			connectionFactory.start(); */

				// Create a Connection
				// Need to investigate a bit further, if the session should be async. if false, performance is better - but not sure
				// this can be used for each queue/topic. May need to move this into a session instead of globally in the connection!
				connectionFactory.setAlwaysSessionAsync(false);
				connectionFactory.setDispatchAsync(false);
				connectionFactory.setMaxThreadPoolSize(300);

				if (user == null || password == null) {
					// Dont provide credentials to connectionFactory if user or pwd are empty
					connection = connectionFactory.createConnection();
				} else {
					connection = connectionFactory.createConnection(user,password);
				}
				connection.start();

				// Create a default session object
				this.session = this.createSession();	
				logger.debug("Started Active MQ connection");

			} catch (ConfigurationException e) {
				// If the configuration is wrong, exit immediately 
				System.out.println("*** Cannot connect to Message Broker - Invalid configuration - Exiting MyOpenTrader ...");
				e.printStackTrace();
				System.exit(0);

			} catch (JMSException e) {
				// Only exit if we have tried for 3 times, with a pause of 2 seconds in between!
				if (++count == max) {
					System.out.println("*** Failed to connect to Message Broker - Exiting MyOpenTrader ...");
					e.printStackTrace();
					System.exit(0);
				} else {
					try {
						// Make sure the connection is closed, before retrying to create a new one. 
						connection.close();
						
						System.out.println("*** Cannot connect to Message Broker - Retry " + count + " of " + max + "...");
						e.printStackTrace();
						Thread.sleep(sleep);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JMSException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} 
			// If everything went well, break the while loop. 
			break;
		}
	}


	/**
	 * @param sessionID
	 * @return
	 * @throws JMSException
	 */
	private Session createSession() throws JMSException {	

		// Create a Session
		return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	/**
	 * Method for closing the session ... make sure to call each time a session is opened
	 * 
	 * @param session
	 */
	private void closeSession(Session session) {
		try {
			session.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	/**
	 * Use this method to create a new destination by name
	 * 
	 * @param name - Name of the destination queue
	 * @return - Destination object
	 */
	public Destination createDestination(String name) {
		Destination dest = null;
		try {	
			dest = session.createQueue(name);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dest;		
	}


	/**
	 * Create a new message producer onto a particular destination
	 * 
	 * @param destination - destination queue/topic object
	 * @return the message producer object
	 */
	public MessageProducer createMessageProducer(String channel, long ttl, boolean persistent) {
		Destination destination = createDestination(channel);
		MessageProducer msg = null;

		try {
			msg = session.createProducer(destination);

			msg.setTimeToLive(ttl);

			if (persistent) {
				msg.setDeliveryMode(DeliveryMode.PERSISTENT);
			} else {
				msg.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}	

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}


	public void closeMessageProducer(MessageProducer mp) {

		try {
			mp.close();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Use this method to create a message consumer object. 
	 * 
	 * @param source - provide a source as destination object 
	 * @param messageProcessor - provide a message listener interface for the message processing
	 */
	public void createMessageConsumer(Destination source, MessageListener messageProcessor) {
		try {
			MessageConsumer msg = session.createConsumer(source);
			msg.setMessageListener(messageProcessor);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Use this method to create a message consumer object. 
	 * 
	 * @param source - provide a source as destination object 
	 * @param messageProcessor - provide a message listener interface for the message processing
	 * @param symbol - provide the symbol
	 */
	public void createMessageConsumer(final Destination source, final MessageListener messageProcessor, final String symbol) {

		try {
			MessageConsumer msg = session.createConsumer(source, "Symbol = '" + symbol + "'");
			msg.setMessageListener(messageProcessor);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param source
	 * @param messageProcessor
	 * @param symbol
	 * @param currency
	 */
	public void createMessageConsumer(final Destination source, final MessageListener messageProcessor, final String symbol, final String currency) {

		try {
			MessageConsumer msg = session.createConsumer(source, "Symbol = '" + symbol + "' AND Currency ='" + currency + "'");
			msg.setMessageListener(messageProcessor);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @param tick
	 * @param ttl
	 * @param persistent
	 */
	public void publishTick(Tick tick, MessageProducer mp) {


		BytesMessage tickMessage;
		try {
			Session session = this.createSession();

			// Create a new ByteMessage
			tickMessage = session.createBytesMessage();

			// Serialize the tick content into the message
			tickMessage.writeBytes(tick.serialize());
			tickMessage.setStringProperty("Symbol", tick.getSymbol());
			tickMessage.setStringProperty("Currency", tick.getCurrency());

			mp.send(tickMessage);
			this.closeSession(session);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param tick
	 * @param ttl
	 * @param persistent
	 */
	public void publishTicks(Tick[] tick, MessageProducer mp) {

		BytesMessage tickMessage;
		try {
			Session session = this.createSession();

			for (int i = 0; i < tick.length; i++ ) {
				// Create a new ByteMessage
				tickMessage = session.createBytesMessage();

				// Serialize the tick content into the message
				tickMessage.writeBytes(tick[i].serialize());
				tickMessage.setStringProperty("Symbol", tick[i].getSymbol());
				tickMessage.setStringProperty("Currency", tick[i].getCurrency());

				mp.send(tickMessage);
			}
			this.closeSession(session);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * @param tickSize
	 * @param ttl
	 * @param persistent
	 */
	public void publishTickSize(TickSize tickSize, MessageProducer mp) {

		BytesMessage tickMessage;
		try {
			Session session = this.createSession();

			// Create a new ByteMessage
			tickMessage = session.createBytesMessage();

			// Serialize the tick content into the message
			tickMessage.writeBytes(tickSize.serialize());
			tickMessage.setStringProperty("Symbol", tickSize.getSymbol());

			mp.send(tickMessage);
			this.closeSession(session);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * @param order - Order to be placed
	 * @param ttl - Time to live. Set to 0 for indefinite
	 * @param deliveryMode - Set to 1 for non-persistent. Default 0
	 */
	public void publishOrder(Order order, MessageProducer mp) {
		BytesMessage orderMessage;
		try {
			Session session = this.createSession();

			// Create a new ByteMessage
			orderMessage = session.createBytesMessage();

			// Serialize the tick content into the message
			orderMessage.writeBytes(order.serialize());
			orderMessage.setStringProperty("Symbol", order.getSymbol());

			mp.send(orderMessage);
			this.closeSession(session);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Publish a simulation request
	 * 
	 * @param sr
	 * @param ttl
	 * @param persistent
	 */
	public void publishSimulationRequest(SimulationRequest sr, MessageProducer mp) {

		BytesMessage simulationMessage;
		try {
			Session session = this.createSession();

			// Create a new ByteMessage
			simulationMessage = session.createBytesMessage();

			// Serialize the simulation request content into the message
			simulationMessage.writeBytes(sr.serialize());
			simulationMessage.setStringProperty("Symbol", sr.getSymbol());
			simulationMessage.setStringProperty("LoadValues", String.valueOf(sr.getLoadValues()));
			simulationMessage.setStringProperty("StartDate", String.valueOf(sr.getStartDate()));
			simulationMessage.setStringProperty("EndDate", String.valueOf(sr.getEndDate()));

			mp.send(simulationMessage);
			this.closeSession(session);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * Publish a tick history object
	 * @param tick
	 */
	public void publishTickHistory(TickHistory tick, MessageProducer mp) {

		BytesMessage tickMessage;
		try {
			Session session = this.createSession();

			// Create a new ByteMessage
			tickMessage = session.createBytesMessage();

			// Serialize the tick content into the message
			tickMessage.writeBytes(tick.serialize());
			mp.send(tickMessage);
			this.closeSession(session);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	public static void main(String[] args) {
		System.setProperty("IACPathToConfigDir", args[0]);

		ActiveMQFactory mq = ActiveMQFactory.getInstance();
		mq.createDestination("test");




	}

}
