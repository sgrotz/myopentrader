package org.mot.web.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.mot.common.db.OrderDAO;
import org.mot.common.db.StrategyDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.Order;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.ExportingOptions;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.ZoomType;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class StrategyDetailChart extends Chart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StrategyDetailChart.class);

	CalculatorFactory cy = new CalculatorFactory();
	DateBuilder db = new DateBuilder();
	StrategyDAO sd = new StrategyDAO();
	OrderDAO od = new OrderDAO();

	public StrategyDetailChart(String name, String symbol, String strategy) {
		super(name, new Options());

		// Historic Chart for EUR & USD
		Options hcOptions = new Options();
		hcOptions.setyAxis(new Axis().setTitle(new Title("Value")));
		hcOptions.setTitle(new Title("Strategy Detail chart for " + strategy));

		// Disable exporting
		hcOptions.setExporting(new ExportingOptions().setEnabled(Boolean.TRUE));

		// Make the chart zoomable!
		ChartOptions co = new ChartOptions();
		co.setZoomType(ZoomType.X);
		// co.setSpacingRight(20);
		hcOptions.setChartOptions(co);

		// Get all the orders
		Order[] buys = od.getAllOrdersByStrategyAndBuySell(symbol, strategy, "BUY");
		Order[] sells = od.getAllOrdersByStrategyAndBuySell(symbol, strategy, "SELLS");
		
		CustomCoordinatesSeries<Long, Double> buySeries = new CustomCoordinatesSeries<Long, Double>();
		buySeries.setType(SeriesType.LINE);
		buySeries.setName("Buy Prices");
		buySeries.setPointInterval(50);
		buySeries.setPointStart(0);

		List<Coordinate<Long, Double>> buyData = new ArrayList<Coordinate<Long, Double>>();

		for (int i = 0; i < buys.length; i++) {
			// add the buy signals to the data series
			buyData.add(new Coordinate<Long, Double>(db.convertTimestampToLong(buys[i].getTimestamp()), buys[i].getPrice()));
		}

		hcOptions.setyAxis(new Axis().setTitle(new Title("Price")).setMin(0));

		// Finally managed to get the x axis labelled correctly! Woot woot woot!
		hcOptions.setxAxis(new Axis().setType(AxisType.DATETIME).setTickPixelInterval(50).setMaxZoom(100));

		buySeries.setData(buyData);
		buySeries.setColor(new Color(333));
		hcOptions.addSeries(buySeries);

		
		
		CustomCoordinatesSeries<Long, Double> sellSeries = new CustomCoordinatesSeries<Long, Double>();
		sellSeries.setType(SeriesType.LINE);
		sellSeries.setName("Buy Prices");
		sellSeries.setPointInterval(50);
		sellSeries.setPointStart(0);

		List<Coordinate<Long, Double>> sellData = new ArrayList<Coordinate<Long, Double>>();

		for (int i = 0; i < sells.length; i++) {
			// add the buy signals to the data series
			sellData.add(new Coordinate<Long, Double>(db.convertTimestampToLong(sells[i].getTimestamp()), sells[i].getPrice()));
		}

		hcOptions.setyAxis(new Axis().setTitle(new Title("Price")).setMin(0));

		// Finally managed to get the x axis labelled correctly! Woot woot woot!
		hcOptions.setxAxis(new Axis().setType(AxisType.DATETIME).setTickPixelInterval(50).setMaxZoom(100));

		sellSeries.setData(sellData);
		sellSeries.setColor(new Color(666));
		hcOptions.addSeries(sellSeries);
		

		super.setOptions(hcOptions);
	}

}
