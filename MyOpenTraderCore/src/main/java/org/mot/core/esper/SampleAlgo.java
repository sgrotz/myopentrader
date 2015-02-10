

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
  
  
  package org.mot.core.esper;

import java.util.ArrayList;

import org.mot.common.iface.StrategyInterface;
import org.mot.common.objects.Expression;
import org.mot.common.objects.LoadValue;
import org.mot.common.objects.Order;
import org.mot.common.objects.SimulationRequest;
import org.mot.common.objects.SimulationResponse;

import com.espertech.esper.client.EventBean;

public class SampleAlgo implements StrategyInterface {

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Expression> getEsperExpression() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SimulationResponse> processSimulationRequests(
			SimulationRequest sr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SimulationRequest> getSimulationRequests(String symbol,
			String className, String frequency, Integer quantity, String startDate,
			String endDate, Double txnPct, Double minProfit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup(String name, String symbol, LoadValue[] values,
			Boolean simulated, Integer amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean closeOrder(Order order, Double lastKnownPrice,
			Double diffPct, Long openMinutes) {
		// TODO Auto-generated method stub
		return null;
	}




}
