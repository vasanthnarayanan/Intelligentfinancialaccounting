package com.curious365.ifa.service;

import java.util.List;

import com.curious365.ifa.dto.AuditedSales;

public interface AuditedSalesService {
	public List<AuditedSales> listAuditedSalesInclPrivilegedForMonth(String monthOfYear);
	public List<AuditedSales> listAuditedSalesForMonth(String monthOfYear);
	public void editAuditedSales(AuditedSales record) throws Exception;
	public AuditedSales getRecordById(long recordid);
}
