package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemAutocomplete implements Serializable {
public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<Item> getData() {
		return data;
	}
	public void setData(Item data) {
		this.data.add(data);
	}
/**
	 * 
	 */
	private static final long serialVersionUID = -3391704043332041869L;
private String value;
private List<Item> data = new ArrayList<Item>();
}
