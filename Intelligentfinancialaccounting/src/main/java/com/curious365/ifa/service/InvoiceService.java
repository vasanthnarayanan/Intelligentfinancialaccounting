package com.curious365.ifa.service;

import java.util.List;
import java.util.Map;

import com.curious365.ifa.dto.Invoice;

public interface InvoiceService {
	public Map<String,Object> populateAutocomplete(String query,String invoiceType);
	public Map<String,Object> populateAutocompleteInclPrivileged(String query,String invoiceType);
	public void edit(Invoice invoice)throws Exception;
	public Invoice getInvoiceWtDetails(long invoiceId)throws Exception;
	public List<Invoice> listEstimatesInclPrivilegedForMonth(String monthOfYear);
	public List<Invoice> listEstimatesForMonth(String monthOfYear);
}
