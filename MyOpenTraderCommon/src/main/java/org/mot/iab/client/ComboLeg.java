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

public class ComboLeg {
    public final static int 	SAME = 0; 	// open/close leg value is same as combo
    public final static int 	OPEN = 1;
    public final static int 	CLOSE = 2;
    public final static int 	UNKNOWN = 3;

    public int 					m_conId;
    public int 					m_ratio;
    public String 				m_action; // BUY/SELL/SSHORT/SSHORTX
    public String 				m_exchange;
    public int 					m_openClose;

    // for stock legs when doing short sale
    public int                  m_shortSaleSlot; // 1 = clearing broker, 2 = third party
    public String               m_designatedLocation;
    public int                  m_exemptCode;

    public ComboLeg() {
    	this(/* conId */ 0, /* ratio */ 0, /* action */ null,
    		/* exchange */ null, /* openClose */ 0,
    		/* shortSaleSlot */ 0, /* designatedLocation*/ null, /* exemptCode */ -1);
    }

    public ComboLeg(int p_conId, int p_ratio, String p_action, String p_exchange, int p_openClose) {
    	this(p_conId, p_ratio, p_action, p_exchange, p_openClose,
    		/* shortSaleSlot */ 0, /* designatedLocation*/ null, /* exemptCode */ -1);

    }

    public ComboLeg(int p_conId, int p_ratio, String p_action, String p_exchange,
    		int p_openClose, int p_shortSaleSlot, String p_designatedLocation) {
    	this(p_conId, p_ratio, p_action, p_exchange, p_openClose, p_shortSaleSlot, p_designatedLocation,
    		/* exemptCode */ -1);

    }

    public ComboLeg(int p_conId, int p_ratio, String p_action, String p_exchange,
    		int p_openClose, int p_shortSaleSlot, String p_designatedLocation, int p_exemptCode) {
        m_conId = p_conId;
        m_ratio = p_ratio;
        m_action = p_action;
        m_exchange = p_exchange;
        m_openClose = p_openClose;
        m_shortSaleSlot = p_shortSaleSlot;
        m_designatedLocation = p_designatedLocation;
        m_exemptCode = p_exemptCode;
    }

    public boolean equals(Object p_other) {
        if ( this == p_other ) {
            return true;
        }
        else if ( p_other == null ) {
            return false;
        }

        ComboLeg l_theOther = (ComboLeg)p_other;

        if (m_conId != l_theOther.m_conId ||
        	m_ratio != l_theOther.m_ratio ||
        	m_openClose != l_theOther.m_openClose ||
        	m_shortSaleSlot != l_theOther.m_shortSaleSlot ||
        	m_exemptCode != l_theOther.m_exemptCode) {
        	return false;
        }

        if (Util.StringCompareIgnCase(m_action, l_theOther.m_action) != 0 ||
        	Util.StringCompareIgnCase(m_exchange, l_theOther.m_exchange) != 0 ||
        	Util.StringCompareIgnCase(m_designatedLocation, l_theOther.m_designatedLocation) != 0) {
        	return false;
        }

        return true;
    }
}
