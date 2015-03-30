package org.mot.web.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class FooterPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3109842410185582563L;

	public FooterPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub

		// Add new timestamp in the footer
		add(new Timestamp("TimeStamp"));
		add(new Label("version", getApplication().getFrameworkSettings()
				.getVersion()));

		String version = System.getProperty("MyOpenTraderWebVersion");
		add(new Label("webVersion", version));

	}

}
