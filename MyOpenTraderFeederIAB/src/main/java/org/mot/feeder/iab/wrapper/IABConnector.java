

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
  
  
  package org.mot.feeder.iab.wrapper;

import org.mot.feeder.iab.wrapper.MarketDataWrapper;
import org.mot.iab.client.EClientSocket;
import org.mot.iab.client.EWrapper;

public class IABConnector {

	
	
	
	private static EClientSocket ecs;
	
	public static EClientSocket getInstance(){
		
		if (ecs == null){
			// Create a new sample wrapper
			EWrapper ew = new MarketDataWrapper();
			ecs = new EClientSocket(ew);
		}
		
		return ecs;
	}
	
	
}
