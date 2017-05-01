package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Purchase;

public interface PurchaseDAO {
	public boolean create(Purchase record);
	public boolean edit(Purchase record);
	public boolean softDelete(long purchaseId);
	public Purchase getRecordById(long purchaseId);
	public List<Content> listAllPurchase(int strtRow,int endRow,int isActive);
	public long getCurrentPurchaseId();
}
