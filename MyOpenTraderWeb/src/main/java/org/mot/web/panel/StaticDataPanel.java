package org.mot.web.panel;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;
import org.mot.common.db.StaticDataDAO;
import org.mot.common.objects.StaticData;

public class StaticDataPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5674963164250781112L;

	public StaticDataPanel(String id, String symbol) {
		super(id);
		// TODO Auto-generated constructor stub

		StaticDataDAO sdd = new StaticDataDAO();
		
		StaticData sd = sdd.getLastStaticDataAsObject(symbol);
		
		add(new Label("range", sd.getRange()));
		add(new Label("yearrange", sd.getYearRange()));
		add(new Label("open", sd.getOpen()));
		add(new Label("volavg", sd.getVolavg()));
		add(new Label("mktcap", sd.getMktcap()));
		add(new Label("pne", sd.getPne()));
		add(new Label("divyield", sd.getDivyield()));
		add(new Label("eps", sd.getEps()));
		add(new Label("shares", sd.getShares()));
		add(new Label("beta", sd.getBeta()));
		
		add(new AjaxSelfUpdatingTimerBehavior(Duration.minutes(30)));
		
		
	}

}
