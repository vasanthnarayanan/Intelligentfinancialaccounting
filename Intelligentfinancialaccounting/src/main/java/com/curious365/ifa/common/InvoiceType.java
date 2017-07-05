package com.curious365.ifa.common;

public enum InvoiceType {
SALES("SALES"),PURCHASE("PURCHASE"),FAULT("FAULT");
	
	private String value;
	
	InvoiceType(String status){
		this.value = status;
	}
	public String getValue() {
		return value;
	}
}
