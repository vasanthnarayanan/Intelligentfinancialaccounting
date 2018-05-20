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
import com.curious365.ifa.dao.FaultDAO;
import com.curious365.ifa.dao.InvoiceDAO;
import com.curious365.ifa.dao.ItemDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Fault;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Item;
import com.curious365.ifa.dto.Transaction;
import com.curious365.ifa.service.AccountingService;
import com.curious365.ifa.service.FaultService;

@Service
public class FaultServiceImpl implements FaultService {
	
	@Autowired
	private FaultDAO faultDAO;
	@Autowired
	private ItemDAO itemDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private InvoiceDAO invoiceDAO;
	@Autowired
	private AccountingService accountingService;

	public FaultDAO getFaultDAO() {
		return faultDAO;
	}

	public void setFaultDAO(FaultDAO faultDAO) {
		this.faultDAO = faultDAO;
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void create(Fault record) throws Exception {
		Item item = new Item();
		item.setItemId(""+record.getFaultItemId());
		item.setStock(record.getFaultPieces());
		 faultDAO.create(record); 
		 itemDAO.increaseStock(item);
		 customerDAO.decreaseCurrentBalance(""+record.getFaultCustomerId(), record.getFaultTotal());
	}
	
	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void create(List<Fault> records,Invoice invoice) throws Exception {
		invoiceDAO.create(invoice);
		long invoiceId = invoiceDAO.getCurrentInvoiceId();
		for (Fault record : records) {
			Item item = new Item();
			item.setItemId(""+record.getFaultItemId());
			item.setStock(record.getFaultPieces());
			Fault faultCopy = new Fault();
			BeanUtils.copyProperties(record, faultCopy);
			faultCopy.setInvoiceId(invoiceId);
			faultDAO.create(faultCopy); 
			itemDAO.increaseStock(item);
			customerDAO.decreaseCurrentBalance(""+faultCopy.getFaultCustomerId(), faultCopy.getFaultTotal());
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
	public void edit(Fault record) throws Exception {
		Fault oldRecord = faultDAO.getRecordById(record.getFaultRecordId());
		double oldtotal = oldRecord.getFaultTotal(); 
		double newtotal = record.getFaultTotal();
		double totaldifference = newtotal-oldtotal;
		
		if(record.getFaultItemId()==oldRecord.getFaultItemId()){
			long change = record.getFaultPieces() - oldRecord.getFaultPieces();	
			if(change>0){
				Item item = new Item();
				item.setItemId(""+record.getFaultItemId());
				item.setStock(change);
				faultDAO.edit(record);
				itemDAO.increaseStock(item);
			}else if(change<0){
				Item item = new Item();
				item.setItemId(""+record.getFaultItemId());
				item.setStock(-change);
				faultDAO.edit(record);
				itemDAO.decreaseStock(item);
			}else{
				// equals zero no need to change stock
				faultDAO.edit(record);
			}
			
		}else{
			// decrease old item stock
			Item oldItem = new Item();
			oldItem.setItemId(""+oldRecord.getFaultItemId());
			oldItem.setStock(oldRecord.getFaultPieces());
			itemDAO.decreaseStock(oldItem);
			
			Item newItem = new Item();
			newItem.setItemId(""+record.getFaultItemId());
			newItem.setStock(record.getFaultPieces());
			faultDAO.edit(record);
			itemDAO.increaseStock(newItem);
		}
		
		if(record.getFaultCustomerId()==oldRecord.getFaultCustomerId()){
			// same customer
			if(totaldifference>0){
				customerDAO.decreaseCurrentBalance(""+record.getFaultCustomerId(), totaldifference);
			}else if(totaldifference<0){
				customerDAO.increaseCurrentBalance(""+record.getFaultCustomerId(), -totaldifference);
			}
		}else{
			// different customer
			// increase current balance with old sum
			customerDAO.increaseCurrentBalance(""+oldRecord.getFaultCustomerId(), oldtotal);
			// decrease current balance for new customer with new sum
			customerDAO.decreaseCurrentBalance(""+record.getFaultCustomerId(), newtotal);
		}
		
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void remove(long faultId) throws Exception {
		Fault record = faultDAO.getRecordById(faultId);
		Item item = new Item();
		item.setItemId(""+record.getFaultItemId());
		item.setStock(record.getFaultPieces());
		faultDAO.softDelete(faultId);
		itemDAO.decreaseStock(item);
		customerDAO.increaseCurrentBalance(""+record.getFaultCustomerId(), record.getFaultTotal());
	}

	@Override
	public Fault getRecordById(long faultId) {
		return faultDAO.getRecordById(faultId);
	}

	@Override
	public List<Content> listAllActiveFault(int strtRow, int endRow) {
		return faultDAO.listAllFault(strtRow, endRow, Constants.ACTIVE);
	}

	@Override
	public List<Content> listAllActiveFaultInclPriveleged(int strtRow,
			int endRow) {
		return faultDAO.listAllFaultInclPriveleged(strtRow, endRow, Constants.ACTIVE);
		}
	
	@Override
	public long getActiveFaultRowCount() {
		long rowCount = 0;
		try {
			rowCount = faultDAO.getActiveFaultRowCount();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return rowCount;
		}
	}

	@Override
	public long getActiveFaultRowCountInclPriveleged() {
		long rowCount = 0;
		try {
			rowCount = faultDAO.getActiveFaultRowCountInclPriveleged();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return rowCount;
		}
	}

}
