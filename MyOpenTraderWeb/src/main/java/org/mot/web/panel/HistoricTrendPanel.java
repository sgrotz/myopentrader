package org.mot.web.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.tools.TrendCalculator;

public class HistoricTrendPanel extends Panel {

	public HistoricTrendPanel(String id, final String symbol) {
		super(id);
	
		final List list = new ArrayList();
		list.add(1);
		list.add(2);
		list.add(5);
		list.add(10);
		list.add(50);
		list.add(100);
		list.add(200);
		
		final CalculatorFactory cf = new CalculatorFactory();
		
	    add( new ListView("HistoricTrendList", list ) 
	    { 

				@Override
				protected void populateItem(ListItem item) {
					// TODO Auto-generated method stub
					int length = (int) item.getModelObject();
					
					//item.add(new Label("Age", length + "day: " +  new TrendCalculator(symbol, length, "1 day").getPriceDifferenceInPct()));
					item.add(new Label("Age", length + ": " +  cf.round((new TrendCalculator(symbol, length, "1 day", "BID", true).getPriceDifferenceInPct()),2)));
					
				} 
	    }); 
	    
	    // Historic Trend Panel does not need to refresh itself
	    // add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(60)));
	    
	}
	
	
}
