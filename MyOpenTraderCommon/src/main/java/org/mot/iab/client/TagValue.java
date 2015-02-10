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

public class TagValue {

	public String m_tag;
	public String m_value;

	public TagValue() {
	}

	public TagValue(String p_tag, String p_value) {
		m_tag = p_tag;
		m_value = p_value;
	}

	public boolean equals(Object p_other) {

		if( this == p_other)
            return true;

        if( p_other == null)
            return false;

        TagValue l_theOther = (TagValue)p_other;

        if( Util.StringCompare(m_tag, l_theOther.m_tag) != 0 ||
        	Util.StringCompare(m_value, l_theOther.m_value) != 0) {
        	return false;
        }

		return true;
	}
}
