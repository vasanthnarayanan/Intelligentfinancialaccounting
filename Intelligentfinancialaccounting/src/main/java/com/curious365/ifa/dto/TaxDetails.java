package com.curious365.ifa.dto;

import java.io.Serializable;

public class TaxDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8859397335894806070L;
	
	private int serialNo;
	private String hsnCode;
	private double bagQuantity;
	private double totalValue;
	private double taxableValue;
	private double cgst;
	private double cgstamount;
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public double getTaxableValue() {
		return taxableValue;
	}
	public void setTaxableValue(double taxableValue) {
		this.taxableValue = taxableValue;
	}
	public double getCgst() {
		return cgst;
	}
	public void setCgst(double cgst) {
		this.cgst = cgst;
	}
	public double getCgstamount() {
		return cgstamount;
	}
	public void setCgstamount(double cgstamount) {
		this.cgstamount = cgstamount;
	}
	public double getSgst() {
		return sgst;
	}
	public void setSgst(double sgst) {
		this.sgst = sgst;
	}
	public double getSgstamount() {
		return sgstamount;
	}
	public void setSgstamount(double sgstamount) {
		this.sgstamount = sgstamount;
	}
	public double getIgst() {
		return igst;
	}
	public void setIgst(double igst) {
		this.igst = igst;
	}
	public double getIgstamount() {
		return igstamount;
	}
	public void setIgstamount(double igstamount) {
		this.igstamount = igstamount;
	}
	private double sgst;
	private double sgstamount;
	private double igst;
	private double igstamount;

	
	@Override
	public boolean equals(Object object){
		boolean result = false;
		if (object == null || object.getClass() != getClass()) {
		      result = false;
		    } else {
		      TaxDetails method = (TaxDetails) object;
		      if (this.hsnCode == method.getHsnCode()
		          ) {
		        result = true;
		      }
		    }
		    return result;
	}
	
	@Override
    public int hashCode() {
    return this.hsnCode.hashCode();
	}
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	public double getBagQuantity() {
		return bagQuantity;
	}
	public void setBagQuantity(double bagQuantity) {
		this.bagQuantity = bagQuantity;
	}
	public double getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

}
