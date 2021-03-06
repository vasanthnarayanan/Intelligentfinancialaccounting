package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.TaxInvoice;

public interface TaxInvoiceDAO {
	public Long getLastTaxInvoiceByDate(Invoice invoice);
	public Long getFirstTaxInvoiceByDate(Invoice invoice);
	public Long countInvoiceByDate(Invoice invoice);
	public Long getLastTaxInvoice();
	public Long getCurrentTaxInvoiceSeq();
	public boolean instantCreate(Invoice invoice);
	public TaxInvoice getTaxInvoiceById(long taxInvoiceId);
	public boolean edit(Invoice invoice);
	public boolean softDelete(long invoiceId);
	public List<TaxInvoice> listInvoiceByMonth(String monthOfYear);
	public List<TaxInvoice> listInvoiceInclPrivelegedByMonth(String monthOfYear);
}
