

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
  
  
  package org.mot.core.algo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.mot.common.esper.EsperFactory;
import org.mot.common.objects.Expression;
import org.mot.common.objects.LoadValue;

import com.espertech.esper.client.UpdateListener;

public class GenericAlgorithm {
	
	EsperFactory ef = EsperFactory.getInstance();
	Logger logger = Logger.getLogger(getClass());
	
	
	
	
	/**
	 * This is a generic algo implementation to be used. Most parameters are taken out of the database
	 * Internally it will use reflection to spin up a new Algo
	 * 
	 * @param className - class to invoke
	 * @param name - name of the algo
	 * @param symbol - which symbol to use
	 * @param values - Array of load values (as specified in the database)
	 * @param simulated - is this a simulation?
	 * @param amount - the amount to trade
	 */
	public GenericAlgorithm (String className, String name, String symbol, LoadValue[] values, boolean simulated, int amount) {
		
		Class<?> c;
		try {
			c = Class.forName(className);
			Object inst = c.newInstance();
		
			// Make sure the startup method is called for initialization...
			Method m1= c.getDeclaredMethod("startup", String.class, String.class, LoadValue[].class, Boolean.class, Integer.class );
			m1.invoke(inst, name, symbol, values, simulated, amount);
			
			// Get the esper expressions and initiate them
			Method m = c.getDeclaredMethod("getEsperExpression");
			Object ret = m.invoke(inst);
			ArrayList<Expression> expressions = (ArrayList<Expression>) ret;
			
			// Iterate over the expressions
			Iterator<Expression> it = expressions.iterator();
			while (it.hasNext()) {
				Expression e = it.next();
				ef.setEsperExpression(e.getName(), e.getExpression(), (UpdateListener) inst);
			}
					
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}	 

}
