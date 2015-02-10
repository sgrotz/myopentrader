/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */



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
  
  
  package org.mot.iab.client;

public class MarketDataType {
    // constants - market data types
    public static final int REALTIME   = 1;
    public static final int FROZEN     = 2;

    public static String getField( int marketDataType) {
        switch( marketDataType) {
            case REALTIME:                    return "Real-Time";
            case FROZEN:                      return "Frozen";

            default:                          return "Unknown";
        }
    }

    public static String[] getFields(){
    	int totalFields = MarketDataType.class.getFields().length;
    	String [] fields = new String[totalFields];
    	for (int i = 0; i < totalFields; i++){
    		fields[i] = MarketDataType.getField(i + 1);
    	}
    	return fields;
    }
}
