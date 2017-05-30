package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Transaction;

public interface AccountingDAO {
	public Long getActiveAccountsRowCount(long customerId) throws Exception;
	public List<Content> listActiveAccountDetails(long customerId, int strtRow, int endRow);
	public List<Double> getPrevPageBalance(long customerId, int strtRow, int endRow);
	public List<Double> listGrossCalculations(long customerId);
	public Transaction getTransactionById(long transactionId);
	public boolean createTransaction(Transaction record);
	public boolean editTransaction(Transaction record);
	public boolean softDeleteTransaction(long transactionId);
	public Long getActiveSalesRowCount() throws Exception;
	public Long getActiveSalesCount() throws Exception;
	public Long getActivePurchaseRowCount() throws Exception;
	public Long getActiveFaultRowCount() throws Exception;
	public long getCurrentTransactionId();
}
