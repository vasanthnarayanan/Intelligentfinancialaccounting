package com.curious365.ifa.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Accounts {
private List<String> dispDates = new ArrayList<String>();
public List<String> getDispDates() {
	return dispDates;
}
public void setDispDates(List<String> dispDates) {
	this.dispDates = dispDates;
}
public Map<String, List<Content>> getContent() {
	return content;
}
public void setContent(Map<String, List<Content>> content) {
	this.content = content;
}
public double getTotal() {
	return total;
}
public void setTotal(double total) {
	this.total = total;
}
public double getTotalSalesSum() {
	return totalSalesSum;
}
public void setTotalSalesSum(double totalSalesSum) {
	this.totalSalesSum = totalSalesSum;
}
public double getTotalExpenditureSum() {
	return totalExpenditureSum;
}
public void setTotalExpenditureSum(double totalExpenditureSum) {
	this.totalExpenditureSum = totalExpenditureSum;
}
public double getGrossTotal() {
	return grossTotal;
}
public void setGrossTotal(double grossTotal) {
	this.grossTotal = grossTotal;
}
public double getPrevPageBalance() {
	return prevPageBalance;
}
public void setPrevPageBalance(double prevPageBalance) {
	this.prevPageBalance = prevPageBalance;
}
private Map<String,List<Content>> content = new LinkedHashMap<String, List<Content>>();
private double total;
private double totalSalesSum;
private double totalExpenditureSum;
private double grossTotal;
private double prevPageBalance;

}
