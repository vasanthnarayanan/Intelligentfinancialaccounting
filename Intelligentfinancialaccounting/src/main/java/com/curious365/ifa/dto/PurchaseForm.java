package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.List;

public class PurchaseForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8358977764186902263L;
	private String purchaseCustomerName;
	private String purchaseDate;
	private double cashPaid;
	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getInvoiceRemarks() {
		return invoiceRemarks;
	}

	public void setInvoiceRemarks(String invoiceRemarks) {
		this.invoiceRemarks = invoiceRemarks;
	}

	private String invoiceRemarks;
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

	public double getCashPaid() {
		return cashPaid;
	}

	public void setCashPaid(double cashPaid) {
		this.cashPaid = cashPaid;
	}
}
