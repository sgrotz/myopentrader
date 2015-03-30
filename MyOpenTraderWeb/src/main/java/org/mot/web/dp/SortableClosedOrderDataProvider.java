package org.mot.web.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.mot.common.db.OrderDAO;
import org.mot.common.objects.Order;
import org.mot.web.view.OrderView;

public class SortableClosedOrderDataProvider extends
		SortableDataProvider<Object, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5531187340179821367L;

	private List<OrderView> list = new ArrayList<OrderView>();
	private static OrderDAO od = new OrderDAO();

	public SortableClosedOrderDataProvider() {

		Order[] o = od.getAllClosedOrders();
		OrderView[] ov = new OrderView[o.length];

		for (int i = 0; i < o.length; i++) {
			int quantity = o[i].getQuantity();
			Double sellPrice = o[i].getPrice();
			Double buyPrice = od.getOrderAmount(o[i].getClosed());
			Double grossPNL = (sellPrice * quantity) - (buyPrice * quantity);
			Double txnCost = ((sellPrice * quantity) + (buyPrice * quantity)) * 0.0024;
			Double netPNL = grossPNL - txnCost;
			Double pctPNL = (netPNL / grossPNL * 100);

			OrderView os = new OrderView();
			os.setSellPrice(sellPrice);
			os.setBuyPrice(buyPrice);
			os.setQuantity(quantity);
			os.setStrategy(o[i].getStrategy());
			os.setSymbol(o[i].getSymbol());
			os.setGrossPNL(grossPNL);
			os.setTxnCost(txnCost);
			os.setNetPNL(netPNL);
			os.setPctPNL(pctPNL);

			ov[i] = os;
		}

		list = Arrays.asList(ov);
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
