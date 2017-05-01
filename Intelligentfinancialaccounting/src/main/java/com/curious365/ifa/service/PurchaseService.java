package com.curious365.ifa.service;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Purchase;

public interface PurchaseService {
	public boolean create(Purchase record);
	public boolean edit(Purchase record);
	public boolean remove(long purchaseId);
	public Purchase getRecordById(long purchaseId);
	public List<Content> listAllActivePurchase(int strtRow,int endRow);
	
}
