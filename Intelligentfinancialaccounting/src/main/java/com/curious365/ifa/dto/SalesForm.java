package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.List;

public class SalesForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8358977764186902263L;
	private String salesCustomerName;
	private String salesDate;
	private double cashPaid;
	private String invoiceRemarks;
	public String getInvoiceRemarks() {
		return invoiceRemarks;
	}

	public void setInvoiceRemarks(String invoiceRemarks) {
		this.invoiceRemarks = invoiceRemarks;
	}

	private int explicitTax=0;
	public String getSalesCustomerName() {
		return salesCustomerName;
	}

	public void setSalesCustomerName(String salesCustomerName) {
		this.salesCustomerName = salesCustomerName;
	}

	public long getSalesCustomerId() {
		return salesCustomerId;
	}

	public void setSalesCustomerId(long salesCustomerId) {
		this.salesCustomerId = salesCustomerId;
	}

	private long salesCustomerId;
	private List<Sales> records;

	public List<Sales> getRecords() {
		return records;
	}

	public void setRecords(List<Sales> records) {
		this.records = records;
	}

	public int getExplicitTax() {
		return explicitTax;
	}

	public void setExplicitTax(int explicitTax) {
		this.explicitTax = explicitTax;
	}

	public String getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(String salesDate) {
		this.salesDate = salesDate;
	}

	public double getCashPaid() {
		return cashPaid;
	}

	public void setCashPaid(double cashPaid) {
		this.cashPaid = cashPaid;
	}
}
