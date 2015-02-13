package org.mot.web.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mot.common.db.DatabaseConnectionFactory;
import org.mot.common.db.OrderDAO;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.objects.Order;
import org.mot.common.objects.Tick;
import org.mot.common.util.DateBuilder;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.ExportingOptions;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.ZoomType;
import com.googlecode.wickedcharts.highcharts.options.livedata.LiveDataSeries;
import com.googlecode.wickedcharts.highcharts.options.livedata.LiveDataUpdateEvent;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class TodaysTickChart extends Chart {
	
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




	public TodaysTickChart(String name, final String symbol, int refresh) {
		super(name, new Options());
		
		this.setSymbol(symbol);

		final TickPriceDAO tpd = new TickPriceDAO();
		
		// Real time chart
		Options options = new Options();
		
		options.setyAxis(new Axis().setTitle(new Title("Value")));
		options.setTitle(new Title("Todays ticks for " + symbol));

		options.setxAxis(new Axis()
        	.setType(AxisType.DATETIME)
        	//.setTickPixelInterval(250)
        	);
		
		options.setExporting(new ExportingOptions()
        	.setEnabled(Boolean.FALSE));
		
		ChartOptions co = new ChartOptions();
		co.setZoomType(ZoomType.X);
		//co.setSpacingRight(20);
		options.setChartOptions(co);
	
		// Live data series for Ask price
		//CustomCoordinatesSeries<String, Number> serie = new CustomCoordinatesSeries<>();
		LiveDataSeries askSeries = new LiveDataSeries(options, refresh) {
		@Override
			public Point update(LiveDataUpdateEvent event) {
				// TODO Auto-generated method stub
				 Point point = new Point();
			     //point.setX(new java.util.Date().getTime());
			     
			     Tick tick = tpd.getLastPriceForSymbolAsObject(symbol, "ASK");
			     point.setY(tick.getPrice());
			     point.setX(tick.getTimestamp().getTime());
			     
			     return point;
			}


		};
		
		// This will flatten the diagram to only show 500 points...
		int count = tpd.getTickCount(symbol, "ASK");
		int modulo = 0;
		if (count < 500) {
			modulo = 1;
		} else {
			modulo =  count / 500;
		}
		
		Tick[] listOfTicks = tpd.getTodaysTicksForSymbol(symbol, "ASK", modulo);
		
		askSeries.setData(randomData(listOfTicks.length, symbol, listOfTicks));
		askSeries.setName("Price");
		
		askSeries.setColor(new Color(255, 0, 0));
		
		
		//
		// Add the buy orders
		// 
		
		Order[] BUYorders = new OrderDAO().getAllOrdersBySymbol(this.symbol, "BUY");
		DateBuilder db = new DateBuilder();
		
		if (BUYorders.length > 0) {
			CustomCoordinatesSeries<Long,Double> buySeries = new CustomCoordinatesSeries<Long,Double>();
			buySeries.setType(SeriesType.SCATTER);
			buySeries.setName("BUY Orders");
			buySeries.setPointInterval(50);
			buySeries.setPointStart(0);
			
			List<Coordinate<Long, Double>> buyData = new ArrayList<Coordinate<Long, Double>>();
			
			// Add all orders into the series list
			for (int i = 0; i < BUYorders.length; i++ ){
				buyData.add(new Coordinate<Long, Double>(db.convertTimestampToLong(BUYorders[i].getTimestamp()), BUYorders[i].getPrice()));
			}
			
			buySeries.setData(buyData);
			options.addSeries(buySeries);
		}
		
		//
		// Add the sell orders
		// 
		Order[] SELLorders = new OrderDAO().getAllOrdersBySymbol(this.symbol, "SELL");
		
		if (SELLorders.length > 0) {	
			CustomCoordinatesSeries<Long,Double> sellSeries = new CustomCoordinatesSeries<Long,Double>();
			sellSeries.setType(SeriesType.SCATTER);
			sellSeries.setName("SELL Orders");
			sellSeries.setPointInterval(50);
			sellSeries.setPointStart(0);
			
			List<Coordinate<Long, Double>> sellData = new ArrayList<Coordinate<Long, Double>>();
			
			// Add all orders into the series list
			for (int i = 0; i < SELLorders.length; i++ ){
				sellData.add(new Coordinate<Long, Double>(db.convertTimestampToLong(SELLorders[i].getTimestamp()), SELLorders[i].getPrice()));
			}
			
			sellSeries.setData(sellData);
			options.addSeries(sellSeries);
		}
		
		
		options.addSeries(askSeries);
		
		super.setOptions(options);
	}
	
	private List<Point> randomData(int size, String symbol, Tick[] ticks) {
	    
	    List<Point> result = new ArrayList<Point>();
	    
	    Collections.reverse(Arrays.asList(ticks));
	    
	    for (int i = 0; i < size; i++) {
	      result
	          .add(new Point()
	              .setX(ticks[i].getTimestamp().getTime())
	              .setY(ticks[i].getPrice())
	              );
	      //time += 1000;
	    }
	    return result;
	  }

}
