package com.curious365.ifa.service;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Purchase;

public interface PurchaseService {
	public void create(Purchase record) throws Exception;
	public void create(List<Purchase> record,Invoice invoice) throws Exception;
	public void edit(Purchase record) throws Exception;
	public void remove(long purchaseId) throws Exception;
	public Purchase getRecordById(long purchaseId);
	public long getActivePurchaseRowCount();
	public long getActivePurchaseRowCountInclPriveleged();
	public List<Content> listAllActivePurchase(int strtRow,int endRow);
	public List<Content> listAllActivePurchaseInclPriveleged(int strtRow,int endRow);
	
}
