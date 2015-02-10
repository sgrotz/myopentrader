

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
  
  
  package org.mot.common.conversion;

import java.util.ArrayList;

import org.mot.common.objects.LoadValue;

public class LoadValueConverter {

	/**
	 * Use this method to convert the string to values. This will convert the string provided in the database to values, which 
	 * are being passed to the startup method in the strategy interface.
	 * 
	 * @param values - String values, containing all values
	 * @return - an array of loadvalues
	 */
	public LoadValue[] convertStringToValues(String values) {

		/*
		 * Sample load values would look similar to this: 
		 * av1=10;av2=20;name=myName
		 */


		if (values != null) {
			// First split by ; - gives us the rows
			String[] props = values.split(";");
			ArrayList<LoadValue> alv = new ArrayList<LoadValue>();
			//LoadValue[] lv = new LoadValue[props.length];

			// Loop over the rows and convert into load values
			for (int i = 0; i < props.length; i++) {
				String[] item = props[i].split("=");
				try { 
					// First value is always the name - second the value
					LoadValue lv = new LoadValue();
					lv.setName(item[0]);
					lv.setValue(item[1]);
					alv.add(lv);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("** Incorrect properties specified - please use: name1=value1;name2=value2 as format ...");
					break;
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			LoadValue[] lvs = new LoadValue[alv.size()];
			
			return alv.toArray(lvs);
		} else {
			return null; 
		} 
	}

	
	/**
	 * Use this method to convert the load values to a string. 
	 * 
	 * @param values - values to convert
	 * @return - String representation of values
	 */
	public String convertValuesToString(LoadValue[] values) {
		/*
		 * Sample load values would look similar to this: 
		 * av1=10;av2=20;name=myName
		 */
		String ret = "";
		
		
		for (int i = 0; i < values.length; i++) {
			ret = ret + values[i].getName() + "=" + values[i].getValue() +";";
		}
		
		return ret; 
	}
	
	

	/**
	 * Get a parameter from the loadvalues
	 * 
	 * @param values - pass in the load values
	 * @param name - which parameter to look for?
	 * @return - string value representing the parameter
	 */
	public String getValue(LoadValue[] values, String name) {

		String ret = null;

		for (int i = 0; i < values.length; i++) {
			if (values[i].getName().equals(name)) {
				ret = values[i].getValue();
				break;
			}
		}

		return ret;		
	}
	
	
	/**
	 * Get a parameter from the loadvalues, if it doesn't exist, use default value
	 * 
	 * @param values - pass in the load values
	 * @param name - which parameter to look for?
	 * @param defaultValue - if not found, provide a default value
	 * @return - string value representing the parameter
	 */
	public String getValue(LoadValue[] values, String name, String defaultValue) {

		String ret = null;

		for (int i = 0; i < values.length; i++) {
			if (values[i].getName().equals(name)) {
				ret = values[i].getValue();
				break;
			} else {
				ret = defaultValue;
			}
		}

		return ret;	
	}
	
	
	/**
	 * Get a parameter from the loadvalues, if it doesn't exist, use default value. 
	 * This method accepts a string as the loadvalues
	 * 
	 * @param values - pass in the load values as String
	 * @param name - which parameter to look for?
	 * @param defaultValue - if not found, provide a default value
	 * @return - string value representing the parameter
	 */
	public String getValue(String loadValues, String name, String defaultValue) {

		LoadValue[] values = this.convertStringToValues(loadValues);
		
		String ret = null;

		for (int i = 0; i < values.length; i++) {
			if (values[i].getName().equals(name)) {
				ret = values[i].getValue();
				break;
			} else {
				ret = defaultValue;
			}
		}

		return ret;	
	}
	
	
	/**
	 * Get a parameter from the loadvalues, if it doesn't exist, return null
	 * This method accepts a string as the loadvalues
	 * 
	 * @param values - pass in the load values as String
	 * @param name - which parameter to look for?
	 * @return - string value representing the parameter
	 */
	public String getValue(String loadValues, String name) {

		LoadValue[] values = this.convertStringToValues(loadValues);
		
		String ret = null;

		for (int i = 0; i < values.length; i++) {
			if (values[i].getName().equals(name)) {
				ret = values[i].getValue();
				break;
			} 
		}

		return ret;	
	}
	
	
	public String addValue(String loadValues, String name, String value) {

		loadValues = loadValues + name + "=" + value + ";";
		
		return loadValues;	
	}

}
