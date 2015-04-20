package org.mot.web.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.mot.common.db.WatchListDAO;
import org.mot.common.objects.WatchList;
import org.mot.web.IndexPage;
import org.mot.web.OrderPage;
import org.mot.web.StockPage;
import org.mot.web.StrategyPage;
import org.mot.web.WatchlistPage;
import org.mot.web.link.BookMarkableLink;

public class MenuPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3109842410185582563L;

	public MenuPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub

		// Add static links
		add(new BookMarkableLink("strategy", StrategyPage.class,
				"All Strategies"));
		add(new BookMarkableLink("orders", OrderPage.class, "All Orders"));
		add(new BookMarkableLink("watchlist", WatchlistPage.class, "Watchlist"));

		// Dynamic links below:
		WatchListDAO wld = new WatchListDAO();

		WatchList[] watchList = wld.getWatchlistAsObject();

		// Convert List to an array
		final List<BookMarkableLink> stkList = new ArrayList<BookMarkableLink>();
		final List<BookMarkableLink> etfList = new ArrayList<BookMarkableLink>();
		final List<BookMarkableLink> indList = new ArrayList<BookMarkableLink>();
		final List<BookMarkableLink> fxList = new ArrayList<BookMarkableLink>();

		for (int i = 0; i < watchList.length; i++) {
			if (watchList[i].getType().equals("STK")) {
				stkList.add(new BookMarkableLink("Link", StockPage.class,
						watchList[i].getName(), watchList[i].getSymbol(),
						watchList[i].getName() + " details", "StockDetails",
						watchList[i].getName(), "LinkName", watchList[i]
								.getName()));
			} else if (watchList[i].getType().equals("IND")) {
				indList.add(new BookMarkableLink("Link", IndexPage.class,
						watchList[i].getName(), watchList[i].getSymbol(),
						watchList[i].getName() + " details", "IndexDetails",
						watchList[i].getName(), "LinkName", watchList[i]
								.getName()));
			} else if (watchList[i].getType().equals("FX")) {
				fxList.add(new BookMarkableLink("Link", IndexPage.class,
						watchList[i].getName(), watchList[i].getSymbol(),
						watchList[i].getName() + " details", "ForexDetails",
						watchList[i].getName(), "LinkName", watchList[i]
								.getName()));
			} else {
				// Must be an ETF then ;)
				etfList.add(new BookMarkableLink("Link", IndexPage.class,
						watchList[i].getName(), watchList[i].getSymbol(),
						watchList[i].getName() + " details", "ETFDetails",
						watchList[i].getName(), "LinkName", watchList[i]
								.getName()));
			}
		}

		add(new ListView<BookMarkableLink>("listview", stkList) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
				BookMarkableLink link = (BookMarkableLink) item
						.getModelObject();

				// item.add(new Label("LinkName", link.getStock()));
				item.add((BookMarkableLink) stkList.get(item.getIndex()));

			}
		});

		add(new ListView<BookMarkableLink>("indexview", indList) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
				BookMarkableLink link = (BookMarkableLink) item
						.getModelObject();

				// item.add(new Label("LinkName", link.getStock()));
				item.add((BookMarkableLink) indList.get(item.getIndex()));

			}
		});

		add(new ListView<BookMarkableLink>("etfview", etfList) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
				BookMarkableLink link = (BookMarkableLink) item.getModelObject();

				// item.add(new Label("LinkName", link.getStock()));
				item.add((BookMarkableLink) etfList.get(item.getIndex()));

			}
		});

		add(new ListView<BookMarkableLink>("fxview", fxList) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
				BookMarkableLink link = (BookMarkableLink) item
						.getModelObject();

				// item.add(new Label("LinkName", link.getStock()));
				item.add((BookMarkableLink) fxList.get(item.getIndex()));

			}
		});

	}

}
