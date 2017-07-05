package com.curious365.ifa.common;

public enum TransactionStatus {
COMPLETE("COMPLETE"),PENDING("PENDING"),REJECTED("REJECTED");
	
	private String value;
	
	TransactionStatus(String status){
		this.value = status;
	}
	public String getValue() {
		return value;
	}
}
