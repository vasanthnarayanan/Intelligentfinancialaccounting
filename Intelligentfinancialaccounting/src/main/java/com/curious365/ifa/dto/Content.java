package com.curious365.ifa.dto;

import org.springframework.util.StringUtils;

public class Content{
private String recordType;
private long recordId;
private double cost;
private long pieces;
private double total;
private String recordDate;
private String name;
private String remarks;
public String getRecordType() {
	return recordType;
}
public void setRecordType(String recordType) {
	this.recordType = recordType;
}
public String getRecordDate() {
	return recordDate;
}
public void setRecordDate(String recordDate) {
	this.recordDate = recordDate;
}
private long customerId;
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}
public String getItemQuantity() {
	return itemQuantity;
}
public void setItemQuantity(String itemQuantity) {
	this.itemQuantity = itemQuantity;
}
public String getItemType() {
	return itemType;
}
public void setItemType(String itemType) {
	this.itemType = itemType;
}
private long itemId;
private String itemName;
private String itemQuantity;
private String itemType;

public long getRecordId() {
	return recordId;
}
public void setRecordId(long recordId) {
	this.recordId = recordId;
}
public double getCost() {
	return cost;
}
public void setCost(double cost) {
	this.cost = cost;
}
public long getPieces() {
	return pieces;
}
public void setPieces(long pieces) {
	this.pieces = pieces;
}
public double getTotal() {
	return total;
}
public void setTotal(double total) {
	this.total = total;
}
public long getCustomerId() {
	return customerId;
}
public void setCustomerId(long customerId) {
	this.customerId = customerId;
}
public long getItemId() {
	return itemId;
}
public void setItemId(long itemId) {
	this.itemId = itemId;
}
public String getName() {
	if(StringUtils.hasText(name)){
		name = name.toUpperCase();
	}
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getRemarks() {
	return remarks;
}
public void setRemarks(String remarks) {
	this.remarks = remarks;
}
}
