package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InvoiceAutocomplete implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1400910967088721915L;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<Invoice> getData() {
		return data;
	}
	public void setData(Invoice data) {
		this.data.add(data);
	}
	
	private String value;
	private List<Invoice> data = new ArrayList<Invoice>();

}
