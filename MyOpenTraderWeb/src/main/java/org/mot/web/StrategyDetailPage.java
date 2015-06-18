package org.mot.web;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.mot.common.tools.PropertiesFactory;
import org.mot.web.chart.StrategyDetailChart;
import org.mot.web.link.BookMarkableLink;
import org.mot.web.panel.FooterPanel;
import org.mot.web.panel.LatestPricePanel;
import org.mot.web.panel.MenuPanel;
import org.mot.web.panel.StrategyPanel;

public class StrategyDetailPage extends WebPage {

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

	@SuppressWarnings("rawtypes")
	public StrategyDetailPage(final PageParameters parameters) {

		PropertiesFactory pf = PropertiesFactory.getInstance();
		String pathToConfigDir = pf.getConfigDir();

		try {
			Configuration config = new PropertiesConfiguration(pathToConfigDir
					+ "/config.properties");
			
			String symbol = String.valueOf(parameters.get("symbol"));
			String strategy = String.valueOf(parameters.get("strategy"));
			this.setPageTitle(String.valueOf(parameters.get("pageTitle")));

			// Static content
			// Add the footer - this should be on each page!
			add(new FooterPanel("footer"));
			add(new Label("pageTitle", String.valueOf(parameters.get("pageTitle"))));
			add(new BookMarkableLink("HOME", HomePage.class, "MyOpenTraderHome"));
			add(new MenuPanel("menu"));
			add(new LatestPricePanel("pricePanel"));
			
			// Dynamic content
			add(new StrategyDetailChart("strategyDetailChart", symbol, strategy));
			
			// Data Table
			/*
			List<IColumn> columns = new ArrayList<IColumn>();
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>(
					"ID"), "ID"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>(
					"Name"), "name"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>(
					"Symbol"), "symbol"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>(
					"Type"), "type"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>(
					"Load Values"), "LoadValues"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>(
					"Enabled"), "Enabled"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>(
					"Timestamp"), "timestamp"));

			final SortableStrategyDataProvider ssdp = new SortableStrategyDataProvider();

			add(new DefaultDataTable("strategyTable", columns, ssdp, 8)); */

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
