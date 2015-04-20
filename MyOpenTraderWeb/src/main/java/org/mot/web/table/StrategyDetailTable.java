package org.mot.web.table;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;

public class StrategyDetailTable <T, S> extends DataTable<T, S>{

	public StrategyDetailTable(String id, List<? extends IColumn<T, S>> columns, IDataProvider<T> dataProvider, long rowsPerPage) {
		super(id, columns, dataProvider, rowsPerPage);
		// TODO Auto-generated constructor stub
	}

}
