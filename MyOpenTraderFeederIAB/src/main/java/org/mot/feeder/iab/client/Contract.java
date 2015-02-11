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
  
  
  package org.mot.feeder.iab.client;

import java.util.Vector;

public class Contract implements Cloneable {

	public int    m_conId;
    public String m_symbol;
    public String m_secType;
    public String m_expiry;
    public double m_strike;
    public String m_right;
    public String m_multiplier;
    public String m_exchange;

    public String m_currency;
    public String m_localSymbol;
    public String m_tradingClass;
    public String m_primaryExch;      // pick a non-aggregate (ie not the SMART exchange) exchange that the contract trades on.  DO NOT SET TO SMART.
    public boolean m_includeExpired;  // can not be set to true for orders.

    public String m_secIdType;        // CUSIP;SEDOL;ISIN;RIC
    public String m_secId;

    // COMBOS
    public String m_comboLegsDescrip; // received in open order version 14 and up for all combos
    public Vector<ComboLeg> m_comboLegs = new Vector<ComboLeg>();

    // delta neutral
    public UnderComp m_underComp;

    public Contract() {
    	m_conId = 0;
        m_strike = 0;
        m_includeExpired = false;
    }

    public Object clone() throws CloneNotSupportedException {
        Contract retval = (Contract)super.clone();
        retval.m_comboLegs = (Vector<ComboLeg>)retval.m_comboLegs.clone();
        return retval;
    }

    public Contract(int p_conId, String p_symbol, String p_secType, String p_expiry,
                    double p_strike, String p_right, String p_multiplier,
                    String p_exchange, String p_currency, String p_localSymbol, String p_tradingClass,
                    Vector<ComboLeg> p_comboLegs, String p_primaryExch, boolean p_includeExpired,
                    String p_secIdType, String p_secId) {
    	m_conId = p_conId;
        m_symbol = p_symbol;
        m_secType = p_secType;
        m_expiry = p_expiry;
        m_strike = p_strike;
        m_right = p_right;
        m_multiplier = p_multiplier;
        m_exchange = p_exchange;
        m_currency = p_currency;
        m_includeExpired = p_includeExpired;
        m_localSymbol = p_localSymbol;
        m_tradingClass = p_tradingClass;
        m_comboLegs = p_comboLegs;
        m_primaryExch = p_primaryExch;
        m_secIdType = p_secIdType;
        m_secId = p_secId ;
    }

    public boolean equals(Object p_other) {

    	if (this == p_other) {
    		return true;
    	}

    	if (p_other == null || !(p_other instanceof Contract)) {
    		return false;
    	}

        Contract l_theOther = (Contract)p_other;

        if (m_conId != l_theOther.m_conId) {
        	return false;
        }

        if (Util.StringCompare(m_secType, l_theOther.m_secType) != 0) {
        	return false;
        }

        if (Util.StringCompare(m_symbol, l_theOther.m_symbol) != 0 ||
        	Util.StringCompare(m_exchange, l_theOther.m_exchange) != 0 ||
        	Util.StringCompare(m_primaryExch, l_theOther.m_primaryExch) != 0 ||
        	Util.StringCompare(m_currency, l_theOther.m_currency) != 0) {
        	return false;
        }

        if (!Util.NormalizeString(m_secType).equals("BOND")) {

        	if (m_strike != l_theOther.m_strike) {
        		return false;
        	}

        	if (Util.StringCompare(m_expiry, l_theOther.m_expiry) != 0 ||
        		Util.StringCompare(m_right, l_theOther.m_right) != 0 ||
        		Util.StringCompare(m_multiplier, l_theOther.m_multiplier) != 0 ||
        		Util.StringCompare(m_localSymbol, l_theOther.m_localSymbol) != 0 ||
        		Util.StringCompare(m_tradingClass, l_theOther.m_tradingClass) != 0) {
        		return false;
        	}
        }

        if (Util.StringCompare(m_secIdType, l_theOther.m_secIdType) != 0) {
        	return false;
        }

        if (Util.StringCompare(m_secId, l_theOther.m_secId) != 0) {
        	return false;
        }

    	// compare combo legs
        if (!Util.VectorEqualsUnordered(m_comboLegs, l_theOther.m_comboLegs)) {
        	return false;
        }

        if (m_underComp != l_theOther.m_underComp) {
        	if (m_underComp == null || l_theOther.m_underComp == null) {
        		return false;
        	}
        	if (!m_underComp.equals(l_theOther.m_underComp)) {
        		return false;
        	}
        }

        return true;
    }
}
