package org.mot.web.panel;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.objects.Tick;
import org.mot.common.objects.WatchList;

public class LatestPricePanel extends Panel {

	public LatestPricePanel(String id) {
		super(id);

		
		WatchListDAO wld = new WatchListDAO();
		TickPriceDAO tp = new TickPriceDAO();
		
		WatchList[] watchList = wld.getWatchlistAsObject();
		Tick[] linkList = new Tick[watchList.length];
		
		for (int i = 0; i < watchList.length; i++) {
			linkList[i] = tp.getLastPriceForSymbolAsObject(watchList[i].getSymbol(), "LAST");
		}
		
		// Convert List to an array
		final List list = Arrays.asList(linkList); 
	    
	    add( new ListView<Tick>("tickList", list ) 
	    { 

				@Override
				protected void populateItem(ListItem item) {
					// TODO Auto-generated method stub
					Tick tick = (Tick) item.getModelObject();
					
					item.add(new Label("Symbol", tick.getSymbol() + "@" + tick.getPrice()));
					
				} 
	    }); 
	    
	    add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

	    
	}
	
	
}
