

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

import java.util.Hashtable;

public class TickConverter {

	
	Hashtable<Integer, String> pc = new Hashtable<Integer, String>();
	Hashtable<String, Integer> pcrev = new Hashtable<String, Integer>();
	Hashtable<Integer, String> ts = new Hashtable<Integer, String>();
		
	public TickConverter() {

		/*	tickPrice:
		1 = bid 
		2 = ask
		4 = last  
		6 = high
		7 = low
		9 = close*/
		
		pc.put(1, "BID");
		pc.put(2, "ASK");
		pc.put(4, "LAST");
		pc.put(6, "HIGH");
		pc.put(7, "LOW");
		pc.put(9, "CLOSE");
		pc.put(14, "OPEN");
		
		pcrev.put("BID",1);
		pcrev.put("ASK",2 );
		pcrev.put("LAST",4);
		pcrev.put("HIGH",6);
		pcrev.put("LOW",7 );
		pcrev.put("CLOSE",9);
		pcrev.put("OPEN",14);
		
		
		ts.put(0, "BID SIZE");
		ts.put(3,  "ASK SIZE");
		ts.put(5,  "LAST SIZE");
		ts.put(8, "VOLUME");
		
	}
	
	
	public String convertPriceFieldToValue(int price) {		
		return pc.get(price);
	}
	
	public int convertPriceValueToField(String value) {
		return pcrev.get(value);
	}
	
	public String convertSizeFieldToValue(int size) {		
		return ts.get(size);
	}
	

}
