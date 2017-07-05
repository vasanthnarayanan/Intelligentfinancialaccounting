package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.List;

public class FaultForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8358977764186902263L;
	private String faultCustomerName;
	private String faultDate;
	private double cashPaid;
	public String getFaultDate() {
		return faultDate;
	}

	public void setFaultDate(String faultDate) {
		this.faultDate = faultDate;
	}

	public String getInvoiceRemarks() {
		return invoiceRemarks;
	}

	public void setInvoiceRemarks(String invoiceRemarks) {
		this.invoiceRemarks = invoiceRemarks;
	}

	private String invoiceRemarks;
	public String getFaultCustomerName() {
		return faultCustomerName;
	}

	public void setFaultCustomerName(String faultCustomerName) {
		this.faultCustomerName = faultCustomerName;
	}

	public long getFaultCustomerId() {
		return faultCustomerId;
	}

	public void setFaultCustomerId(long faultCustomerId) {
		this.faultCustomerId = faultCustomerId;
	}

	private long faultCustomerId;
	private List<Fault> records;

	public List<Fault> getRecords() {
		return records;
	}

	public void setRecords(List<Fault> records) {
		this.records = records;
	}

	public double getCashPaid() {
		return cashPaid;
	}

	public void setCashPaid(double cashPaid) {
		this.cashPaid = cashPaid;
	}
}
