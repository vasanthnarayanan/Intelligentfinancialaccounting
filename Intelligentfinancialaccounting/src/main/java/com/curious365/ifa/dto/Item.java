package com.curious365.ifa.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9166394661500298395L;
	private String itemId;
	private String itemName;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		if(StringUtils.hasText(itemName)){
			itemName = itemName.toLowerCase();
		}
		this.itemName = itemName;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public long getStock() {
		return stock;
	}
	public void setStock(long stock) {
		this.stock = stock;
	}
	
	public int getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}

	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public long getQuantityId() {
		return quantityId;
	}
	public void setQuantityId(long quantityId) {
		this.quantityId = quantityId;
	}

	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	private long quantityId;
	private long quantity;
	private String type;
	private long typeId;
	private double cost;
	private double taxRate;
	private long stock;
	private String vendor;
	private int activeFlag;
}
