package com.curious365.ifa.common;

public enum ModeOfPayment {
CASH("CASH"),CHEQUE("CHEQUE"),NEFT("NEFT");
	
	private String value;
	
	ModeOfPayment(String type){
		this.value = type;
	}
	public String getValue() {
		return value;
	}
}
