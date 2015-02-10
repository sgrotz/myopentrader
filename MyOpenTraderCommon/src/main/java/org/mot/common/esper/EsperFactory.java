

 /*
  * Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *
  */
  
  
  package org.mot.common.esper;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mot.common.db.TickPriceDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.Tick;
import org.mot.common.tools.PropertiesFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.SafeIterator;
import com.espertech.esper.client.UpdateListener;

/**
 * The EsperFactory is the responsible for the core CEP functionality. 
 * This factory is used to send all events to the CEP Engine, but also to get the results from the queries back.
 * @author sgrotz
 */
public class EsperFactory {   

	Logger logger = Logger.getLogger(getClass());
	
	private static EPServiceProvider epService;
	private static EPRuntime epr;
	private static EPAdministrator epa;

	private EsperFactory() {
		Configuration config = new Configuration();
		config.getEngineDefaults().getThreading().setListenerDispatchPreserveOrder(true);
		config.getEngineDefaults().getViewResources().setShareViews(false);
		
		epService = EPServiceProviderManager.getDefaultProvider(config);
		
		epr = epService.getEPRuntime();
		epa = epService.getEPAdministrator();
		
	}
	
	public static EsperFactory instance = null;
	
	public static EsperFactory getInstance() {
		if (instance == null) {
			return new EsperFactory();
		} else {
			return instance;
		}	
	}
	


	/**
	 * Use this method to set a new Esper Expression. 
	 * @param Name
	 * @param expression
	 * @param listener
	 */
	public void setEsperExpression(String Name, String expression, UpdateListener listener) {

		if (Name != null) {
			
			if (Name.equals(expression)) {
				expression = this.getStatement(Name);
			}
			
			logger.debug("Setting new EsperStatement: " + expression);
			EPStatement statement = epa.createEPL(expression, Name);
			
			if (listener != null) {
				statement.addListener(listener);
			}
		}
	}

	private String getStatement(String name) {
		return epa.getStatement(name).toString();
	}




	/**
	 * Use this method to get an Esper Expression given its Name
	 * @param Name
	 * @return EPStatement
	 */
	private EPStatement getEsperStatement(String Name) {
		return epa.getStatement(Name);
	}


	/**
	 * This is the main method to send events to the ESPER CEP Engine
	 * @param object
	 */
	public void sendEsperEvent(Object object) {
		logger.debug("Sending new ESPER event ..." );
		epr.sendEvent(object);
	}


	/**
	 * This method is used to query a certain statement from the CEP Engine. The method will return an ArrayList of EventBeans
	 * @param StatementName
	 * @return ArrayList<EventBean> 
	 */
	private ArrayList<EventBean> queryStatement(String StatementName) {
		
		long start = System.currentTimeMillis();
		
		// Get the Eventbean iterator
		SafeIterator<EventBean> iterator = getEsperStatement(StatementName).safeIterator();
		ArrayList<EventBean> result = new ArrayList<EventBean>();
		logger.trace("Querying statement: " + StatementName);
		
		
		// Loop over the list and add them to the result list.
		while (iterator.hasNext()) {
			EventBean event = iterator.next();
			result.add(event);
		}

		iterator.close();

		long end = System.currentTimeMillis();
		logger.trace("queryStatement took " + (end - start) + " msecs to process " + StatementName );

		
		// Return result list
		return result;
	}


