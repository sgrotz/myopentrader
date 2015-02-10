

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


package org.mot.common.util;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mot.common.db.OrderDAO;
import org.mot.common.math.CalculatorFactory;
import org.mot.common.objects.Order;
import org.mot.common.objects.StrategyAnalysis;
import org.mot.common.tools.PropertiesFactory;

public class StrategyAnalyser {

	Logger logger = Logger.getLogger(getClass());
	OrderDAO od = new OrderDAO();
	CalculatorFactory cf = new CalculatorFactory();

	public static void main (String[] args) {

		PropertiesFactory pf = PropertiesFactory.getInstance();
		pf.setConfigDir(args[0]);

		System.setProperty("PathToConfigDir", pf.getConfigDir());
		PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");

		StrategyAnalyser sa = new StrategyAnalyser();
		CalculatorFactory cf = new CalculatorFactory();

		ArrayList<StrategyAnalysis> salist = sa.analyseAll();

		Double pnl = 0.0;
		Double txn = 0.0;
		Double invest = 0.0;
		int tradeCount = 0;
		
		for (int i = 0; i < salist.size();i++) {
			StrategyAnalysis single = salist.get(i);
			System.out.println("Strategy: " + single.getName() + " - PNL " + single.getPnL() + " - TxnCosts: " + single.getTxnCost() + " - Trades: " + single.getTradeCount());
			invest = invest + (single.getQuantity() * single.getPrice());
			pnl = pnl + single.getPnL();
			txn = txn + single.getTxnCost();
			tradeCount = tradeCount + single.getTradeCount();
		}
		
		OrderDAO od = new OrderDAO();
		DateBuilder db = new DateBuilder();
		
		String startDate = od.getFirstOrderDate();
		String endDate = od.getLastOrderDate();
		Long diffDays = db.calculateDifferenceInDays(startDate, endDate, "yyyy-MM-dd HH:mm:ss.SSS");
		
		if (diffDays == 0) {
			// Diffdays is always at least 1 
			diffDays = (long) 1;
		}
		
		Double total = pnl - txn;
		Double profitPct = total / invest * 100;
		
		System.out.println("*** PNL is: " + cf.round(pnl,2) + " minus the transaction costs of: " + cf.round(txn,2));
		System.out.println("* Total amount of trades: " + tradeCount);
		System.out.println("* First order date: " + startDate);
		System.out.println("* Last order date: " + endDate);
		System.out.println("* Total traded days: " + diffDays);
		System.out.println("* Total Win/Loss: " + cf.round(total,2)+ "$");
		System.out.println("* Total investment: " + cf.round(invest, 2)+ "$");
		System.out.println("* Profit vs Investment Pct: " + cf.round(profitPct,2) + "%");
		
		Double annualInvestPct = profitPct / diffDays * 200;
		System.out.println("* Investment Pct annualized: " + cf.round(annualInvestPct,2)+ "%");
		
		Double annualProfit = invest / 100 * annualInvestPct;
		System.out.println("* Profit annualized: " + cf.round(annualProfit,2)+ "$");
		System.out.println("***");
	}



	public ArrayList<StrategyAnalysis> analyseAll() {
		ArrayList<String> listOfStrategies = od.getAllClosedOrderStrategies();
		ArrayList<StrategyAnalysis> result = new ArrayList<StrategyAnalysis>(); 

		for (int p = 0; p < listOfStrategies.size(); p++) {

			ArrayList<StrategyAnalysis> a = this.analyseStrategy(listOfStrategies.get(p));

			for (int z = 0; z < a.size(); z++) {
				result.add(a.get(z));
			}
		}

		return result;
	}


	private ArrayList<StrategyAnalysis> analyseStrategy(String strategyName) {
		
		ArrayList<StrategyAnalysis> sa = new ArrayList<StrategyAnalysis>();

		Order[] o = od.getAllClosedOrdersByStrategy(strategyName);

		// If order array is 0 or less - ignore
		if (o.length != 0) {
			int size = o.length;
			Double pnl = 0.0;
			Double txnCost = 0.0;

			// Loop over all orders
			for (int i = 0; i < size; i++ ){
				int quantity = o[i].getQuantity();
				Double price = o[i].getPrice();


				// if order is a buy - deduct the overall amount from the pnl
				if (o[i].getBUYSELL().equals("BUY")) {
					pnl = pnl - (quantity * price); 
					txnCost = txnCost + (quantity * price * 0.0024); 
					logger.trace (i + "- Buying " + quantity + " trades@" + price + " -- P&L is: " + pnl);
				} else {
					// if it is a sell, add it to the pnl
					// must be a SELL
					pnl = pnl + (quantity * price);
					txnCost = txnCost + (quantity * price * 0.0024);
					logger.trace (i + "- Selling " + quantity + " trades@" + price + " -- P&L is: " + pnl);
				}
				

			}

			//	public StrategyAnalysis(String name, Double pnL, int tradeCount, int quantity)
			sa.add(new StrategyAnalysis(o[0].getStrategy(), cf.round(pnl, 3), size, o[0].getQuantity(), cf.round(o[0].getPrice(), 3), cf.round(txnCost, 3)));

		}

		return sa;

	}
	

}
