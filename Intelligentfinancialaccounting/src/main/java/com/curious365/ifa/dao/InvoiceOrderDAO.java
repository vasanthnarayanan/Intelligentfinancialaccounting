package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.InvoiceOrder;

public interface InvoiceOrderDAO {
	public boolean create(InvoiceOrder invoiceorder);
	public boolean edit(InvoiceOrder invoiceorder);
	public boolean softDelete(long invoiceorderId);
	public InvoiceOrder getRecordById(long invoiceorderId);
	public List<Content> listAllInvoiceOrder();
	public long getCurrentInvoiceOrderId();
}
