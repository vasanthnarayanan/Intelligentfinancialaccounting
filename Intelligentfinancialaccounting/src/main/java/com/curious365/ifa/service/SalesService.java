package com.curious365.ifa.service;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.exceptions.NoStockInHand;

public interface SalesService {
	public void create(Sales record) throws NoStockInHand,Exception;
	public void create(List<Sales> records,Invoice invoice) throws NoStockInHand,Exception;
	public void edit(Sales record) throws NoStockInHand,Exception;
	public void remove(long salesId) throws Exception;
	public Sales getRecordById(long salesId);
	public long getActiveSalesRowCount();
	public long getActiveSalesRowCountInclPriveleged();
	public List<Content> listAllActiveSales(int strtRow,int endRow);
	public List<Content> listAllActiveSalesInclPriveleged(int strtRow,int endRow);
	
}
