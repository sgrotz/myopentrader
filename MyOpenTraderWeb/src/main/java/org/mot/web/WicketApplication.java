package org.mot.web;

import javax.servlet.ServletContext;

import org.apache.log4j.PropertyConfigurator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.mot.common.tools.PropertiesFactory;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see org.sg.iab.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
	
		super.init();
		
		ServletContext context = this.getServletContext(); 
		System.out.println("Using Config directory: " + context.getRealPath("/WEB-INF/conf"));
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		pf.setConfigDir(context.getRealPath("/WEB-INF/conf"));
		
		PropertyConfigurator.configure(pf.getConfigDir()+ "/log4j.properties");

	}
	

}
