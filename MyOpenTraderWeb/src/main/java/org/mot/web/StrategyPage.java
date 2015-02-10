package org.mot.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.mot.common.tools.PropertiesFactory;
import org.mot.web.dp.SortableStrategyDataProvider;
import org.mot.web.link.BookMarkableLink;
import org.mot.web.panel.LatestPricePanel;
import org.mot.web.panel.MenuPanel;
import org.mot.web.panel.StrategyPanel;
import org.mot.web.panel.Timestamp;

public class StrategyPage extends WebPage {

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
	public StrategyPage(final PageParameters parameters) {

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
			add(new StrategyPanel("strategyPanel"));
			
			
			// Data Table
			List<IColumn> columns = new ArrayList<IColumn>();
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("ID"), "ID"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Name"), "name"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Symbol"), "symbol"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Type"), "type"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Load Values"), "LoadValues"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Enabled"), "Enabled"));
			columns.add((IColumn<?, ?>) new PropertyColumn(new Model<String>("Timestamp"), "timestamp"));
			
			final SortableStrategyDataProvider ssdp = new SortableStrategyDataProvider();
			
			add(new DefaultDataTable("strategyTable", columns, ssdp, 8));
			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
