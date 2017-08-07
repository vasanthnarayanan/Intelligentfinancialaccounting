package com.curious365.ifa.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class Fault implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4471571304940948851L;
	private long faultRecordId;
	private String faultDate;
	private String faultCustomerName;
	private long faultCustomerId;
	private long faultItemId;
	private String faultItemName;
	private String faultItemQuantity;
	private String faultItemType;
	private double faultTotal;
	private double cashPaid;
	public double getFaultTaxRate() {
		return faultTaxRate;
	}
	public void setFaultTaxRate(double faultTaxRate) {
		this.faultTaxRate = faultTaxRate;
	}
	public double getFaultTax() {
		return faultTax;
	}
	public void setFaultTax(double faultTax) {
		this.faultTax = faultTax;
	}
	public long getFaultStock() {
		return faultStock;
	}
	public void setFaultStock(long faultStock) {
		this.faultStock = faultStock;
	}
	public double getFaultTotal() {
		return getFaultAmount()+getFaultTax();
	}
	public double getFaultAmount() {
		return faultPieces*faultCost;
	}
	private double faultAmount;
	private double faultTaxRate;
	private double faultTax;
	private long invoiceId;
	private long faultStock;
	public String getFaultItemQuantity() {
		return faultItemQuantity;
	}
	public void setFaultItemQuantity(String faultItemQuantity) {
		this.faultItemQuantity = faultItemQuantity;
	}
	public String getFaultItemType() {
		return faultItemType;
	}
	public void setFaultItemType(String faultItemType) {
		this.faultItemType = faultItemType;
	}
	private long faultPieces;
	private double faultCost;
	private int activeFlag;
	private String faultRemarks;
	public long getFaultRecordId() {
		return faultRecordId;
	}
	public void setFaultRecordId(long faultRecordId) {
		this.faultRecordId = faultRecordId;
	}
	public String getFaultDate() {
		return faultDate;
	}
	public void setFaultDate(String faultDate) {
		this.faultDate = faultDate;
	}
	public String getFaultCustomerName() {
		if(StringUtils.hasText(faultCustomerName)){
			faultCustomerName = faultCustomerName.toUpperCase();
		}
		return faultCustomerName;
	}
	public void setFaultCustomerName(String faultCustomerName) {
		this.faultCustomerName = faultCustomerName;
	}
	public long getFaultItemId() {
		return faultItemId;
	}
	public void setFaultItemId(long faultItemId) {
		this.faultItemId = faultItemId;
	}
	public long getFaultPieces() {
		return faultPieces;
	}
	public void setFaultPieces(long faultPieces) {
		this.faultPieces = faultPieces;
	}
	public double getFaultCost() {
		return faultCost;
	}
	public void setFaultCost(double faultCost) {
		this.faultCost = faultCost;
	}
	public int getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}
	public long getFaultCustomerId() {
		return faultCustomerId;
	}
	public void setFaultCustomerId(long faultCustomerId) {
		this.faultCustomerId = faultCustomerId;
	}
	public String getFaultItemName() {
		if(StringUtils.hasText(faultItemName)){
			faultItemName = faultItemName.toUpperCase();
		}
		return faultItemName;
	}
	public void setFaultItemName(String faultItemName) {
		this.faultItemName = faultItemName;
	}
	public String getFaultRemarks() {
		if(!StringUtils.hasText(faultRemarks)){
			faultRemarks = "";
		}
		return faultRemarks;
	}
	public void setFaultRemarks(String faultRemarks) {
		this.faultRemarks = faultRemarks;
	}
	public double getCashPaid() {
		return cashPaid;
	}
	public void setCashPaid(double cashPaid) {
		this.cashPaid = cashPaid;
	}
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
}
