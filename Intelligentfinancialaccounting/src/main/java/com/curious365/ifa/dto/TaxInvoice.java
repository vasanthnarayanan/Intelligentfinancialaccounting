package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.List;

public class TaxInvoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5252915110198465903L;
	
	private long taxInvoiceId;
	private String taxInvoiceDate;
	private String taxInvoiceType;
	private String taxInvoiceCustomerName;
	private long taxInvoiceCustomerId;
	private long taxInvoiceTransportId;
	private double cashPaid;
	private long orderId;
	private String remarks;
	private String deliveryNote;
	private String termsOfPayment;
	private String termsOfDelivery;
	
	// consignee details
	private String consigneeName;
	private String consigneeAddress;
	private String consigneeState;
	private String consigneeStateCode;
	private String consigneeTaxUniqueId;
	
	// consignor details
	private String consignorAddress;
	private String consignorState;
	private String consignorStateCode;
	private String consignorTaxUniqueId;
	
	// sales summary
	private double totalCgst;
	private double totalSgst;
	private double totalIgst;
	private double roundOff;
	private double invoiceValue;
	private String invoiceValueInWords;
	private long totalQuantity;
		
	// tax summary
	private double totalTaxableValue;
	private double totalTax;
	private String totalTaxInWords;
	
	public double getTotalTaxableValue() {
		return totalTaxableValue;
	}
	public void setTotalTaxableValue(double totalTaxableValue) {
		this.totalTaxableValue = totalTaxableValue;
	}
	public double getTotalCgst() {
		return totalCgst;
	}
	public void setTotalCgst(double totalCgst) {
		this.totalCgst = totalCgst;
	}
	public double getTotalSgst() {
		return totalSgst;
	}
	public void setTotalSgst(double totalSgst) {
		this.totalSgst = totalSgst;
	}
	public double getTotalIgst() {
		return totalIgst;
	}
	public void setTotalIgst(double totalIgst) {
		this.totalIgst = totalIgst;
	}
	
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public String getConsigneeState() {
		return consigneeState;
	}
	public void setConsigneeState(String consigneeState) {
		this.consigneeState = consigneeState;
	}
	public String getConsigneeStateCode() {
		return consigneeStateCode;
	}
	public void setConsigneeStateCode(String consigneeStateCode) {
		this.consigneeStateCode = consigneeStateCode;
	}
	public String getConsigneeTaxUniqueId() {
		return consigneeTaxUniqueId;
	}
	public void setConsigneeTaxUniqueId(String consigneeTaxUniqueId) {
		this.consigneeTaxUniqueId = consigneeTaxUniqueId;
	}
	public String getConsignorAddress() {
		return consignorAddress;
	}
	public void setConsignorAddress(String consignorAddress) {
		this.consignorAddress = consignorAddress;
	}
	public String getConsignorState() {
		return consignorState;
	}
	public void setConsignorState(String consignorState) {
		this.consignorState = consignorState;
	}
	public String getConsignorStateCode() {
		return consignorStateCode;
	}
	public void setConsignorStateCode(String consignorStateCode) {
		this.consignorStateCode = consignorStateCode;
	}
	public String getConsignorTaxUniqueId() {
		return consignorTaxUniqueId;
	}
	public void setConsignorTaxUniqueId(String consignorTaxUniqueId) {
		this.consignorTaxUniqueId = consignorTaxUniqueId;
	}
	public double getRoundOff() {
		return roundOff;
	}
	public void setRoundOff(double roundOff) {
		this.roundOff = roundOff;
	}
	public double getInvoiceValue() {
		return invoiceValue;
	}
	public void setInvoiceValue(double invoiceValue) {
		this.invoiceValue = invoiceValue;
	}

	public long getTaxInvoiceId() {
		return taxInvoiceId;
	}
	public void setTaxInvoiceId(long taxInvoiceId) {
		this.taxInvoiceId = taxInvoiceId;
	}
	public String getTaxInvoiceDate() {
		return taxInvoiceDate;
	}
	public void setTaxInvoiceDate(String taxInvoiceDate) {
		this.taxInvoiceDate = taxInvoiceDate;
	}
	public String getTaxInvoiceType() {
		return taxInvoiceType;
	}
	public void setTaxInvoiceType(String taxInvoiceType) {
		this.taxInvoiceType = taxInvoiceType;
	}
	public String getTaxInvoiceCustomerName() {
		return taxInvoiceCustomerName;
	}
	public void setTaxInvoiceCustomerName(String taxInvoiceCustomerName) {
		this.taxInvoiceCustomerName = taxInvoiceCustomerName;
	}
	public long getTaxInvoiceCustomerId() {
		return taxInvoiceCustomerId;
	}
	public void setTaxInvoiceCustomerId(long taxInvoiceCustomerId) {
		this.taxInvoiceCustomerId = taxInvoiceCustomerId;
	}
	public long getTaxInvoiceTransportId() {
		return taxInvoiceTransportId;
	}
	public void setTaxInvoiceTransportId(long taxInvoiceTransportId) {
		this.taxInvoiceTransportId = taxInvoiceTransportId;
	}
	public double getCashPaid() {
		return cashPaid;
	}
	public void setCashPaid(double cashPaid) {
		this.cashPaid = cashPaid;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDeliveryNote() {
		return deliveryNote;
	}
	public void setDeliveryNote(String deliveryNote) {
		this.deliveryNote = deliveryNote;
	}
	public String getTermsOfPayment() {
		return termsOfPayment;
	}
	public void setTermsOfPayment(String termsOfPayment) {
		this.termsOfPayment = termsOfPayment;
	}
	public String getTermsOfDelivery() {
		return termsOfDelivery;
	}
	public void setTermsOfDelivery(String termsOfDelivery) {
		this.termsOfDelivery = termsOfDelivery;
	}
	public List<?> getRecords() {
		return records;
	}
	public void setRecords(List<?> records) {
		this.records = records;
	}
	public long getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public double getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}
	public String getInvoiceValueInWords() {
		return invoiceValueInWords;
	}
	public void setInvoiceValueInWords(String string) {
		this.invoiceValueInWords = string;
	}
	public String getTotalTaxInWords() {
		return totalTaxInWords;
	}
	public void setTotalTaxInWords(String totalTaxInWords) {
		this.totalTaxInWords = totalTaxInWords;
	}
	private List<?> records;

}
