package com.curious365.ifa.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4704947604641847874L;
	
	private String customerId;
	private String name;
	private String customerAddress;
	private String customerPhoneNumber;
	private int activeFlag;
	private double initialBalance;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}
	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(StringUtils.hasText(name)){
			name = name.toLowerCase();
		}
		this.name = name;
	}
	public int getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}
	public double getInitialBalance() {
		return initialBalance;
	}
	public void setInitialBalance(double initialBalance) {
		this.initialBalance = initialBalance;
	}

}
