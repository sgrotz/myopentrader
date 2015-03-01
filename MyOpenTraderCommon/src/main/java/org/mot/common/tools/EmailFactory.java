
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

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mot.common.objects.StrategyAnalysis;
import org.mot.common.util.DateBuilder;
import org.mot.common.util.StrategyAnalyser;

public class EmailFactory {

	private PropertiesFactory pf = PropertiesFactory.getInstance();
	private String from;
	private String rcpt;
	private boolean enabled;
	private String host;
	private Session session;

	public EmailFactory() {

		Configuration emailProps;
		try {
			emailProps = new PropertiesConfiguration(pf.getConfigDir() + "/email.properties");

			enabled = emailProps.getBoolean("email.enabled", true);
			from = emailProps.getString("email.sender", "info@myopentrader.org");
			rcpt = emailProps.getString("email.recipient", "stephan@myopentrader.org");
			host = emailProps.getString("email.smtp.host", "localhost");
			
			// Get system properties
			Properties properties = System.getProperties();

			// Setup mail server
			properties.setProperty("mail.smtp.host", host);
			
			session = Session.getDefaultInstance(properties);

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void sendEmail(String recipient, String subject, String body) {

		if (enabled) {

			try {
				
				if (recipient == null || recipient == "") {
					recipient = rcpt;
				}
	
				// Create a default MimeMessage object.
				Message message = new MimeMessage(session);
	
				// Set From: header field of the header.
				message.setFrom(new InternetAddress(from));
	
				// Set To: header field of the header.
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(recipient));
	
				// Set Subject: header field
				message.setSubject(subject);
	
				// Send the actual HTML message, as big as you like
				message.setText(body);
	
				// Send message
				Transport.send(message);
				//System.out.println("Sent message successfully....");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Get Properties Instance
		PropertiesFactory pf = PropertiesFactory.getInstance();

		// First make sure to set the config directory
		pf.setConfigDir(args[0]);

		EmailFactory ef = new EmailFactory();
		
		StrategyAnalyser sa = new StrategyAnalyser();
		ArrayList<StrategyAnalysis> salist = sa.analyseAll();
		
		String mailBody = sa.getAnalysisAsString(salist);
		String date = new DateBuilder().getTimeStampWithPattern("yyyy-MM-dd");
		
		ef.sendEmail(null, "Daily Trade Report for " + date, mailBody);	

		
		

	}

}
