package com.curious365.ifa.dto;

import java.io.Serializable;

public class ItemType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3210911877530477246L;
	
	private String typeId;
	private String type;
	private double taxRate;
	private String hsnCode;
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	
	

}
