package com.curious365.ifa.dao;

import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.TaxInvoice;

public interface TaxInvoiceDAO {
	public Long getLastTaxInvoiceByMonth(Invoice invoice);
	public Long getLastTaxInvoice();
	public Long getCurrentTaxInvoiceSeq();
	public boolean instantCreate(Invoice invoice);
	public TaxInvoice getTaxInvoiceById(long taxInvoiceId);
}
