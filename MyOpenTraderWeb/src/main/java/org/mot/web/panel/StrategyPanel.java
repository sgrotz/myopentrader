package org.mot.web.panel;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.StrategyAnalysis;
import org.mot.common.tools.PropertiesFactory;
import org.mot.common.util.StrategyAnalyser;

public class StrategyPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4499844720657306704L;
	CalculatorFactory cf = new CalculatorFactory();

	private static PropertiesFactory pf = PropertiesFactory.getInstance();
	private String pathToConfigDir = pf.getConfigDir();
	private static Configuration config;
	
	/**
	 * The panel only shows the strategies for a particular symbol. This is embedded in the stock page.
	 * @param id
	 * @param symbol
	 */
	public StrategyPanel(String id, final String symbol) {
		super(id);
		

		try {
			config = new PropertiesConfiguration(pathToConfigDir + "/config.properties");
			StrategyAnalyser sa = new StrategyAnalyser();
			
		    add( new ListView<StrategyAnalysis>("strategyList", sa.analyseBySymbol(symbol) ) 
		    { 
	
					@Override
					protected void populateItem(ListItem item) {
						// TODO Auto-generated method stub
						StrategyAnalysis sa = (StrategyAnalysis) item.getModelObject();
						
						//Double txCost = cf.round((sa.getQuantity() * sa.getPrice() * config.getDouble("order.transactionCostPerOrder.pct") * sa.getTradeCount()), 2);
						Double txCost = cf.round(sa.getTxnCost(), 2);
						Double pnl = cf.round(sa.getPnL() - txCost, 2) ;
						Double ept = cf.round((sa.getPnL() - sa.getTradeCount()) /sa.getTradeCount(),2);
						Double eps = cf.round(sa.getPnL()/sa.getQuantity(),2);

						item.add(new Label("Strategy", "Strategy: " + sa.getName() + "@" + sa.getTradeCount() + " trades - P/L: $" + pnl
								+ " (TxnCost: $" + txCost  + " - EarningsPerTrade: $" 
								+  ept + " - Shares: " + sa.getQuantity() + " - EarningsPerShare: $"+ eps +")") );
						
					} 
		    }); 
	    
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(60)));

	    
	}
	
	
	/**
	 * Use this panel for a generic overview - shows all strategies
	 * @param id
	 */
	public StrategyPanel(String id) { 
		super(id);
		
		try {
			config = new PropertiesConfiguration(pathToConfigDir + "/config.properties");
	
			StrategyAnalyser sa = new StrategyAnalyser();
			
		    add( new ListView<StrategyAnalysis>("strategyList", sa.analyseAll() ) 
		    { 
	
					@Override
					protected void populateItem(ListItem item) {
						// TODO Auto-generated method stub
						StrategyAnalysis sa = (StrategyAnalysis) item.getModelObject();
						
						Double txCost = cf.round((sa.getQuantity() * sa.getPrice() * config.getDouble("order.transactionCostPerOrder.pct") * sa.getTradeCount()), 2);
						Double pnl = cf.round(sa.getPnL() - txCost, 2) ;
						Double ept = cf.round((sa.getPnL() - sa.getTradeCount()) /sa.getTradeCount(),2);
						Double eps = cf.round(sa.getPnL()/sa.getQuantity(),2);

						item.add(new Label("Strategy", "Strategy: " + sa.getName() + "@" + sa.getTradeCount() + " trades - P/L: $" + pnl
								+ " (TxnCost: $" + txCost  + " - EarningsPerTrade: $" 
								+  ept + " - Shares: " + sa.getQuantity() + " - EarningsPerShare: $"+ eps +")") );
						
					} 
		    }); 
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(60)));

	    
	}
	
	
	
}
