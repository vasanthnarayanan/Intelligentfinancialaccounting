package com.curious365.ifa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.curious365.ifa.dto.BankAccount;
import com.curious365.ifa.dto.Transaction;

public interface AccountingService {
	public long getAccountDetailsCount(long customerId);
	public Map<String,Object> getCustomerAccountDetail(long customerId,double initialBalance,int strtRow,int endRow,int length) throws ParseException, Exception;
	public List<BankAccount> listBankAccounts();
	public boolean createBankAccount(BankAccount bankAccount) throws Exception;
	public boolean createTransaction(Transaction record) throws Exception;
	public boolean editTransaction(Transaction record) throws Exception;
	public boolean removeTransaction(long transactionId) throws Exception;
	public Transaction getRecordById(long transactionId);
	public long countPendingTransactions();
	public List<Transaction> listPendingTransactions(int strtRow,int endRow,int length);
}
