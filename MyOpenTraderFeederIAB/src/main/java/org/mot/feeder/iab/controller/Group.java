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

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.mot.feeder.iab.controller.Types.Method;

public class Group {
	private String m_name;
	private Method m_defaultMethod;
	private ArrayList<String> m_accounts = new ArrayList<String>();

	public String name() 					{ return m_name; }
	public Method defaultMethod() 			{ return m_defaultMethod; }
	public ArrayList<String> accounts() 	{ return m_accounts; }

	public void name( String v) 			{ m_name = v; }
	public void defaultMethod( Method v) 	{ m_defaultMethod = v; }
	public void addAccount( String acct) 	{ m_accounts.add( acct); }

	/** @param val is a comma or space delimited string of accounts */
	public void setAllAccounts(String val) {
		m_accounts.clear();

		StringTokenizer st = new StringTokenizer( val, " ,");
		while( st.hasMoreTokens() ) {
			m_accounts.add( st.nextToken() );
		}
	}
}
