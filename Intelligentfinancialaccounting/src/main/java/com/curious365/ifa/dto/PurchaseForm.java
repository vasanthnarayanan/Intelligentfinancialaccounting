package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.List;

public class PurchaseForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8358977764186902263L;
	private String purchaseCustomerName;
	public String getPurchaseCustomerName() {
		return purchaseCustomerName;
	}

	public void setPurchaseCustomerName(String purchaseCustomerName) {
		this.purchaseCustomerName = purchaseCustomerName;
	}

	public long getPurchaseCustomerId() {
		return purchaseCustomerId;
	}

	public void setPurchaseCustomerId(long purchaseCustomerId) {
		this.purchaseCustomerId = purchaseCustomerId;
	}

	private long purchaseCustomerId;
	private List<Purchase> records;

	public List<Purchase> getRecords() {
		return records;
	}

	public void setRecords(List<Purchase> records) {
		this.records = records;
	}
}
