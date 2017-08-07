package com.curious365.ifa.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Purchase;

public interface TaxInvoiceService {
	public void createInstantTaxInvoice(Invoice invoice) throws ParseException;
	public void generateTaxInvoiceForMonth(Calendar cal,List<Purchase> purchases);

}
