package org.mot.web.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;
import org.mot.common.db.DatabaseConnectionFactory;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.Tick;
import org.mot.common.objects.WatchList;
import org.mot.common.tools.TrendCalculator;

public class RealTimeTrendPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RealTimeTrendPanel(String id, final String symbol) {
		super(id);
	
		TickPriceDAO tpd = new TickPriceDAO();
		final int tickCount = tpd.getTickCount(symbol, "ASK");
		
		
		final List list = new ArrayList();
		list.add(tickCount / 100 * 10);
		list.add(tickCount / 100 * 25);
		list.add(tickCount / 100 * 50);
		list.add(tickCount / 100 * 75);
		list.add(tickCount);
		
		final CalculatorFactory cf = new CalculatorFactory();

		add(new Label("TotalCount", tickCount));
		add(new Label("Symbol", symbol));
		
	    add( new ListView("TrendList", list ) 
	    { 

				@Override
				protected void populateItem(ListItem item) {
					// TODO Auto-generated method stub
					int length = (int) item.getModelObject();
					
					//item.add(new Label("Age", length + "day: " +  new TrendCalculator(symbol, length, "1 day").getPriceDifferenceInPct()));
					item.add(new Label("Age", length + ": " +  cf.round((new TrendCalculator(symbol, length, "DOESNOTMATTER", "BID", false).getPriceDifferenceInPct()), 2)));
					
				} 
	    }); 
	    
	    // Historic Trend Panel does not need to refresh itself
	    add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(30)));
	    
	}
	
	
}
