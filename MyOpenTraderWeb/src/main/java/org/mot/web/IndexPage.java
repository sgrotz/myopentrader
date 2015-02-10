package org.mot.web;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.mot.common.tools.PropertiesFactory;
import org.mot.web.chart.HistoricChart;
import org.mot.web.chart.RealTimeTickChart;
import org.mot.web.chart.TodaysTickChart;
import org.mot.web.link.BookMarkableLink;
import org.mot.web.panel.LatestPricePanel;
import org.mot.web.panel.MenuPanel;
import org.mot.web.panel.Timestamp;

public class IndexPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pageTitle;
	
	public String getPageTitle() {
		return pageTitle; 
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	
	public IndexPage(final PageParameters parameters) {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		String pathToConfigDir = pf.getConfigDir();
		try {
			Configuration config = new PropertiesConfiguration(pathToConfigDir + "/config.properties");
		
			String symbol = String.valueOf(parameters.get("symbol"));
			
			// Charts
			add(new TodaysTickChart("todaysTicks", symbol, config.getInt("chart.todays.refreshMsec")));
			add(new RealTimeTickChart("realTimeChart", symbol, config.getInt("page.mvaStockPage.showHistoryTicks"), config.getInt("chart.realtime.refreshMsec")));
			add(new HistoricChart("histChart", symbol, config.getInt("page.mvaStockPage.showHistoryTicks"), 30));
				
			// Additional components
			this.setPageTitle(String.valueOf(parameters.get("pageTitle")));
			add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
			add(new Timestamp("TimeStamp"));
			add(new Label("pageTitle", String.valueOf(parameters.get("pageTitle"))));
	
			// Links and Panels
			add(new BookMarkableLink("HOME", HomePage.class, "MyOpenTraderHome"));
			add(new MenuPanel("menu"));
			add(new LatestPricePanel("pricePanel"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
