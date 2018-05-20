package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Datatable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5404073006379109750L;
	
	private int draw;
	private int start;
	private int length;
	private long rowCount;
	private long filtered;
	private long customerId;
	private double initialBalance;
	private Map<String,String> search = new HashMap<String,String>();
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public long getRowCount() {
		return rowCount;
	}
	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
	public double getInitialBalance() {
		return initialBalance;
	}
	public void setInitialBalance(double initialBalance) {
		this.initialBalance = initialBalance;
	}
	public Map<String,String> getSearch() {
		return search;
	}
	public void setSearch(Map<String,String> search) {
		this.search = search;
	}
	public long getFiltered() {
		return filtered;
	}
	public void setFiltered(long filtered) {
		this.filtered = filtered;
	}

}
