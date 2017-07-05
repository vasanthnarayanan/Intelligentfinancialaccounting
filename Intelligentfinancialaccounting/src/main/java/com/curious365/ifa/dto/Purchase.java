package com.curious365.ifa.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class Purchase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4471571304940948851L;
	private long purchaseRecordId;
	private String purchaseDate;
	private String purchaseCustomerName;
	private long purchaseCustomerId;
	private long purchaseItemId;
	private String purchaseItemName;
	private String purchaseItemQuantity;
	private String purchaseItemType;
	private double purchaseTotal;
	private double purchaseAmount;
	private double purchaseTaxRate;
	private double purchaseTax;
	public double getPurchaseTaxRate() {
		return purchaseTaxRate;
	}
	public void setPurchaseTaxRate(double purchaseTaxRate) {
		this.purchaseTaxRate = purchaseTaxRate;
	}
	public double getPurchaseTax() {
		return purchaseTax;
	}
	public void setPurchaseTax(double purchaseTax) {
		this.purchaseTax = purchaseTax;
	}
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	public long getPurchaseStock() {
		return purchaseStock;
	}
	public void setPurchaseStock(long purchaseStock) {
		this.purchaseStock = purchaseStock;
	}
	public double getPurchaseTotal() {
		return getPurchaseAmount()+getPurchaseTax();
	}
	public double getPurchaseAmount() {
		return purchasePieces*purchaseCost;
	}
	private long invoiceId;
	private long purchaseStock;
	public String getPurchaseItemQuantity() {
		return purchaseItemQuantity;
	}
	public void setPurchaseItemQuantity(String purchaseItemQuantity) {
		this.purchaseItemQuantity = purchaseItemQuantity;
	}
	public String getPurchaseItemType() {
		return purchaseItemType;
	}
	public void setPurchaseItemType(String purchaseItemType) {
		this.purchaseItemType = purchaseItemType;
	}
	private long purchasePieces;
	private double purchaseCost;
	private int activeFlag;
	private String purchaseRemarks;
	public long getPurchaseRecordId() {
		return purchaseRecordId;
	}
	public void setPurchaseRecordId(long purchaseRecordId) {
		this.purchaseRecordId = purchaseRecordId;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getPurchaseCustomerName() {
		if(StringUtils.hasText(purchaseCustomerName)){
			purchaseCustomerName = purchaseCustomerName.toUpperCase();
		}
		return purchaseCustomerName;
	}
	public void setPurchaseCustomerName(String purchaseCustomerName) {
		this.purchaseCustomerName = purchaseCustomerName;
	}
	public long getPurchaseItemId() {
		return purchaseItemId;
	}
	public void setPurchaseItemId(long purchaseItemId) {
		this.purchaseItemId = purchaseItemId;
	}
	public long getPurchasePieces() {
		return purchasePieces;
	}
	public void setPurchasePieces(long purchasePieces) {
		this.purchasePieces = purchasePieces;
	}
	public double getPurchaseCost() {
		return purchaseCost;
	}
	public void setPurchaseCost(double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}
	public int getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}
	public long getPurchaseCustomerId() {
		return purchaseCustomerId;
	}
	public void setPurchaseCustomerId(long purchaseCustomerId) {
		this.purchaseCustomerId = purchaseCustomerId;
	}
	public String getPurchaseItemName() {
		if(StringUtils.hasText(purchaseItemName)){
			purchaseItemName = purchaseItemName.toUpperCase();
		}
		return purchaseItemName;
	}
	public void setPurchaseItemName(String purchaseItemName) {
		this.purchaseItemName = purchaseItemName;
	}
	public String getPurchaseRemarks() {
		if(!StringUtils.hasText(purchaseRemarks)){
			purchaseRemarks = "";
		}
		return purchaseRemarks;
	}
	public void setPurchaseRemarks(String purchaseRemarks) {
		this.purchaseRemarks = purchaseRemarks;
	}
	
}
