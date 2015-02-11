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
  
  
  package org.mot.feeder.iab.controller;

import org.mot.feeder.iab.client.IApiEnum;


public enum OrderType implements IApiEnum {
	None( ""),
	MKT( "MKT"),
	LMT( "LMT"),
	STP( "STP"),
	STP_LMT( "STP LMT"),
	REL( "REL"),
	TRAIL( "TRAIL"),
	BOX_TOP( "BOX TOP"),
	FIX_PEGGED( "FIX PEGGED"),
	LIT( "LIT"),
	LMT_PLUS_MKT( "LMT + MKT"),
	LOC( "LOC"),
	MIT( "MIT"),
	MKT_PRT( "MKT PRT"),
	MOC( "MOC"),
	MTL( "MTL"),
	PASSV_REL( "PASSV REL"),
	PEG_BENCH( "PEG BENCH"),
	PEG_MID( "PEG MID"),
	PEG_MKT( "PEG MKT"),
	PEG_PRIM( "PEG PRIM"),
	PEG_STK( "PEG STK"),
	REL_PLUS_LMT( "REL + LMT"),
	REL_PLUS_MKT( "REL + MKT"),
	STP_PRT( "STP PRT"),
	TRAIL_LIMIT( "TRAIL LIMIT"),
	TRAIL_LIT( "TRAIL LIT"),
	TRAIL_LMT_PLUS_MKT( "TRAIL LMT + MKT"),
	TRAIL_MIT( "TRAIL MIT"),
	TRAIL_REL_PLUS_MKT( "TRAIL REL + MKT"),
	VOL( "VOL"),
	VWAP( "VWAP");

	private String m_apiString;

	private OrderType( String apiString) {
		m_apiString = apiString;
	}

	public static OrderType get(String apiString) {
		if (apiString != null && apiString.length() > 0 && !apiString.equals( "None") ) {
			for (OrderType type : values() ) {
				if (type.m_apiString.equals( apiString) ) {
					return type;
				}
			}
		}
		return None;
	}

	@Override public String toString() {
		return this == None ? super.toString() : m_apiString;
	}

	@Override public String getApiString() {
		return m_apiString;
	}
}
