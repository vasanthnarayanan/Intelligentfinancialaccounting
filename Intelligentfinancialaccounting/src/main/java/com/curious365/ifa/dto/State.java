package com.curious365.ifa.dto;

import java.io.Serializable;

public class State implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2696914244177328937L;
	
	private long stateCode;
	private String stateName;
	private String stateInitial;
	private String stateType;
	public long getStateCode() {
		return stateCode;
	}
	public void setStateCode(long stateCode) {
		this.stateCode = stateCode;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getStateInitial() {
		return stateInitial;
	}
	public void setStateInitial(String stateInitial) {
		this.stateInitial = stateInitial;
	}
	public String getStateType() {
		return stateType;
	}
	public void setStateType(String stateType) {
		this.stateType = stateType;
	}

}
