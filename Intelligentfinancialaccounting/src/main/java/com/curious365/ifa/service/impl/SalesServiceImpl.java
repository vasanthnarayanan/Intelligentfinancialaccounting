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
import com.curious365.ifa.dao.SalesDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Item;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.dto.Transaction;
import com.curious365.ifa.exceptions.NoStockInHand;
import com.curious365.ifa.service.AccountingService;
import com.curious365.ifa.service.SalesService;

@Service
public class SalesServiceImpl implements SalesService {
	
	@Autowired
	private SalesDAO salesDAO;
	@Autowired
	private ItemDAO itemDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private InvoiceDAO invoiceDAO;
	@Autowired
	private AccountingService accountingService;

	public SalesDAO getSalesDAO() {
		return salesDAO;
	}

	public void setSalesDAO(SalesDAO salesDAO) {
		this.salesDAO = salesDAO;
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void create(Sales record) throws NoStockInHand,Exception {
		Item item = new Item();
		item.setItemId(""+record.getSalesItemId());
		item.setStock(record.getSalesPieces());
		boolean stockAvailable = itemDAO.verifyStock(item);
		if(stockAvailable){
			salesDAO.create(record);
			itemDAO.decreaseStock(item);
			customerDAO.increaseCurrentBalance(""+record.getSalesCustomerId(), record.getSalesTotal());
		}else{
			throw new NoStockInHand("Requirement exceeds stock in hand.Please check!");
		}
			
	}
	
	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void create(List<Sales> records,Invoice invoice) throws NoStockInHand,Exception {
		invoiceDAO.create(invoice);
		long invoiceId = invoiceDAO.getCurrentInvoiceId();
		for (Sales record : records) {
			Item item = new Item();
			item.setItemId(""+record.getSalesItemId());
			item.setStock(record.getSalesPieces());
			Sales salesCopy = new Sales();
			BeanUtils.copyProperties(record, salesCopy);
			salesCopy.setInvoiceId(invoiceId);
			boolean stockAvailable = itemDAO.verifyStock(item);
			if(stockAvailable){
				salesDAO.create(salesCopy);
				itemDAO.decreaseStock(item);
				customerDAO.increaseCurrentBalance(""+salesCopy.getSalesCustomerId(), salesCopy.getSalesTotal());
			}else{
				throw new NoStockInHand("Requirement exceeds stock in hand.Please check!");
			}
		}
		
		if(invoice.getCashPaid()>0){
			Transaction transaction = new Transaction();
			transaction.setTransactionDate(invoice.getInvoiceDate());
			transaction.setTransactionCustomerId(invoice.getInvoiceCustomerId());
			transaction.setTransactionAmount(invoice.getCashPaid());
			transaction.setIsIncome("Y");
			transaction.setModeOfPayment(ModeOfPayment.CASH.getValue());
			transaction.setDueDate(invoice.getInvoiceDate());
			transaction.setTransactionStatus(TransactionStatus.COMPLETE.getValue());
			transaction.setTransactionRemarks("Immediate Payment for invoice #"+invoiceId);
			
			accountingService.createTransaction(transaction);
		}
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void edit(Sales record) throws NoStockInHand,Exception {
		Sales oldRecord = salesDAO.getRecordById(record.getSalesRecordId());
		double oldtotal = oldRecord.getSalesTotal(); 
		double newtotal = record.getSalesTotal();
		double totaldifference = newtotal-oldtotal;
		if(record.getSalesItemId()==oldRecord.getSalesItemId()){
			long change = record.getSalesPieces() - oldRecord.getSalesPieces();	
			if(change>0){
				Item item = new Item();
				item.setItemId(""+record.getSalesItemId());
				item.setStock(change);
				boolean stockAvailable = itemDAO.verifyStock(item);
				if(stockAvailable){
					salesDAO.edit(record);
					itemDAO.decreaseStock(item);
				}else{
					throw new NoStockInHand("Requirement exceeds stock in hand.Please check!");
				}
			}else if(change<0){
				Item item = new Item();
				item.setItemId(""+record.getSalesItemId());
				item.setStock(-change);
				salesDAO.edit(record);
				itemDAO.increaseStock(item);
			}else{
				// equals zero no need to change stock
				salesDAO.edit(record);
			}
			
		}else{
			// replenish old item stock
			Item oldItem = new Item();
			oldItem.setItemId(""+oldRecord.getSalesItemId());
			oldItem.setStock(oldRecord.getSalesPieces());
			itemDAO.increaseStock(oldItem);
			
			Item newItem = new Item();
			newItem.setItemId(""+record.getSalesItemId());
			newItem.setStock(record.getSalesPieces());
			// verify and decrease new item stock
			boolean stockAvailable = itemDAO.verifyStock(newItem);
			if(stockAvailable){
				salesDAO.edit(record);
				itemDAO.decreaseStock(newItem);
			}else{
				throw new NoStockInHand("Requirement exceeds stock in hand.Please check!");
			}
		}
		
		if(record.getSalesCustomerId()==oldRecord.getSalesCustomerId()){
			// same customer
			if(totaldifference>0){
				customerDAO.increaseCurrentBalance(""+record.getSalesCustomerId(), totaldifference);
			}else if(totaldifference<0){
				customerDAO.decreaseCurrentBalance(""+record.getSalesCustomerId(), -totaldifference);
			}
		}else{
			// different customer
			// decrease current balance with old sum
			customerDAO.decreaseCurrentBalance(""+oldRecord.getSalesCustomerId(), oldtotal);
			// increase current balance for new customer with new sum
			customerDAO.increaseCurrentBalance(""+record.getSalesCustomerId(), newtotal);
		}
		
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void remove(long salesId) {
			Sales record = salesDAO.getRecordById(salesId);
			Item item = new Item();
			item.setItemId(""+record.getSalesItemId());
			item.setStock(record.getSalesPieces());
			salesDAO.softDelete(salesId);
			itemDAO.increaseStock(item);
			customerDAO.decreaseCurrentBalance(""+record.getSalesCustomerId(), record.getSalesTotal());
	}

	@Override
	public Sales getRecordById(long salesId) {
		return salesDAO.getRecordById(salesId);
	}
	
	@Override
	public List<Content> listAllActiveSales(int strtRow,int endRow) {
		return salesDAO.listAllSales(strtRow,endRow,Constants.ACTIVE);
	}

	@Override
	public List<Content> listAllActiveSalesInclPriveleged(int strtRow,
			int endRow) {
		return salesDAO.listAllSalesInclPriveleged(strtRow,endRow,Constants.ACTIVE);
		}
	
	@Override
	public long getActiveSalesRowCount() {
		long rowCount = 0;
		try {
			rowCount = salesDAO.getActiveSalesRowCount();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return rowCount;
		}
	}

	@Override
	public long getActiveSalesRowCountInclPriveleged() {
		long rowCount = 0;
		try {
			rowCount = salesDAO.getActiveSalesRowCountInclPriveleged();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return rowCount;
		}
	}
	
	

}
