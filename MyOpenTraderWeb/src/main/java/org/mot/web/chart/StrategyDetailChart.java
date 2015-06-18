package org.mot.web.chart;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.mot.common.db.OrderDAO;
import org.mot.common.db.StrategyDAO;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.Order;
import org.mot.common.objects.Tick;
import org.mot.common.util.DateBuilder;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.ExportingOptions;
import com.googlecode.wickedcharts.highcharts.options.Marker;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
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


	CalculatorFactory cy = new CalculatorFactory();
	static DateBuilder db = new DateBuilder();
	static StrategyDAO sd = new StrategyDAO();
	static OrderDAO od = new OrderDAO();
	static TickPriceDAO tpd = new TickPriceDAO();

	public StrategyDetailChart(String name, String symbol, String strategy) {
		super(name, new Options());

		
		// Historic Chart for EUR & USD
		Options hcOptions = new Options();
		hcOptions.setTitle(new Title("Strategy Detail chart for " + strategy));
		
		Axis yAxis = new Axis();
		yAxis.setTitle(new Title("Value"));
		yAxis.setStartOnTick(false);
		yAxis.setShowFirstLabel(false);
		hcOptions.setyAxis(yAxis);
		
		PlotOptions po = new PlotOptions();
		po.setLineWidth(1);
		po.setMarker(new Marker().setEnabled(false));
		po.setShadow(false);
		
		PlotOptionsChoice poc = new PlotOptionsChoice();
		poc.setPlotOptions(po, SeriesType.LINE);
		
		hcOptions.setPlotOptions(poc);
		
		// Disable exporting
		hcOptions.setExporting(new ExportingOptions().setEnabled(Boolean.TRUE));

		// Make the chart zoomable!
		ChartOptions co = new ChartOptions();
		co.setZoomType(ZoomType.X);
		// co.setSpacingRight(20);
		hcOptions.setChartOptions(co);

		// Get all the orders
		Order[] buys = od.getAllOrdersByStrategyAndBuySell(symbol, strategy, "BUY");
		Order[] sells = od.getAllOrdersByStrategyAndBuySell(symbol, strategy, "SELL");
		
		Timestamp startTime = buys[0].getTimestamp();
		Timestamp endTime = sells[sells.length -1].getTimestamp();
		
		Tick[] ticks = tpd.getTicksForSymbolBetweenTimestamps(symbol, "BID", startTime, endTime, 50);
		
		
		CustomCoordinatesSeries<Long, Double> tickSeries = new CustomCoordinatesSeries<Long, Double>();
		tickSeries.setType(SeriesType.LINE);
		tickSeries.setName("Tick Price");
		//tickSeries.setPointInterval(50);
		//tickSeries.setPointStart(0);

		List<Coordinate<Long, Double>> tickData = new ArrayList<Coordinate<Long, Double>>();

		for (int i = 0; i < ticks.length; i++) {
			// Figure out the start and end time
			
			
			Long tickTimestamp = db.convertTimestampToLong(ticks[i].getTimestamp());
			// add the buy signals to the data series
			tickData.add(new Coordinate<Long, Double>(tickTimestamp, ticks[i].getPrice()));
			
		}

		hcOptions.setyAxis(new Axis().setTitle(new Title("Price")).setMin(0));

		// Finally managed to get the x axis labelled correctly! Woot woot woot!
		hcOptions.setxAxis(new Axis().setType(AxisType.DATETIME).setTickPixelInterval(50).setMaxZoom(100));
		
		tickSeries.setData(tickData);
		tickSeries.setColor(new Color(Color.black.getRGB()));
		hcOptions.addSeries(tickSeries);
		
		
		
		
		CustomCoordinatesSeries<Long, Double> buySeries = new CustomCoordinatesSeries<Long, Double>();
		buySeries.setType(SeriesType.SCATTER);
		buySeries.setName("Buy Prices");
		//buySeries.setPointInterval(50);
		//buySeries.setPointStart(0);

		List<Coordinate<Long, Double>> buyData = new ArrayList<Coordinate<Long, Double>>();

		for (int i = 0; i < buys.length; i++) {
			// Figure out the start and end time
			
			
			Long buyTimestamp = db.convertTimestampToLong(buys[i].getTimestamp());
			// add the buy signals to the data series
			buyData.add(new Coordinate<Long, Double>(buyTimestamp, buys[i].getPrice()));
			
		}

		hcOptions.setyAxis(new Axis().setTitle(new Title("Price")).setMin(0));

		// Finally managed to get the x axis labelled correctly! Woot woot woot!
		hcOptions.setxAxis(new Axis().setType(AxisType.DATETIME).setTickPixelInterval(50).setMaxZoom(100));
		
		buySeries.setData(buyData);
		buySeries.setColor(new Color(Color.red.getRGB()));
		hcOptions.addSeries(buySeries);

		
		
		CustomCoordinatesSeries<Long, Double> sellSeries = new CustomCoordinatesSeries<Long, Double>();
		sellSeries.setType(SeriesType.SCATTER);
		sellSeries.setName("Sell Prices");
		//sellSeries.setPointInterval(50);
		//sellSeries.setPointStart(0);

		List<Coordinate<Long, Double>> sellData = new ArrayList<Coordinate<Long, Double>>();

		for (int i = 0; i < sells.length; i++) {			
			Long ts = db.convertTimestampToLong(sells[i].getTimestamp());
			// add the buy signals to the data series
			sellData.add(new Coordinate<Long, Double>(ts, sells[i].getPrice()));
		}

		hcOptions.setyAxis(new Axis().setTitle(new Title("Price")).setMin(0));

		// Finally managed to get the x axis labelled correctly! Woot woot woot!
		hcOptions.setxAxis(new Axis().setType(AxisType.DATETIME).setTickPixelInterval(50).setMaxZoom(100));

		sellSeries.setData(sellData);
		sellSeries.setColor(new Color(Color.blue.getRGB()));
		hcOptions.addSeries(sellSeries);
		

		super.setOptions(hcOptions);
	}

}
