package org.mot.web;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.mot.common.tools.PropertiesFactory;
import org.mot.web.chart.HistoricChart;
import org.mot.web.chart.RealTimeTickChart;
import org.mot.web.chart.TickRangeChart;
import org.mot.web.chart.TodaysTickChart;
import org.mot.web.link.BookMarkableLink;
import org.mot.web.panel.FooterPanel;
import org.mot.web.panel.HistoricTrendPanel;
import org.mot.web.panel.LatestPricePanel;
import org.mot.web.panel.MenuPanel;
import org.mot.web.panel.RealTimeTrendPanel;
import org.mot.web.panel.StaticDataPanel;
import org.mot.web.panel.StrategyPanel;

public class StockPage extends WebPage {

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

	public StockPage(final PageParameters parameters) {

		PropertiesFactory pf = PropertiesFactory.getInstance();
		String pathToConfigDir = pf.getConfigDir();
		try {
			Configuration config = new PropertiesConfiguration(pathToConfigDir
					+ "/config.properties");

			// Static context
			String symbol = String.valueOf(parameters.get("symbol"));
			this.setPageTitle(String.valueOf(parameters.get("pageTitle")));

			// Add the footer - this should be on each page!
			add(new FooterPanel("footer"));

			add(new Label("pageTitle", String.valueOf(parameters
					.get("pageTitle"))));
			add(new BookMarkableLink("HOME", HomePage.class, "MyOpenTraderHome"));
			add(new MenuPanel("menu"));

			// Dynamic Content
			add(new TodaysTickChart("todaysTicks", symbol,
					config.getInt("chart.todays.refreshMsec")));

			add(new TickRangeChart("scatterTicks", symbol,
					config.getInt("chart.todays.refreshMsec")));
			add(new RealTimeTickChart("realTimeChart", symbol,
					config.getInt("page.mvaStockPage.showLastTicks"),
					config.getInt("chart.realtime.refreshMsec")));
			add(new HistoricChart("histChart", symbol,
					config.getInt("page.mvaStockPage.showHistoryTicks"), 30));

			// Panels
			add(new RealTimeTrendPanel("trendPanel", symbol));
			add(new HistoricTrendPanel("historicTrendPanel", symbol));
			add(new StaticDataPanel("staticData", symbol));
			add(new LatestPricePanel("pricePanel"));
			add(new StrategyPanel("strategyPanel", symbol));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