	/**
	 * This method is used to query field values from a CEP Query. 
	 * @param StatementName
	 * @param fieldName
	 * @return Object[]
	 */
	public Number[] getNumericFieldValues(String StatementName, String fieldName ) {
		
		Boolean exists = this.checkIfStatementNameExists(StatementName);
		
		if (exists) {
			// Get ArrayList of Event Beans
			ArrayList<EventBean> list = queryStatement(StatementName);
			
			logger.trace("Getting Numeric Values for Statement: " + StatementName +" - Field: " + fieldName);
			
			// Loop over array and add the requested field to the output list
			//ArrayList<Number> resultList = new ArrayList<Number>();
			Number[] resultList = new Number[list.size()];
			
			for (int i = 0; i < list.size(); i++) {
				EventBean event = list.get(i);
				
				resultList[i] = (Number) event.get(fieldName);
			}
			
			//logger.trace("Returning list with " + resultList.length + " objects " + resultList.toString());
			
			// Convert back to an Array!
			return resultList;
		} else {
			return null;
		}

	}
	
	
	/**
	 * @param StatementName
	 * @param fieldName
	 * @return
	 */
	public Long[] getLongFieldValues(String StatementName, String fieldName ) {
		
		Boolean exists = checkIfStatementNameExists(StatementName);
		
		if (exists) {
			// Get ArrayList of Event Beans
			ArrayList<EventBean> list = queryStatement(StatementName);
			
			logger.trace("Getting Numeric Values for Statement: " + StatementName +" - Field: " + fieldName);
			
			// Loop over array and add the requested field to the output list
			//ArrayList<Number> resultList = new ArrayList<Number>();
			Long[] resultList = new Long[list.size()];
			
			for (int i = 0; i < list.size(); i++) {
				EventBean event = list.get(i);
				
				resultList[i] = (Long) event.get(fieldName);
			}
			
			//logger.trace("Returning list with " + resultList.length + " objects " + resultList.toString());
			
			// Convert back to an Array!
			return resultList;
		} else {
			return null;
		}
	}
	
	/**
	 * @param StatementName
	 * @param fieldName
	 * @return
	 */
	public Double[] getDoubleFieldValues(String StatementName, String fieldName ) {
		
		if (this.checkIfStatementNameExists(StatementName)) {
			// Get ArrayList of Event Beans
			ArrayList<EventBean> list = queryStatement(StatementName);
			
			logger.trace("Getting Numeric Values for Statement: " + StatementName +" - Field: " + fieldName);
			
			// Loop over array and add the requested field to the output list
			//ArrayList<Number> resultList = new ArrayList<Number>();
			Double[] resultList = new Double[list.size()];
			
			for (int i = 0; i < list.size(); i++) {
				EventBean event = list.get(i);
				
				resultList[i] = (Double) event.get(fieldName);
				//System.out.println(resultList[i]);
			}
			
			//logger.trace("Returning list with " + resultList.length + " objects " + resultList.toString());
			return resultList;
		} else {
			return null;
		}
		
	}
	
	
	
	/**
	 * @param StatementName
	 * @param fieldName
	 * @return
	 */
	public Double getLastDoubleValue(String StatementName, String fieldName) {
		
		long start = System.currentTimeMillis();
		Double ret = 0.0;
		Double[] values = this.getDoubleFieldValues(StatementName, fieldName);
		
		if (values.length != 0.0) {
			if (values[values.length - 1] != null ) {
				ret = values[values.length - 1];
			}
		}
		
		long end = System.currentTimeMillis();
		logger.debug("getLastDoubleValue - Processing StatementName " + StatementName + " for " + values.length + " took " + (end - start) + " msecs...");

		return ret;
	}
	
	
	/**
	 * @param StatementName
	 * @param fieldName
	 * @return
	 */
	public Long getLastLongValue(String StatementName, String fieldName) {
		long start = System.currentTimeMillis();
		
		Long ret = 0L;
		Long[] values = this.getLongFieldValues(StatementName, fieldName);
		
		if (values.length != 0) {
			if ((values[values.length - 1] != null)) {
				ret = values[values.length - 1];
			}
		}
		
		long end = System.currentTimeMillis();
		logger.debug("getLastLongValue - Processing StatementName " + StatementName + " for " + values.length + " took " + (end - start) + " msecs...");
		return ret;
	}



	/**
	 * This method is used to query string field values from a CEP Query. 
	 * @param StatementName
	 * @param fieldName
	 * @return String[]
	 */
	public String[] getStringFieldValues(String StatementName, String fieldName ) {
		
		Boolean exists = this.checkIfStatementNameExists(StatementName);
		
		if (exists) {
			// Get ArrayList of Event Beans
			ArrayList<EventBean> list = queryStatement(StatementName);

			logger.debug("Getting String Values for Statement: " + StatementName +" - Field: " + fieldName);
			
			// Loop over array and add the requested field to the output list
			//ArrayList<Number> resultList = new ArrayList<Number>();
			String[] resultList = new String[list.size()];
			
			for (int i = 0; i < list.size(); i++) {
				EventBean event = list.get(i);
				
				resultList[i] = (String) event.get(fieldName);
			}
			
			// Convert back to an Array!
			return resultList;
		} else {
			return null;
		}
		


	}
	

