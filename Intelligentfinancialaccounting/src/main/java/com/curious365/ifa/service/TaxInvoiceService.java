package com.curious365.ifa.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.dto.TaxInvoice;
import com.curious365.ifa.exceptions.InvoiceLimitExceeded;

public interface TaxInvoiceService {
	public long createInstantTaxInvoice(Invoice invoice) throws ParseException, InvoiceLimitExceeded;
	public void edit(Invoice invoice)throws Exception;
	public void generateTaxInvoiceForMonth(Calendar cal,List<Purchase> purchases);
	public TaxInvoice getTaxInvoiceWtDetails(long taxInvoiceId)throws Exception;
	public List<TaxInvoice> listTaxInvoicesInclPrivilegedForMonth(String monthOfYear);
	public List<TaxInvoice> listTaxInvoicesForMonth(String monthOfYear);

}
