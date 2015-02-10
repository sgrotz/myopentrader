package org.mot.web.dp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.mot.common.db.StrategyDAO;
import org.mot.common.objects.Strategy;

public class SortableStrategyDataProvider extends SortableDataProvider<Object, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5531187340179821367L;

    class SortableDataProviderComparator implements Comparator<Strategy>, Serializable {
        public int compare(final Strategy o1, final Strategy o2) {
            PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, o1.getID());
            PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, o2.getID());
 
            int result = model1.getObject().compareTo(model2.getObject());
 
            if (!getSort().isAscending()) {
                result = -result;
            }
 
            return result;
        }
 
    }
    
    private List<Strategy> list = new ArrayList<Strategy>();
    private SortableDataProviderComparator comparator = new SortableDataProviderComparator();
	
	
    public SortableStrategyDataProvider() {
        // The default sorting
        //setSort("name.first", true);
    	
    	StrategyDAO sd = new StrategyDAO();
    	Strategy[] s = sd.getAllStrategiesAsObject();
    	
    	list = Arrays.asList(s);
 
        //(String iD, String name, String type, String symbol, String loadValues, boolean enabled)
        //list.add(new Strategy("99", "Test", "MovingAverage", "FB", "15;77", true));
        //list.add(new Strategy("99", "Test", "Mo2vingAverage", "FB", "15;77", true));

    }
    
    
	@Override
	public Iterator<?> iterator(long first, long count) {
        // Get the data
        List<Strategy> newList = new ArrayList<Strategy>(list);
 
        // Sort the data
        //Collections.sort(newList, comparator);
 
        // Return the data for the current page - this can be determined only after sorting
        return newList.iterator();
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
