package com.curious365.ifa.service;

import java.text.ParseException;
import java.util.Map;

import com.curious365.ifa.dto.Transaction;

public interface AccountingService {
	public long getAccountDetailsCount(long customerId);
	public Map<String,Object> getCustomerAccountDetail(long customerId,double initialBalance,int strtRow,int endRow,int length) throws ParseException, Exception;
	public boolean createTransaction(Transaction record);
	public boolean editTransaction(Transaction record);
	public boolean removeTransaction(long transactionId);
	public Transaction getRecordById(long transactionId);
	public long getActiveSalesRowCount();
	public long getActivePurchaseRowCount();
	public long getActiveFaultRowCount();
}
