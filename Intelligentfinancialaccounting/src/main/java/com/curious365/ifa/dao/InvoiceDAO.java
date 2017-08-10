package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Invoice;

public interface InvoiceDAO {
	public boolean create(Invoice invoice);
	public boolean edit(Invoice invoice);
	public boolean softDelete(long invoiceId);
	public Invoice getRecordById(long invoiceId);
	public boolean updateInvoiceWtTaxInvoice(Invoice invoice);
	public List<Invoice> listInvoiceByMonth(String monthOfYear);
	public List<Invoice> listInvoiceInclPrivelegedByMonth(String monthOfYear);
	public List<Invoice> listInvoiceLike(String query,String invoiceType);
	public List<Invoice> listInvoiceInclPrivelegedLike(String query,String invoiceType);
	public long getCurrentInvoiceId();
}
