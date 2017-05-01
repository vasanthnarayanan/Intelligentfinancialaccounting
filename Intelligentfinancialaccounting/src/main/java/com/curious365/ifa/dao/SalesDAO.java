package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Sales;

public interface SalesDAO {
	public boolean create(Sales record);
	public boolean edit(Sales record);
	public boolean softDelete(long salesId);
	public Sales getRecordById(long salesId);
	public List<Content> listAllSales(int strtRow,int endRow,int isActive);
	public long getCurrentSalesId();
}
