package org.mot.web.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.mot.common.db.OrderDAO;
import org.mot.common.db.WatchListDAO;
import org.mot.common.objects.Order;
import org.mot.common.objects.WatchList;

public class SortableWatchlistDataProvider extends
		SortableDataProvider<Object, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5531187340179821367L;

	private List<WatchList> list = new ArrayList<WatchList>();
	private static WatchListDAO wld = new WatchListDAO();

	public SortableWatchlistDataProvider() {

		WatchList[] w = wld.getWatchlistAsObject();

		list = Arrays.asList(w);
	}

	@Override
	public Iterator<?> iterator(long first, long count) {

		return list.subList((int) first, (int) (first + count)).iterator();
	}

	public IModel<Object> model(final Object object) {
		return new AbstractReadOnlyModel<Object>() {
			@Override
			public Object getObject() {
				return object;
			}
		};
	}

	public long size() {
		return list.size();
	}

}
