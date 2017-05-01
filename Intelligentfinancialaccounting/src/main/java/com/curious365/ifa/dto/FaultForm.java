package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.List;

public class FaultForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8358977764186902263L;
	private String faultCustomerName;
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
}
