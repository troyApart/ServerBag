package com.troyApart.serverbag;

import java.text.DecimalFormat;

public class SalesReport {
	private String date;
	private double dayCashTotal, nightCashTotal, dayCreditTotal, nightCreditTotal, dayCashTipTotal, nightCashTipTotal, dayCreditTipTotal, nightCreditTipTotal, dayTipOut, nightTipOut;
	
	public SalesReport() {
		// TODO Auto-generated constructor stub
		
	}
	
	public SalesReport(String date, double dayCashTotal, double nightCashTotal,
			double dayCreditTotal, double nightCreditTotal,
			double dayCashTipTotal, double nightCashTipTotal,
			double dayCreditTipTotal, double nightCreditTipTotal) {
		super();
		this.date = date;
		this.dayCashTotal = dayCashTotal;
		this.nightCashTotal = nightCashTotal;
		this.dayCreditTotal = dayCreditTotal;
		this.nightCreditTotal = nightCreditTotal;
		this.dayCashTipTotal = dayCashTipTotal;
		this.nightCashTipTotal = nightCashTipTotal;
		this.dayCreditTipTotal = dayCreditTipTotal;
		this.nightCreditTipTotal = nightCreditTipTotal;
		this.dayTipOut = 0;
		this.nightTipOut = 0;
	}
	
	public double getFinalTips() {
		return (dayCashTipTotal + dayCreditTipTotal + nightCashTipTotal + nightCreditTipTotal) - (dayTipOut + nightTipOut);
	}
	
	public double getNightTipOut(double percentageOfTips) {
		nightTipOut = (nightCashTipTotal + nightCreditTipTotal) * percentageOfTips;
		return nightTipOut;
	}
	
	public double getDayTipOut(double percentageOfTips) {
		dayTipOut = (dayCashTipTotal + dayCreditTipTotal + nightCashTipTotal + nightCreditTipTotal) * percentageOfTips;
		return dayTipOut;
	}
	
	public double getTipPercentage() {
		return Double.valueOf(new DecimalFormat("#.##").format((dayCashTipTotal + dayCreditTipTotal + nightCashTipTotal + nightCreditTipTotal)/(dayCashTotal + dayCreditTotal + nightCashTotal + nightCreditTotal)));
	}
	
	public double getTotalTurnIn() {
		return (dayCashTotal + nightCashTotal) - (dayCreditTipTotal + nightCreditTipTotal); 
	}
	
	public double getTotalTips() {
		return dayCashTipTotal + dayCreditTipTotal + nightCashTipTotal + nightCreditTipTotal;
	}
	
	public double getTotalSales() {
		return dayCashTotal + dayCreditTotal + nightCashTotal + nightCreditTotal;
	}
	
	public double getNightTotalTurnIn() {
		return nightCashTotal - nightCreditTipTotal; 
	}
	
	public double getNightTotalTips() {
		return nightCashTipTotal + nightCreditTipTotal;
	}
	
	public double getNightTotalSales() {
		return nightCashTotal + nightCreditTotal;
	}
	
	public double getDayTotalTurnIn() {
		return dayCashTotal - dayCreditTipTotal; 
	}
	
	public double getDayTotalTips() {
		return dayCashTipTotal + dayCreditTipTotal;
	}
	
	public double getDayTotalSales() {
		return dayCashTotal + dayCreditTotal; 
	}
	
	/*
	 * Getters and Setters
	 */
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getDayCashTotal() {
		return dayCashTotal;
	}
	public void setDayCashTotal(double dayCashTotal) {
		this.dayCashTotal = dayCashTotal;
	}
	public double getNightCashTotal() {
		return nightCashTotal;
	}
	public void setNightCashTotal(double nightCashTotal) {
		this.nightCashTotal = nightCashTotal;
	}
	public double getDayCreditTotal() {
		return dayCreditTotal;
	}
	public void setDayCreditTotal(double dayCreditTotal) {
		this.dayCreditTotal = dayCreditTotal;
	}
	public double getNightCreditTotal() {
		return nightCreditTotal;
	}
	public void setNightCreditTotal(double nightCreditTotal) {
		this.nightCreditTotal = nightCreditTotal;
	}
	public double getDayCashTipTotal() {
		return dayCashTipTotal;
	}
	public void setDayCashTipTotal(double dayCashTipTotal) {
		this.dayCashTipTotal = dayCashTipTotal;
	}
	public double getNightCashTipTotal() {
		return nightCashTipTotal;
	}
	public void setNightCashTipTotal(double nightCashTipTotal) {
		this.nightCashTipTotal = nightCashTipTotal;
	}
	public double getDayCreditTipTotal() {
		return dayCreditTipTotal;
	}
	public void setDayCreditTipTotal(double dayCreditTipTotal) {
		this.dayCreditTipTotal = dayCreditTipTotal;
	}
	public double getNightCreditTipTotal() {
		return nightCreditTipTotal;
	}
	public void setNightCreditTipTotal(double nightCreditTipTotal) {
		this.nightCreditTipTotal = nightCreditTipTotal;
	}
}
