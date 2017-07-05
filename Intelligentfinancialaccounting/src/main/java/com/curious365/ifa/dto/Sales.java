package com.curious365.ifa.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class Sales implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4471571304940948851L;
	private long salesRecordId;
	private String salesDate;
	private String salesCustomerName;
	private long salesCustomerId;
	private long salesItemId;
	private double salesTotal;
	private double salesAmount;
	private double salesTaxRate;
	private double salesTax;
	public double getSalesTaxRate() {
		return salesTaxRate;
	}
	public void setSalesTaxRate(double salesTaxRate) {
		this.salesTaxRate = salesTaxRate;
	}
	public double getSalesTax() {
		return salesTax;
	}
	public void setSalesTax(double salesTax) {
		this.salesTax = salesTax;
	}
	private long invoiceId;
	private String salesItemName;
	private String salesItemQuantity;
	private String salesItemType;
	public String getSalesItemQuantity() {
		return salesItemQuantity;
	}
	public void setSalesItemQuantity(String salesItemQuantity) {
		this.salesItemQuantity = salesItemQuantity;
	}
	public String getSalesItemType() {
		return salesItemType;
	}
	public void setSalesItemType(String salesItemType) {
		this.salesItemType = salesItemType;
	}
	private long salesPieces;
	private long salesStock;
	private double salesCost;
	private int activeFlag;
	private String salesRemarks;
	public long getSalesRecordId() {
		return salesRecordId;
	}
	public void setSalesRecordId(long salesRecordId) {
		this.salesRecordId = salesRecordId;
	}
	public String getSalesDate() {
		return salesDate;
	}
	public void setSalesDate(String salesDate) {
		this.salesDate = salesDate;
	}
	public String getSalesCustomerName() {
		if(StringUtils.hasText(salesCustomerName)){
			salesCustomerName = salesCustomerName.toUpperCase();
		}
		return salesCustomerName;
	}
	public void setSalesCustomerName(String salesCustomerName) {
		this.salesCustomerName = salesCustomerName;
	}
	public long getSalesItemId() {
		return salesItemId;
	}
	public void setSalesItemId(long salesItemId) {
		this.salesItemId = salesItemId;
	}
	public long getSalesPieces() {
		return salesPieces;
	}
	public void setSalesPieces(long salesPieces) {
		this.salesPieces = salesPieces;
	}
	public double getSalesCost() {
		return salesCost;
	}
	public void setSalesCost(double salesCost) {
		this.salesCost = salesCost;
	}
	public int getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}
	public long getSalesCustomerId() {
		return salesCustomerId;
	}
	public void setSalesCustomerId(long salesCustomerId) {
		this.salesCustomerId = salesCustomerId;
	}
	public String getSalesItemName() {
		if(StringUtils.hasText(salesItemName)){
			salesItemName = salesItemName.toUpperCase();
		}
		return salesItemName;
	}
	public void setSalesItemName(String salesItemName) {
		this.salesItemName = salesItemName;
	}
	public String getSalesRemarks() {
		if(!StringUtils.hasText(salesRemarks)){
			salesRemarks = "";
		}
		return salesRemarks;
	}
	public void setSalesRemarks(String salesRemarks) {
		this.salesRemarks = salesRemarks;
	}
	public long getSalesStock() {
		return salesStock;
	}
	public void setSalesStock(long salesStock) {
		this.salesStock = salesStock;
	}
	public double getSalesTotal() {
		return getSalesAmount()+getSalesTax();
	}
	public double getSalesAmount() {
		return salesCost*salesPieces;
	}
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
}
