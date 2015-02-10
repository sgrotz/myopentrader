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
  
  
  package org.mot.iab.controller;

public enum MarketValueTag {
	NetLiquidationByCurrency,
	CashBalance,
	TotalCashBalance,
	AccruedCash,
	StockMarketValue,
	OptionMarketValue,
	FutureOptionValue,
	FuturesPNL,
	UnrealizedPnL,
	RealizedPnL,
	ExchangeRate,
	FundValue,
	NetDividend,
	MutualFundValue,
	MoneyMarketFundValue,
	CorporateBondValue,
	TBondValue,
	TBillValue,
	WarrantValue,
	FxCashBalance;

	public static MarketValueTag get( int i) {
		return Types.getEnum( i, values() );
	}

	@Override public String toString() {
		switch( this) {
			case NetLiquidationByCurrency: return "Net Liq";
			case StockMarketValue: return "Stocks";
			case OptionMarketValue: return "Options";
			case FutureOptionValue: return "Futures";
		}
		return super.toString().replaceAll("Value", "");
	}
}
