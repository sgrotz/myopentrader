package org.mot.web.view;

import java.io.Serializable;

public class OrderView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4399471585277120554L;
	private String symbol;
	private int quantity;
	private Double buyPrice;
	private Double sellPrice;
	private Double grossPNL;
	private Double txnCost;
	private Double netPNL;
	private Double pctPNL;
	private String strategy;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Double getGrossPNL() {
		return grossPNL;
	}

	public void setGrossPNL(Double grossPNL) {
		this.grossPNL = grossPNL;
	}

	public Double getTxnCost() {
		return txnCost;
	}

	public void setTxnCost(Double txnCost) {
		this.txnCost = txnCost;
	}

	public Double getNetPNL() {
		return netPNL;
	}

	public void setNetPNL(Double netPNL) {
		this.netPNL = netPNL;
	}

	public Double getPctPNL() {
		return pctPNL;
	}

	public void setPctPNL(Double pctPNL) {
		this.pctPNL = pctPNL;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

}
