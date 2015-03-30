package org.mot.web.chart;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.mot.common.db.TickPriceDAO;
import org.mot.common.tools.RangeAnalyser;
import org.mot.common.util.DateBuilder;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.HorizontalAlignment;
import com.googlecode.wickedcharts.highcharts.options.Legend;
import com.googlecode.wickedcharts.highcharts.options.LegendLayout;
import com.googlecode.wickedcharts.highcharts.options.Marker;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.State;
import com.googlecode.wickedcharts.highcharts.options.StatesChoice;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.VerticalAlignment;
import com.googlecode.wickedcharts.highcharts.options.ZoomType;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.color.RgbaColor;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class TickRangeChart extends Chart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String symbol;

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public TickRangeChart(String name, final String symbol, int refresh) {
		super(name, new Options());

		this.setSymbol(symbol);

		Options o = new Options();

		ChartOptions co = new ChartOptions();
		co.setType(SeriesType.SCATTER);
		co.setZoomType(ZoomType.XY);

		o.setChartOptions(co);

		o.setTitle(new Title("Tick Range chart"));
		o.setSubtitle(new Title("For the last 5 days - " + symbol));

		Axis xAxis = new Axis();
		xAxis.setTitle(new Title("Price"));

		xAxis.setStartOnTick(Boolean.TRUE);
		xAxis.setEndOnTick(Boolean.TRUE);
		xAxis.setShowLastLabel(Boolean.TRUE);
		xAxis.setType(AxisType.LINEAR);
		o.setxAxis(xAxis);

		Axis yAxis = new Axis();
		yAxis.setTitle(new Title("Count"));

		o.setyAxis(yAxis);

		Legend legend = new Legend();
		legend.setLayout(LegendLayout.VERTICAL);
		legend.setAlign(HorizontalAlignment.LEFT);
		legend.setVerticalAlign(VerticalAlignment.BOTTOM);
		legend.setX(100);
		legend.setY(70);
		legend.setFloating(Boolean.TRUE);
		legend.setBackgroundColor(new HexColor("#FFFFFF"));
		legend.setBorderWidth(1);
		o.setLegend(legend);

		State markerState = new State();
		markerState.setEnabled(Boolean.TRUE);

		StatesChoice markerStatesChoice = new StatesChoice();
		markerStatesChoice.setHover(markerState);

		State hoverState = new State();
		hoverState.setEnabled(Boolean.TRUE);
		hoverState.setLineColor(new RgbaColor(100, 100, 100));

		StatesChoice statesChoice = new StatesChoice();
		statesChoice.setHover(hoverState);

		Marker marker = new Marker();
		marker.setEnabled(Boolean.TRUE);
		marker.setRadius(5);
		marker.setStates(markerStatesChoice);

		PlotOptions plotOptions = new PlotOptions();
		plotOptions.setStates(statesChoice);
		plotOptions.setMarker(marker);

		PlotOptionsChoice plotOptionsChoice = new PlotOptionsChoice();
		plotOptionsChoice.setScatter(plotOptions);
		o.setPlotOptions(plotOptionsChoice);

		// Get the ticks for the last 5 days
		DateBuilder db = new DateBuilder();
		String pattern = "yyyy-MM-dd";
		String end = db.getTimeStampWithPattern(pattern);
		String start = db.subtractDaysFromDate(end, pattern, 5);

		ArrayList<Double> askTicks = new TickPriceDAO()
				.getPriceForSymbolByDate(symbol, start, end, "ASK");

		if (askTicks.size() > 0) {
			Double[] ticks = new Double[askTicks.size()];
			askTicks.toArray(ticks);

			// Run the range analyser
			RangeAnalyser ra = new RangeAnalyser(ticks, false, true, 12);
			LinkedHashMap<Double, Integer> scatterList = ra.getPriceRange();

			// Hashtable<Double, Integer> scatterList = new
			// TickPriceDAO().getTickScatter(symbol, "BID");

			List<Coordinate<Double, Integer>> scatterData = new ArrayList<Coordinate<Double, Integer>>();

			Set<Entry<Double, Integer>> set = scatterList.entrySet();
			Iterator<Entry<Double, Integer>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<Double, Integer> entry = (Map.Entry<Double, Integer>) it
						.next();
				scatterData.add(new Coordinate<Double, Integer>(entry.getKey(),
						entry.getValue()));
			}

			CustomCoordinatesSeries<Double, Integer> series1 = new CustomCoordinatesSeries<Double, Integer>();
			series1.setColor(new RgbaColor(111, 83, 83, 0.5f));
			series1.setName("ASK");
			series1.setData(scatterData);
			o.addSeries(series1);
		}

		super.setOptions(o);
	}

}
