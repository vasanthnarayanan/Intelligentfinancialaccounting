package com.curious365.ifa.dto;

import java.io.Serializable;

public class Autocomplete implements Serializable {
public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
/**
	 * 
	 */
	private static final long serialVersionUID = -3391704043332041869L;
private String value;
private String data;
}
