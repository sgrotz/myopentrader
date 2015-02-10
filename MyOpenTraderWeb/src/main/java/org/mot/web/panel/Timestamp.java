package org.mot.web.panel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

public class Timestamp extends Panel {

	public Timestamp(String id) {
		super(id);
		// TODO Auto-generated constructor stub
		
		// Server time Label
		Label time = new Label("time", new Model() {
		

			private static final long serialVersionUID = -4992940551255653329L;

			@Override
		    public Serializable getObject() {
		    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    	java.util.Date date = new java.util.Date();
		        return dateFormat.format(date);
		    }
			
		}); 
		time.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));
		add(time);
	}
	
	
}
