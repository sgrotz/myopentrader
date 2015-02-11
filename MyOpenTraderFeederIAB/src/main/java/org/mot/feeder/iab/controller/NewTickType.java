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


public enum NewTickType {
	BID_SIZE,
	BID,
	ASK,
	ASK_SIZE,
	LAST,
	LAST_SIZE,
	HIGH,
	LOW,
	VOLUME,
	CLOSE,
	BID_OPTION,
	ASK_OPTION,
	LAST_OPTION,
	MODEL_OPTION,
	OPEN,
	LOW_13_WEEK,
	HIGH_13_WEEK,
	LOW_26_WEEK,
	HIGH_26_WEEK,
	LOW_52_WEEK,
	HIGH_52_WEEK,
	AVG_VOLUME,
	OPEN_INTEREST,
	OPTION_HISTORICAL_VOL,
	OPTION_IMPLIED_VOL,
    OPTION_BID_EXCH,
    OPTION_ASK_EXCH,
	OPTION_CALL_OPEN_INTEREST,
	OPTION_PUT_OPEN_INTEREST,
	OPTION_CALL_VOLUME,
	OPTION_PUT_VOLUME,
	INDEX_FUTURE_PREMIUM,
	BID_EXCH,					// string
	ASK_EXCH,					// string
	AUCTION_VOLUME,
	AUCTION_PRICE,
	AUCTION_IMBALANCE,
	MARK_PRICE,
	BID_EFP_COMPUTATION,
	ASK_EFP_COMPUTATION,
	LAST_EFP_COMPUTATION,
	OPEN_EFP_COMPUTATION,
	HIGH_EFP_COMPUTATION,
	LOW_EFP_COMPUTATION,
	CLOSE_EFP_COMPUTATION,
	LAST_TIMESTAMP,				// string
	SHORTABLE,
	FUNDAMENTAL_RATIOS,			// string
	RT_VOLUME,					// string
	HALTED,
	BID_YIELD,
	ASK_YIELD,
	LAST_YIELD,
	CUST_OPTION_COMPUTATION,
	TRADE_COUNT,
	TRADE_RATE,
	VOLUME_RATE,
	LAST_RTH_TRADE,
	RT_HISTORICAL_VOL;

	public static NewTickType get( int ordinal) {
		return Types.getEnum( ordinal, values() );
	}

    public static String getField( int ordinal) {
    	NewTickType tickType = get( ordinal);
    	return tickType.toString();
    }
}
