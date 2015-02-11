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

public class ContractDetails {
    public Contract	m_summary;
    public String 	m_marketName;
    public double 	m_minTick;
    public int      m_priceMagnifier;
    public String 	m_orderTypes;
    public String 	m_validExchanges;
    public int      m_underConId;
    public String 	m_longName;
    public String	m_contractMonth;
    public String	m_industry;
    public String	m_category;
    public String	m_subcategory;
    public String	m_timeZoneId;
    public String	m_tradingHours;
    public String	m_liquidHours;
    public String 	m_evRule;
    public double 	m_evMultiplier;

    public Vector<TagValue> m_secIdList; // CUSIP/ISIN/etc.

    // BOND values
    public String 	m_cusip;
    public String 	m_ratings;
    public String 	m_descAppend;
    public String 	m_bondType;
    public String 	m_couponType;
    public boolean 	m_callable			= false;
    public boolean 	m_putable			= false;
    public double 	m_coupon			= 0;
    public boolean 	m_convertible		= false;
    public String 	m_maturity;
    public String 	m_issueDate;
    public String 	m_nextOptionDate;
    public String 	m_nextOptionType;
    public boolean 	m_nextOptionPartial = false;
    public String 	m_notes;

    public ContractDetails() {
        m_summary = new Contract();
        m_minTick = 0;
        m_underConId = 0;
        m_evMultiplier = 0;
    }

    public ContractDetails( Contract p_summary, String p_marketName, 
    		double p_minTick, String p_orderTypes, String p_validExchanges, int p_underConId, String p_longName,
    	    String p_contractMonth, String p_industry, String p_category, String p_subcategory,
    	    String p_timeZoneId, String	p_tradingHours, String p_liquidHours,
    	    String p_evRule, double p_evMultiplier) {
        m_summary = p_summary;
    	m_marketName = p_marketName;
    	m_minTick = p_minTick;
    	m_orderTypes = p_orderTypes;
    	m_validExchanges = p_validExchanges;
    	m_underConId = p_underConId;
    	m_longName = p_longName;
        m_contractMonth = p_contractMonth;
        m_industry = p_industry;
        m_category = p_category;
        m_subcategory = p_subcategory;
        m_timeZoneId = p_timeZoneId;
        m_tradingHours = p_tradingHours;
        m_liquidHours = p_liquidHours;
        m_evRule = p_evRule;
        m_evMultiplier = p_evMultiplier;
    }
}
