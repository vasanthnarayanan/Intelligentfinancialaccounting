package com.curious365.ifa.dto;

import org.springframework.util.StringUtils;


public class Transaction {
private long transactionRecordId;
private String transactionDate;
private String transactionDateAlt;
private String transactionCustomerName;
private long transactionCustomerId;
private double transactionAmount;
private String isIncome;
private int activeFlag;
private String transactionRemarks;
public long getTransactionRecordId() {
	return transactionRecordId;
}
public void setTransactionRecordId(long transactionRecordId) {
	this.transactionRecordId = transactionRecordId;
}
public String getTransactionDate() {
	return transactionDate;
}
public void setTransactionDate(String transactionDate) {
	this.transactionDate = transactionDate;
}
public String getTransactionCustomerName() {
	if(StringUtils.hasText(transactionCustomerName)){
		transactionCustomerName = transactionCustomerName.toUpperCase();
	}
	return transactionCustomerName;
}
public void setTransactionCustomerName(String transactionCustomerName) {
	this.transactionCustomerName = transactionCustomerName;
}
public double getTransactionAmount() {
	return transactionAmount;
}
public void setTransactionAmount(double transactionAmount) {
	this.transactionAmount = transactionAmount;
}
public String getIsIncome() {
	return isIncome;
}
public void setIsIncome(String isIncome) {
	this.isIncome = isIncome;
}
public int getActiveFlag() {
	return activeFlag;
}
public void setActiveFlag(int activeFlag) {
	this.activeFlag = activeFlag;
}

@Override
	public String toString() {
		return "Transaction Id: " + transactionRecordId + " Customer name: "
				+ transactionCustomerName + " Date: " + transactionDate
				+ " Amount: " + transactionAmount + " Income: " + isIncome
				+ " Active Flag: " + activeFlag;

	}
public String getTransactionDateAlt() {
	return transactionDateAlt;
}
public void setTransactionDateAlt(String transactionDateAlt) {
	this.transactionDateAlt = transactionDateAlt;
}
public long getTransactionCustomerId() {
	return transactionCustomerId;
}
public void setTransactionCustomerId(long transactionCustomerId) {
	this.transactionCustomerId = transactionCustomerId;
}
public String getTransactionRemarks() {
	if(!StringUtils.hasText(transactionRemarks)){
		transactionRemarks = "";
	}
	return transactionRemarks;
}
public void setTransactionRemarks(String transactionRemarks) {
	this.transactionRemarks = transactionRemarks;
}

}
