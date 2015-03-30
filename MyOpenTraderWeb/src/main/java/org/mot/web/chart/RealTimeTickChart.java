package org.mot.web.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mot.common.db.TickPriceDAO;
import org.mot.common.objects.Tick;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ExportingOptions;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.livedata.LiveDataSeries;
import com.googlecode.wickedcharts.highcharts.options.livedata.LiveDataUpdateEvent;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class RealTimeTickChart extends Chart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String symbol;

	public String getPccy() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public RealTimeTickChart(String name, final String symbol, int tickCount,
			int refresh) {
		super(name, new Options());

		this.setSymbol(symbol);

		final TickPriceDAO tpd = new TickPriceDAO();

		// Real time chart
		Options options = new Options();

		options.setyAxis(new Axis().setTitle(new Title("Value")));
		options.setTitle(new Title("Last " + tickCount + " ticks for " + symbol));

		options.setxAxis(new Axis().setType(AxisType.DATETIME)
				.setTickPixelInterval(250));

		options.setExporting(new ExportingOptions().setEnabled(Boolean.FALSE));

		// Live data series for Ask price
		// CustomCoordinatesSeries<String, Number> serie = new
		// CustomCoordinatesSeries<>();
		LiveDataSeries askSeries = new LiveDataSeries(options, refresh) {
			@Override
			public Point update(LiveDataUpdateEvent event) {
				// TODO Auto-generated method stub
				Point point = new Point();
				// point.setX(new java.util.Date().getTime());

				Tick tick = tpd.getLastPriceForSymbolAsObject(symbol, "LAST");
				point.setY(tick.getPrice());
				point.setX(tick.getTimestamp().getTime());

				return point;
			}

		};

		Tick[] listOfTicks = tpd.getTicksForSymbol(symbol, "LAST", tickCount);

		askSeries.setData(randomData(listOfTicks.length, symbol, listOfTicks));
		askSeries.setName("Price");
		options.addSeries(askSeries);

		super.setOptions(options);
	}

	private List<Point> randomData(int size, String symbol, Tick[] ticks) {

		List<Point> result = new ArrayList<Point>();

		Collections.reverse(Arrays.asList(ticks));

		for (int i = 0; i < size; i++) {
			result.add(new Point().setX(ticks[i].getTimestamp().getTime())
					.setY(ticks[i].getPrice()));
			// time += 1000;
		}
		return result;
	}

}
