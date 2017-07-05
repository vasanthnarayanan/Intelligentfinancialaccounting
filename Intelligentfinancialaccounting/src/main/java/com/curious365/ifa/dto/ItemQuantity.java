package com.curious365.ifa.dto;

import java.io.Serializable;

public class ItemQuantity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2809978227429108241L;
	
	private String quantityId;
	private long quantity;
	
	public String getQuantityId() {
		return quantityId;
	}
	public void setQuantityId(String quantityId) {
		this.quantityId = quantityId;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

}
