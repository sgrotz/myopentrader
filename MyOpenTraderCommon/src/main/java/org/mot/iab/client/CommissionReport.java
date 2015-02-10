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

public class CommissionReport {

    public String m_execId;
    public double m_commission;
    public String m_currency;
    public double m_realizedPNL;
    public double m_yield;
    public int    m_yieldRedemptionDate; // YYYYMMDD format

    public CommissionReport() {
        m_commission = 0;
        m_realizedPNL = 0;
        m_yield = 0;
        m_yieldRedemptionDate = 0;
    }

    public boolean equals(Object p_other) {
        boolean l_bRetVal = false;

        if ( p_other == null ) {
            l_bRetVal = false;
        }
        else if ( this == p_other ) {
            l_bRetVal = true;
        }
        else {
            CommissionReport l_theOther = (CommissionReport)p_other;
            l_bRetVal = m_execId.equals( l_theOther.m_execId);
        }
        return l_bRetVal;
    }
}
