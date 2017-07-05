package com.curious365.ifa.dto;

import org.springframework.util.StringUtils;


public class Transaction {
private Long transactionRecordId;
private String transactionDate;
private String transactionDateAlt;
private String transactionCustomerName;
private Long transactionCustomerId;
private Double transactionAmount;
private String isIncome;
private int activeFlag;
private String transactionRemarks;
private String dueDate;
private String modeOfPayment;
private String transactionStatus;
private String chequeNumber;
private Long chequeBankId;
private Long refCustomerId;
private String refCustomerName;
private Long bankId;

public String getChequeNumber() {
	return chequeNumber;
}
public void setChequeNumber(String chequeNumber) {
	this.chequeNumber = chequeNumber;
}
public Long getRefCustomerId() {
	return refCustomerId;
}
public void setRefCustomerId(Long refCustomerId) {
	this.refCustomerId = refCustomerId;
}
public Long getBankId() {
	return bankId;
}
public void setBankId(Long bankId) {
	this.bankId = bankId;
}
public Long getTransactionRecordId() {
	return transactionRecordId;
}
public void setTransactionRecordId(Long transactionRecordId) {
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
public Double getTransactionAmount() {
	return transactionAmount;
}
public void setTransactionAmount(Double transactionAmount) {
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
public Long getTransactionCustomerId() {
	return transactionCustomerId;
}
public void setTransactionCustomerId(Long transactionCustomerId) {
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

public String getDueDate() {
	return dueDate;
}
public void setDueDate(String dueDate) {
	this.dueDate = dueDate;
}
public String getTransactionStatus() {
	return transactionStatus;
}
public void setTransactionStatus(String transactionStatus) {
	this.transactionStatus = transactionStatus;
}
public Long getChequeBankId() {
	return chequeBankId;
}
public void setChequeBankId(Long chequeBankId) {
	this.chequeBankId = chequeBankId;
}
public String getRefCustomerName() {
	return refCustomerName;
}
public void setRefCustomerName(String refCustomerName) {
	this.refCustomerName = refCustomerName;
}
public String getModeOfPayment() {
	return modeOfPayment;
}
public void setModeOfPayment(String modeOfPayment) {
	this.modeOfPayment = modeOfPayment;
}

}
