package org.mot.web.panel;

import java.util.ArrayList;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.mot.common.db.TickHistoryDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.web.StockPage;
import org.mot.web.link.BookMarkableLink;

public class OverviewTrendPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OverviewTrendPanel(String id) {
		super(id);

		WatchListDAO wld = new WatchListDAO();
		final TickHistoryDAO thd = new TickHistoryDAO();
		ArrayList<String> symbolList = wld.getWatchlist("STK");

		add(new ListView("OverviewTrendList", symbolList) {

			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
				String symbol = (String) item.getModelObject();

				// item.add(new Label("ticker", symbol));
				item.add(new BookMarkableLink("symbolLink", StockPage.class,
						symbol, symbol, symbol + " details", "StockDetails",
						symbol, "ticker", symbol));

				item.add(new Label("ClosePrice", thd
						.getLastClosingPriceForStock(symbol)));

				item.add(new HistoricTrendPanel("historicTrendPanel", symbol));

			}
		});

		// Historic Trend Panel does not need to refresh itself
		// add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(60)));

	}

}
