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
	private List<?> records;

}
