package com.curious365.ifa.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.ModeOfPayment;
import com.curious365.ifa.common.TransactionStatus;
import com.curious365.ifa.dao.CustomerDAO;
import com.curious365.ifa.dao.InvoiceDAO;
import com.curious365.ifa.dao.ItemDAO;
import com.curious365.ifa.dao.PurchaseDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Item;
import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.dto.Transaction;
import com.curious365.ifa.service.AccountingService;
import com.curious365.ifa.service.PurchaseService;

@Service
public class PurchaseServiceImpl implements PurchaseService {
	
	@Autowired
	private PurchaseDAO purchaseDAO;
	@Autowired
	private ItemDAO itemDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private InvoiceDAO invoiceDAO;
	@Autowired
	private AccountingService accountingService;

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void create(Purchase record) throws Exception{
		Item item = new Item();
		item.setItemId(""+record.getPurchaseItemId());
		item.setStock(record.getPurchasePieces());
		 purchaseDAO.create(record); 
		 itemDAO.increaseStock(item);
		 customerDAO.decreaseCurrentBalance(""+record.getPurchaseCustomerId(), record.getPurchaseTotal());
	}
	
	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void create(List<Purchase> records,Invoice invoice) throws Exception {
		invoiceDAO.create(invoice);
		long invoiceId = invoiceDAO.getCurrentInvoiceId();
		for (Purchase record : records) {
			Item item = new Item();
			item.setItemId(""+record.getPurchaseItemId());
			item.setStock(record.getPurchasePieces());
			Purchase purchaseCopy = new Purchase();
			BeanUtils.copyProperties(record, purchaseCopy);
			purchaseCopy.setInvoiceId(invoiceId);
			purchaseDAO.create(purchaseCopy); 
			itemDAO.increaseStock(item);
			customerDAO.decreaseCurrentBalance(""+purchaseCopy.getPurchaseCustomerId(), purchaseCopy.getPurchaseTotal());
		}
		
		if(invoice.getCashPaid()>0){
			Transaction transaction = new Transaction();
			transaction.setTransactionDate(invoice.getInvoiceDate());
			transaction.setTransactionCustomerId(invoice.getInvoiceCustomerId());
			transaction.setTransactionAmount(invoice.getCashPaid());
			transaction.setIsIncome("N");
			transaction.setModeOfPayment(ModeOfPayment.CASH.getValue());
			transaction.setDueDate(invoice.getInvoiceDate());
			transaction.setTransactionStatus(TransactionStatus.COMPLETE.getValue());
			transaction.setTransactionRemarks("Immediate Payment for invoice #"+invoiceId);
			
			accountingService.createTransaction(transaction);
		}
		 
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void edit(Purchase record) throws Exception {
		Purchase oldRecord = purchaseDAO.getRecordById(record.getPurchaseRecordId());
		double oldtotal = oldRecord.getPurchaseTotal(); 
		double newtotal = record.getPurchaseTotal();
		double totaldifference = newtotal-oldtotal;
		
		if(record.getPurchaseItemId()==oldRecord.getPurchaseItemId()){
			long change = record.getPurchasePieces() - oldRecord.getPurchasePieces();	
			if(change>0){
				Item item = new Item();
				item.setItemId(""+record.getPurchaseItemId());
				item.setStock(change);
				purchaseDAO.edit(record);
				itemDAO.increaseStock(item);
			}else if(change<0){
				Item item = new Item();
				item.setItemId(""+record.getPurchaseItemId());
				item.setStock(-change);
				purchaseDAO.edit(record);
				itemDAO.decreaseStock(item);
			}else{
				// equals zero no need to change stock
				purchaseDAO.edit(record);
			}
			
		}else{
			// decrease old item stock
			Item oldItem = new Item();
			oldItem.setItemId(""+oldRecord.getPurchaseItemId());
			oldItem.setStock(oldRecord.getPurchasePieces());
			itemDAO.decreaseStock(oldItem);
			
			Item newItem = new Item();
			newItem.setItemId(""+record.getPurchaseItemId());
			newItem.setStock(record.getPurchasePieces());
			purchaseDAO.edit(record);
			itemDAO.increaseStock(newItem);
		}
		
		if(record.getPurchaseCustomerId()==oldRecord.getPurchaseCustomerId()){
			// same customer
			if(totaldifference>0){
				customerDAO.decreaseCurrentBalance(""+record.getPurchaseCustomerId(), totaldifference);
			}else if(totaldifference<0){
				customerDAO.increaseCurrentBalance(""+record.getPurchaseCustomerId(), -totaldifference);
			}
		}else{
			// different customer
			// increase current balance with old sum
			customerDAO.increaseCurrentBalance(""+oldRecord.getPurchaseCustomerId(), oldtotal);
			// decrease current balance for new customer with new sum
			customerDAO.decreaseCurrentBalance(""+record.getPurchaseCustomerId(), newtotal);
		}
		
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void remove(long purchaseId) throws Exception {
		Purchase record = purchaseDAO.getRecordById(purchaseId);
		Item item = new Item();
		item.setItemId(""+record.getPurchaseItemId());
		item.setStock(record.getPurchasePieces());
		purchaseDAO.softDelete(purchaseId);
		itemDAO.decreaseStock(item);
		customerDAO.increaseCurrentBalance(""+record.getPurchaseCustomerId(), record.getPurchaseTotal());
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

	@Override
	public List<Content> listAllActivePurchaseInclPriveleged(int strtRow,
			int endRow) {
		return purchaseDAO.listAllPurchaseInclPriveleged(strtRow, endRow, Constants.ACTIVE);
		}
	
	@Override
	public long getActivePurchaseRowCount() {
		long rowCount = 0;
		try {
			rowCount = purchaseDAO.getActivePurchaseRowCount();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return rowCount;
		}
	}

	@Override
	public long getActivePurchaseRowCountInclPriveleged() {
		long rowCount = 0;
		try {
			rowCount = purchaseDAO.getActivePurchaseRowCountInclPriveleged();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return rowCount;
		}
	}

}
