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

/** This class is used to build messages so the entire message can be
 *  sent to the socket in a single write. */
public class Builder {
	private static final char SEP = 0;

	private StringBuilder m_sb = new StringBuilder( 4096);

	public void send(int a) {
		send( a == Integer.MAX_VALUE ? "" : String.valueOf( a) );
	}

	public void send(double a) {
		send( a == Double.MAX_VALUE ? "" : String.valueOf( a) );
	}

	public void send( boolean a) {
		send( a ? 1 : 0);
	}

	public void send( IApiEnum a) {
		send( a.getApiString() );
	}

	public void send( String a) {
		if (a != null) {
			m_sb.append( a);
		}
		m_sb.append( SEP);
	}

	public String toString() {
		return m_sb.toString();
	}

	public byte[] getBytes() {
		return m_sb.toString().getBytes();
	}
}
