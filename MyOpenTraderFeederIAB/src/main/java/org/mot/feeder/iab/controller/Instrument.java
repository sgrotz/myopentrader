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

public enum Instrument {
	STK,
	BOND,
	EFP,
	FUT_EU,
	FUT_HK,
	FUT_NA,
	FUT_US,
	IND_EU,
	IND_HK,
	IND_US,
	PMONITOR,
	PMONITORM,
	SLB_US,
	STOCK_EU,
	STOCK_HK,
	STOCK_NA,
	WAR_EU;

	public String toString() {
		return super.toString().replace( '_', '.');
	}
}
