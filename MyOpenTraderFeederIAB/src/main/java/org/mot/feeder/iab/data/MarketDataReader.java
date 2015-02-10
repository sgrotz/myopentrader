

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
  
  
  package org.mot.feeder.iab.data;

import org.mot.common.db.WatchListDAO;
import org.mot.iab.client.Contract;
import org.mot.iab.client.EClientSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarketDataReader {

	private String Instrument; 
	private String currency;
	private Integer Reference; 
	private EClientSocket ecs; 
	Logger logger = LoggerFactory.getLogger(getClass());
	WatchListDAO wld = new WatchListDAO();

	

	/**
	 * Use this constructor to create a new MarketData Reader - this will subscribe to the current live stream - but also 
	 * collect historic data.
	 * 
	 * @param ecs - EClientSocket to connect
	 * @param reference - Reference number
	 * @param instrument - Instrument to query
	 */
	public MarketDataReader(EClientSocket ecs, Integer reference, String instrument, String currency) {
		this.Instrument = instrument;
		this.currency = currency;
		this.Reference = reference;
		this.ecs= ecs;  
		getData();
	}
	
	private void getData() {

		// Create a new contract
		Contract con = new Contract();
		
		
		con.m_currency = this.currency;
		String secType = wld.getTypeForID( this.Reference);
		con.m_exchange =wld.getExchangeForID(this.Reference);
		con.m_symbol = this.Instrument;
		
		if (secType.equals("ETF")) {
			// ETF use STK as well
			secType = "STK";
		} else if (secType.equals("FX")) {
			// Map FX to Cash
			secType = "CASH";
			
			// Only take the first 3 letters
			con.m_symbol = this.Instrument.substring(0, 3);
		}
		
		//Overwrite the secType
		con.m_secType = secType;
		
		// Subscribe to all positions
		ecs.reqAccountUpdates(true, "0");
		
		// Subscribe to the current mkt data feed
		ecs.reqMktData(this.Reference, con, "233", false);
		
		// Seems as if historic data requests for indeces are failing
		if (!con.m_secType.equals("IND") && !con.m_secType.equals("CASH")) {
			new HistoricMarketDataFactory(Instrument, ecs, con);
		}
		
		
		
			
	}

}
