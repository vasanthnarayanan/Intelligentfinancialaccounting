package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.AccountingDAO;
import com.curious365.ifa.dto.BankAccount;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Transaction;

@Repository
public class AccountingDAOImpl implements AccountingDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public List<Double> listGrossCalculations(long customerId) {
		List<Double> grossCalculations = null;
		try{	
			grossCalculations = jdbcTemplate.queryForList(QueryConstants.LIST_TOTAL_BALANCE,Double.class,new Object[]{customerId,Constants.ACTIVE,
					customerId,Constants.ACTIVE,
					customerId,Constants.ACTIVE,
					customerId,Constants.CHAR_Y,Constants.ACTIVE,
					customerId,Constants.CHAR_N,Constants.ACTIVE});
		}catch(Exception e){
			e.printStackTrace();
		}

		return grossCalculations;
	}


	@Override
	public Long getActiveAccountsRowCount(long customerId) throws Exception {
		return jdbcTemplate.queryForObject(QueryConstants.COUNT_ACTIVE_ACCOUNT_DETAILS,Long.class,new Object[]{customerId,Constants.ACTIVE,
																												customerId,Constants.ACTIVE,
																												customerId,Constants.ACTIVE,
																												customerId,Constants.ACTIVE});
	}


	@Override
	public Transaction getTransactionById(long transactionId) {
		Transaction record = null;
		try{
			record = jdbcTemplate.queryForObject(QueryConstants.GET_TRANSACTION,  new BeanPropertyRowMapper<Transaction>(Transaction.class),new Object[]{transactionId,Constants.ACTIVE});
		}catch(Exception e){
			
		}
		return record;
	}


	@Override
	public boolean createTransaction(Transaction record) {
		int flag = jdbcTemplate.update(
				QueryConstants.CREATE_TRANSACTION,
				new Object[] { record.getTransactionDate(),
						record.getTransactionCustomerId(),
						record.getTransactionAmount(), record.getIsIncome(),
						record.getTransactionRemarks(),
						record.getModeOfPayment(),
						record.getDueDate(),
						record.getTransactionStatus(),
						record.getChequeNumber(),
						record.getChequeBankId(),
						record.getRefCustomerId(),
						record.getBankId(),
						Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean editTransaction(Transaction record) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_TRANSACTION,
				new Object[] { record.getTransactionDate(),
						record.getTransactionCustomerId(),
						record.getTransactionAmount(), record.getIsIncome(),
						record.getTransactionRemarks(),
						record.getModeOfPayment(),
						record.getDueDate(),
						record.getTransactionStatus(),
						record.getChequeNumber(),
						record.getChequeBankId(),
						record.getRefCustomerId(),
						record.getBankId(),
						record.getTransactionRecordId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean softDeleteTransaction(long transactionId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_TRANSACTION,
				new Object[] { Constants.INACTIVE, transactionId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Long getActiveSalesCount() throws Exception {
		return jdbcTemplate.queryForObject("Select count(*) from sales where activeflag=?",Long.class,new Object[]{Constants.ACTIVE});
	}

	@Override
	public List<Content> listActiveAccountDetails(long customerId, int strtRow,
			int endRow) {
		List<Content> accountList = null;
		try{
			accountList = jdbcTemplate.query(QueryConstants.LIST_ACTIVE_ACCOUNT_DETAILS,new BeanPropertyRowMapper<Content>(Content.class),new Object[]{customerId,Constants.ACTIVE,
				customerId,Constants.ACTIVE,
				customerId,Constants.ACTIVE,
				customerId,Constants.CHAR_Y,Constants.ACTIVE,
				customerId,Constants.CHAR_N,Constants.ACTIVE
				,strtRow,endRow});
		}catch(Exception e){
			e.printStackTrace();
		}
		return accountList;
	}
	
	@Override
	public List<Double> getPrevPageBalance(long customerId, int strtRow,
			int endRow) {
		List<Double> balanceList = null;
		try{
			balanceList = jdbcTemplate.queryForList(QueryConstants.GET_PREVIOUS_PAGE_BALANCE,Double.class,new Object[]{customerId,Constants.ACTIVE,
				customerId,Constants.ACTIVE,
				customerId,Constants.ACTIVE,
				customerId,Constants.CHAR_Y,Constants.ACTIVE,
				customerId,Constants.CHAR_N,Constants.ACTIVE
				,strtRow,endRow});
		}catch(Exception e){
			e.printStackTrace();
		}
		return balanceList;
	}


	@Override
	public long getCurrentTransactionId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_TRANSACTION_CURR_SEQ, Long.class);
	}


	@Override
	public List<BankAccount> listBankAccounts() {
		return jdbcTemplate.query(QueryConstants.LIST_BANK_ACCOUNTS, new BeanPropertyRowMapper<BankAccount>(BankAccount.class), new Object[]{Constants.ACTIVE});
	}


	@Override
	public BankAccount getBankAccountById(long bankId) {
		return jdbcTemplate.queryForObject(QueryConstants.GET_BANK_ACCOUNT,BankAccount.class,new Object[]{bankId,Constants.ACTIVE});
	}


	@Override
	public boolean createBankAccount(BankAccount bankAccount) {
		int flag = jdbcTemplate.update(
				QueryConstants.CREATE_BANK_ACCONT,
				new Object[] { bankAccount.getBankName(),
						bankAccount.getBankAddress(),
						bankAccount.getBankAccountNumber(),
						bankAccount.getBankIfscCode(),
						bankAccount.getBankAccountType(),
						Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean editBankAccount(BankAccount bankAccount) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_BANK_ACCOUNT,
				new Object[] {  bankAccount.getBankName(),
						bankAccount.getBankAddress(),
						bankAccount.getBankAccountNumber(),
						bankAccount.getBankIfscCode(),
						bankAccount.getBankAccountType(),
						bankAccount.getBankId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean removeBankAccount(long bankId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_BANK_ACCOUNT,
				new Object[] { Constants.INACTIVE, bankId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public List<Transaction> listTransactionByStatus(String status,int strtRow,int endRow,int length) {
		return jdbcTemplate.query(QueryConstants.LIST_TRANSACTION_BY_STATUS, new BeanPropertyRowMapper<Transaction>(Transaction.class), new Object[]{status,Constants.ACTIVE,endRow,strtRow});
	}


	@Override
	public long countTransactionByStatus(String status) {
		return jdbcTemplate.queryForObject(QueryConstants.COUNT_TRANSACTION_BY_STATUS,Long.class,new Object[]{status,Constants.ACTIVE});
	}
	
	
}
