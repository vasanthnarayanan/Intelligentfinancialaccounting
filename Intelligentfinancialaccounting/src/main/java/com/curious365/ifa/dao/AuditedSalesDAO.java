package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.AuditedSales;


public interface AuditedSalesDAO {
	public boolean create(AuditedSales record);
	public List<AuditedSales> listAuditedSalesByMonthOfYear(String monthOfYear);
}
