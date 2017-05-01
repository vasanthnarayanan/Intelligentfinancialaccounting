package com.curious365.ifa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dao.PurchaseDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.service.PurchaseService;

@Service
public class PurchaseServiceImpl implements PurchaseService {
	
	@Autowired
	private PurchaseDAO purchaseDAO;

	@Override
	public boolean create(Purchase record) {
		return purchaseDAO.create(record); 
	}

	@Override
	public boolean edit(Purchase record) {
		return purchaseDAO.edit(record);
	}

	@Override
	public boolean remove(long purchaseId) {
		return purchaseDAO.softDelete(purchaseId);
	}

	@Override
	public Purchase getRecordById(long purchaseId) {
		return purchaseDAO.getRecordById(purchaseId);
	}

	public PurchaseDAO getPurchaseDAO() {
		return purchaseDAO;
	}

	public void setPurchaseDAO(PurchaseDAO purchaseDAO) {
		this.purchaseDAO = purchaseDAO;
	}

	@Override
	public List<Content> listAllActivePurchase(int strtRow, int endRow) {
		return purchaseDAO.listAllPurchase(strtRow, endRow, Constants.ACTIVE);
	}

}
