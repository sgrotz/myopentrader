package org.mot.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.mot.common.tools.PropertiesFactory;
import org.mot.web.chart.HistoricChart;
import org.mot.web.chart.RealTimeTickChart;
import org.mot.web.chart.TodaysTickChart;
import org.mot.web.dp.SortableClosedOrderDataProvider;
import org.mot.web.dp.SortableOrderDataProvider;
import org.mot.web.dp.SortableStrategyDataProvider;
import org.mot.web.link.BookMarkableLink;
import org.mot.web.panel.HistoricTrendPanel;
import org.mot.web.panel.LatestPricePanel;
import org.mot.web.panel.MenuPanel;
import org.mot.web.panel.RealTimeTrendPanel;
import org.mot.web.panel.StaticDataPanel;
import org.mot.web.panel.Timestamp;

public class OrderPage extends WebPage {

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
	public OrderPage(final PageParameters parameters) {
		PropertiesFactory pf = PropertiesFactory.getInstance();
		String pathToConfigDir = pf.getConfigDir();
		try {
			Configuration config = new PropertiesConfiguration(pathToConfigDir + "/config.properties");
			String symbol = String.valueOf(parameters.get("symbol"));
			this.setPageTitle(String.valueOf(parameters.get("pageTitle")));

			// Static content
			add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
			add(new Timestamp("TimeStamp"));
			add(new Label("pageTitle", String.valueOf(parameters.get("pageTitle"))));
			add(new BookMarkableLink("HOME", HomePage.class, "MyOpenTraderHome"));
			add(new MenuPanel("menu"));
			add(new LatestPricePanel("pricePanel"));
			
			
			// Data Table
			List<IColumn> columns = new ArrayList<IColumn>();
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Buy/Sell"), "BUYSELL"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Quantity"), "quantity"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Price"), "price"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Symbol"), "symbol"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Strategy"), "strategy"));

			String strategy = String.valueOf(parameters.get("symbol"));
			final SortableOrderDataProvider ssdp;
			
			if (strategy == null) {
				// If no strategy is provided - return all orders
				 ssdp = new SortableOrderDataProvider();
			} else {
				// If searching for a particular strategy, only return those orders
				 ssdp = new SortableOrderDataProvider(strategy);
			}
				
			add(new DefaultDataTable("orderTable", columns, ssdp, 20));
			
			
			
			List<IColumn> columns2 = new ArrayList<IColumn>();
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Symbol"), "symbol"));
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Quantity"), "quantity"));
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Buy Price"), "buyPrice"));
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Sell Price"), "sellPrice"));
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Gross PNL"), "grossPNL"));
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Txn Cost"), "txnCost"));
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Net PNL"), "netPNL"));
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Percentage"), "pctPNL"));
			columns2.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Strategy"), "strategy"));
			
			DefaultDataTable table = new DefaultDataTable("closedOrderTable", columns2, new SortableClosedOrderDataProvider(), 20);
					
			add(table);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
