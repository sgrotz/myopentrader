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

public class SortableOrderDataProvider extends SortableDataProvider<Object, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5531187340179821367L;

    
    private List<Order> list = new ArrayList<Order>();
    private static     	OrderDAO od = new OrderDAO();
 	
    
    
    public SortableOrderDataProvider() {
    	
    	Order[] s = od.getAllOrders();
    	list = Arrays.asList(s);
     }
    
    public SortableOrderDataProvider(String symbol) {
    	
    	Order[] s = od.getAllOrdersByStrategy(symbol);
    	list = Arrays.asList(s);
     }

    
    
	@Override
	public Iterator<?> iterator(long first, long count) {

        return list.subList((int) first, (int) (first + count)).iterator();
	}

    public IModel<Object> model(final Object object) {
        return new AbstractReadOnlyModel<Object>() {
            @Override
            public Object getObject() {
                return  object;
            }
        };
    }
 
    public long size() {
        return list.size();
    }

}
