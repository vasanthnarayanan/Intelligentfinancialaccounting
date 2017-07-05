package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.BankAccount;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Transaction;

public interface AccountingDAO {
	public Long getActiveAccountsRowCount(long customerId) throws Exception;
	public List<Content> listActiveAccountDetails(long customerId, int strtRow, int endRow);
	public List<Double> getPrevPageBalance(long customerId, int strtRow, int endRow);
	public List<Double> listGrossCalculations(long customerId);
	public List<BankAccount> listBankAccounts();
	public BankAccount getBankAccountById(long bankId);
	public boolean createBankAccount(BankAccount bankAccount);
	public boolean editBankAccount(BankAccount bankAccount);
	public boolean removeBankAccount(long bankId);
	public Transaction getTransactionById(long transactionId);
	public boolean createTransaction(Transaction record);
	public boolean editTransaction(Transaction record);
	public boolean softDeleteTransaction(long transactionId);
	public Long getActiveSalesCount() throws Exception;
	public long getCurrentTransactionId();
	public long countTransactionByStatus(String status);
	public List<Transaction> listTransactionByStatus(String status,int strtRow,int endRow,int length);
}