	/**
	 * @param fieldName
	 * @param event
	 * @return
	 */
	private Object getFieldValue(String fieldName, EventBean event) {
		return event.get(fieldName);
	}
	
	
	/**
	 * @param name
	 * @return
	 */
	private Boolean checkIfStatementNameExists(String name) {
		
		Boolean found = false;
		
		String[] names = epa.getStatementNames();
		for (int i = 0; i < names.length; i++ ){
			if (names[i].equals(name)) {
				found = true; 
				break;
			}
		}

		return found;		
	}
	
	public static void main(String[] args) {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		pf.setConfigDir(args[0]);

		System.setProperty("PathToConfigDir", pf.getConfigDir());
		PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
		
		int avg1 = 5;
		int avg2 = 15;
		String stock = "AAPL";
		String name = "TEST";
		int total = avg2;
		
		EsperFactory ef = new EsperFactory();
		ef.setEsperExpression(name, "select avg1.symbol, avg1.price, avg2.price, count(avg1.price), avg(avg1.price) as avg1price, avg(avg2.price) as avg2price, stddev(avg1.price) as stddev, avedev(avg1.price) as avedev from org.sg.iab.common.objects.Tick(priceField = 'ASK').win:length(" + avg1 + ") as avg1, org.sg.iab.common.objects.Tick(priceField = 'ASK').win:length(" + avg2 + ") as avg2 where avg1.symbol = '" + stock +"' and avg1.symbol = avg2.symbol", null);
		//ef.setEsperExpression(name, "select avg1.symbol, avg1.price, count(avg1.price), avg(avg1.price) as avg1price, stddev(avg1.price) as stddev, avedev(avg1.price) as avedev from org.sg.iab.common.objects.Tick(priceField = 'ASK').win:length(" + avg1 + ") as avg1 where avg1.symbol = '" + stock +"'", null);
		//String expression = "select ask, avg(ask) from org.mot.objects.Tick.win:length(30)";
		
		//eF.setEsperExpression(expression, expression, null); //new EsperUpdateListener());
		//EPStatement statement = epService.getEPAdministrator().createEPL(expression, "123");
		
		TickPriceDAO tpd = new TickPriceDAO();
		Tick[] list = tpd.getTicksForSymbol(stock, "ASK", total);
		
		Double[] data = new Double[list.length];
		String values = null;
		double count = 0;
		
		for (int i = 0; i < list.length; i++) {	
			//System.out.println(epService.getEPRuntime().getNumEventsEvaluated());
			list[i].setPrice(i +1);
			ef.sendEsperEvent(list[i]);
			data[i] = list[i].getPrice();
			count = count + i;
			values = values + " - " + data[i];
		}
		System.out.println("Rolling Average #1: "+  avg1 + " -- Average 2: "+ avg2);
		System.out.println("Amount of values: " + total + " - total sum: " + count + " - average is: " + count / (total));
		System.out.println("Values are: " + values);
		
		CalculatorFactory cf = new CalculatorFactory();
	
		Double[] mvgList1 = cf.getRollingTimeWindowAverage(data, avg1);
		Double ravg1 = mvgList1[mvgList1.length -1];

		Double[] mvgList2 = cf.getRollingTimeWindowAverage(data, avg2);
		Double ravg2 = mvgList2[mvgList2.length -1 ];
		
		System.out.println("Rolling average value 1 is " + ravg1);
		System.out.println("Rolling average value 2 is " + ravg2);
		
		
		// ArrayList<EventBean>  eb = ef.queryStatement(name);
		Double[] avg1list =  ef.getDoubleFieldValues(name, "avg1price");
		Double esavg1 = avg1list[avg1 -1];
		System.out.println("Esper average for value 1 is: " + esavg1);
		
		Double[] avg2list =  ef.getDoubleFieldValues(name, "avg2price");
		Double esavg2 = avg2list[avg2 -1];
		System.out.println("Esper average for value 2 is: " + esavg2);
		
		// System.out.println("Ask value: " + ef.getFieldValue("ask", eb.get(0)));
	
		// System.out.println("Expression value 0: " +ef.getNumericFieldValues(name, "ask")[0]);
		
		System.exit(0);
	
	}


}
