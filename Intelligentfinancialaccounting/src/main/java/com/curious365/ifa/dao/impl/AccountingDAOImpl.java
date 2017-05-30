package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.AccountingDAO;
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
	public Long getActiveSalesRowCount() throws Exception {
		return jdbcTemplate.queryForObject(QueryConstants.COUNT_ACTIVE_SALES,Long.class,new Object[]{Constants.ACTIVE});
	}

	@Override
	public Long getActiveSalesCount() throws Exception {
		return jdbcTemplate.queryForObject("Select count(*) from sales where activeflag=?",Long.class,new Object[]{Constants.ACTIVE});
	}


	@Override
	public Long getActivePurchaseRowCount() throws Exception {
		return jdbcTemplate.queryForObject(QueryConstants.COUNT_ACTIVE_PURCHASE,Long.class,new Object[]{Constants.ACTIVE});
	}


	@Override
	public Long getActiveFaultRowCount() throws Exception {
		return jdbcTemplate.queryForObject(QueryConstants.COUNT_ACTIVE_FAULT,Long.class,new Object[]{Constants.ACTIVE});
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
	
	
}
