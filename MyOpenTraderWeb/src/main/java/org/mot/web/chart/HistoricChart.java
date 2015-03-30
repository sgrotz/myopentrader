package org.mot.web.chart;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.util.time.Duration;
import org.mot.common.db.DatabaseConnectionFactory;
import org.mot.common.db.TickHistoryDAO;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.TickHistory;
import org.mot.common.util.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.DateTimeLabelFormat;
import com.googlecode.wickedcharts.highcharts.options.ExportingOptions;
import com.googlecode.wickedcharts.highcharts.options.Marker;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.ZoomType;
import com.googlecode.wickedcharts.highcharts.options.color.Gradient;
import com.googlecode.wickedcharts.highcharts.options.color.HighchartsColor;
import com.googlecode.wickedcharts.highcharts.options.color.LinearGradient;
import com.googlecode.wickedcharts.highcharts.options.color.LinearGradient.GradientDirection;
import com.googlecode.wickedcharts.highcharts.options.color.RgbaColor;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CoordinatesSeries;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class HistoricChart extends Chart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HistoricChart.class);

	private static TickHistoryDAO sdw = new TickHistoryDAO();
	private static CalculatorFactory cy = new CalculatorFactory();
	private static DateBuilder db = new DateBuilder();

	public HistoricChart(String name, String stock, int maxCount,
			int RefreshSeconds) {
		super(name, new Options());

		// Historic Chart for EUR & USD
		Options hcOptions = new Options();
		hcOptions.setyAxis(new Axis().setTitle(new Title("Value")));
		hcOptions.setTitle(new Title("Historic chart for " + stock));

		// Disable exporting
		hcOptions
				.setExporting(new ExportingOptions().setEnabled(Boolean.FALSE));

		// Make the chart zoomable!
		ChartOptions co = new ChartOptions();
		co.setZoomType(ZoomType.X);
		// co.setSpacingRight(20);
		hcOptions.setChartOptions(co);

		TickHistory[] historyPrices = sdw.getHistoricPriceTableForStock(stock,
				"1 day", maxCount);

		Double[] closePrices = new Double[historyPrices.length];
		for (int i = 0; i < historyPrices.length; i++) {
			closePrices[i] = historyPrices[i].getClose();
		}

		// (String stock, int quantity, int mvgAvg1, int mvgAvg2, Double[]
		// prices)
		int ma1 = 3;
		int ma2 = 6;
		String time = " day ";

		// Lets keep this disabled for now
		// mas = new MovingAverageSimulator(stock, 10, ma1, ma2, closePrices);
		// Double[] mv1 = mas.getMvgAvg1List();
		// Double[] mv2 = mas.getMvgAvg2List();

		CustomCoordinatesSeries<Long, Double> series1 = new CustomCoordinatesSeries<Long, Double>();
		series1.setType(SeriesType.LINE);
		series1.setName("Close Price");
		series1.setPointInterval(50);
		series1.setPointStart(0);

		List<Coordinate<Long, Double>> seriesData = new ArrayList<Coordinate<Long, Double>>();

		for (int i = 0; i < historyPrices.length; i++) {

			seriesData.add(new Coordinate<Long, Double>(historyPrices[i]
					.getDateAsLong(), historyPrices[i].getClose()));

		}

		hcOptions.setyAxis(new Axis().setTitle(new Title("Price")).setMin(0));

		// Finally managed to get the x axis labelled correctly! Woot woot woot!
		hcOptions.setxAxis(new Axis().setType(AxisType.DATETIME)
				.setTickPixelInterval(50).setMaxZoom(100));

		series1.setData(seriesData);
		series1.setColor(new Color(333));
		hcOptions.addSeries(series1);

		/*
		 * CustomCoordinatesSeries<Long,Double> series2 = new
		 * CustomCoordinatesSeries<Long,Double>();
		 * series2.setType(SeriesType.LINE); series2.setName(ma1 + time +
		 * " Moving Average"); series2.setPointInterval(50);
		 * series2.setPointStart(0);
		 * 
		 * List<Coordinate<Long, Double>> seriesData2 = new
		 * ArrayList<Coordinate<Long, Double>>();
		 * 
		 * for (int i = 0; i < historyPrices.length; i++ ){
		 * 
		 * seriesData2.add(new Coordinate<Long,
		 * Double>(historyPrices[i].getDateAsLong(), mv1[i]));
		 * 
		 * } series2.setData(seriesData2);
		 * 
		 * hcOptions.addSeries(series2);
		 * 
		 * 
		 * CustomCoordinatesSeries<Long,Double> series3 = new
		 * CustomCoordinatesSeries<Long,Double>();
		 * series3.setType(SeriesType.LINE); series3.setName(ma2 + time +
		 * " Moving Average"); series3.setPointInterval(50);
		 * series3.setPointStart(0);
		 * 
		 * List<Coordinate<Long, Double>> seriesData3 = new
		 * ArrayList<Coordinate<Long, Double>>();
		 * 
		 * for (int i = 0; i < historyPrices.length; i++ ){
		 * 
		 * seriesData3.add(new Coordinate<Long,
		 * Double>(historyPrices[i].getDateAsLong(), mv2[i]));
		 * 
		 * } series3.setData(seriesData3);
		 * 
		 * hcOptions.addSeries(series3);
		 */

		// super.add(new
		// AjaxSelfUpdatingTimerBehavior(Duration.seconds(RefreshSeconds)));

		super.setOptions(hcOptions);
	}

}
