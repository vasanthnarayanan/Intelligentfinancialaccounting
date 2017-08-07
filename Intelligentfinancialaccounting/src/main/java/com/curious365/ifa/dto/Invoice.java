package com.curious365.ifa.dto;

import java.io.Serializable;
import java.util.List;

public class Invoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1970073294829181782L;
	
	private long invoiceId;
	private String invoiceDate;
	private String invoiceType;
	private String invoiceCustomerName;
	private long invoiceCustomerId;
	private long invoiceTransportId;
	private double cashPaid;
	private long orderId;
	private String remarks;
	private String deliveryNote;
	private String termsOfPayment;
	private String termsOfDelivery;
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
	private List<?> records;
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public long getInvoiceCustomerId() {
		return invoiceCustomerId;
	}
	public void setInvoiceCustomerId(long invoiceCustomerId) {
		this.invoiceCustomerId = invoiceCustomerId;
	}
	public long getInvoiceTransportId() {
		return invoiceTransportId;
	}
	public void setInvoiceTransportId(long invoiceTransportId) {
		this.invoiceTransportId = invoiceTransportId;
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
	public List<?> getRecords() {
		return records;
	}
	public void setRecords(List<?> records) {
		this.records = records;
	}
	public String getInvoiceCustomerName() {
		return invoiceCustomerName;
	}
	public void setInvoiceCustomerName(String invoiceCustomerName) {
		this.invoiceCustomerName = invoiceCustomerName;
	}
	public double getCashPaid() {
		return cashPaid;
	}
	public void setCashPaid(double cashPaid) {
		this.cashPaid = cashPaid;
	}

}
