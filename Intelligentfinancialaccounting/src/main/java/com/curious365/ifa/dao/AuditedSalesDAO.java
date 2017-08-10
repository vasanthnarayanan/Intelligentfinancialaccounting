package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.AuditedSales;


public interface AuditedSalesDAO {
	public boolean create(AuditedSales record);
	public boolean edit(AuditedSales record);
	public AuditedSales getRecordById(long recordid);
	public List<AuditedSales> listAuditedSalesByTaxInvoiceId(long taxInvoiceId);
	public List<AuditedSales> listAuditedSalesByMonthOfYear(String monthOfYear);
	public List<AuditedSales> listAuditedSalesByMonth(String monthOfYear);
	public List<AuditedSales> listAuditedSalesInclPrivelegedByMonth(String monthOfYear);
}
