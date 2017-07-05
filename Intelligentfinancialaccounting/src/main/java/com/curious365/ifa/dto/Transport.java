package com.curious365.ifa.dto;

import java.io.Serializable;

public class Transport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7430153450350247845L;
	
	private long transportId;
	private String transportName;
	private String transportAddress;
	private String transportPhone;
	private String vehicleNo;
	
	public long getTransportId() {
		return transportId;
	}
	public void setTransportId(long transportId) {
		this.transportId = transportId;
	}
	public String getTransportName() {
		return transportName;
	}
	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}
	public String getTransportAddress() {
		return transportAddress;
	}
	public void setTransportAddress(String transportAddress) {
		this.transportAddress = transportAddress;
	}
	public String getTransportPhone() {
		return transportPhone;
	}
	public void setTransportPhone(String transportPhone) {
		this.transportPhone = transportPhone;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

}
