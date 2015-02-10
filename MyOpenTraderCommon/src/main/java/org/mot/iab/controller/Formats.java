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
  
  
  package org.mot.iab.controller;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formats {
	private static final Format FMT2 = new DecimalFormat( "#,##0.00");
	private static final Format FMT0 = new DecimalFormat( "#,##0");
	private static final Format PCT = new DecimalFormat( "0.0%");
	private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss"); // format for display
	private static final SimpleDateFormat TIME = new SimpleDateFormat( "HH:mm:ss"); // format for display

	/** Format with two decimals. */
	public static String fmt( double v) {
		return v == Double.MAX_VALUE ? null : FMT2.format( v);
	}

	/** Format with two decimals; return null for zero. */
	public static String fmtNz( double v) {
		return v == Double.MAX_VALUE || v == 0 ? null : FMT2.format( v);
	}

	/** Format with no decimals. */
	public static String fmt0( double v) {
		return v == Double.MAX_VALUE ? null : FMT0.format( v);
	}

	/** Format as percent with one decimal. */
	public static String fmtPct( double v) {
		return v == Double.MAX_VALUE ? null : PCT.format( v);
	}

	/** Format date/time for display. */
	public static String fmtDate( long ms) {
		return DATE_TIME.format( new Date( ms) );
	}

	/** Format time for display. */
	public static String fmtTime( long ms) {
		return TIME.format( new Date( ms) );
	}
}
