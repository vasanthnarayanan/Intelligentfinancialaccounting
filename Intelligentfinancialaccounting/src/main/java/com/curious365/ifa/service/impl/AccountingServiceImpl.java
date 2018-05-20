package com.curious365.ifa.service.impl;

import static com.curious365.ifa.common.Constants.EXPENDITURE;
import static com.curious365.ifa.common.Constants.FAULT;
import static com.curious365.ifa.common.Constants.INCOME;
import static com.curious365.ifa.common.Constants.PURCHASE;
import static com.curious365.ifa.common.Constants.SALES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curious365.ifa.common.TransactionStatus;
import com.curious365.ifa.dao.AccountingDAO;
import com.curious365.ifa.dao.CustomerDAO;
import com.curious365.ifa.dto.BankAccount;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Transaction;
import com.curious365.ifa.exceptions.NoRecordFound;
import com.curious365.ifa.service.AccountingService;

@Service
public class AccountingServiceImpl implements AccountingService {

	@Autowired
	private AccountingDAO accountingDAO;
	@Autowired
	private CustomerDAO customerDAO;

	public AccountingDAO getAccountingDAO() {
		return accountingDAO;
	}

	public void setAccountingDAO(AccountingDAO accountingDAO) {
		this.accountingDAO = accountingDAO;
	}

	@Override
	public Map<String,Object> getCustomerAccountDetail(long customerId, double initialBalance, int strtRow,
			int endRow,int length) throws Exception {
			Map<String,Object> accountsMap = new HashMap<String,Object>();
		    List<Double> prevBalance=new ArrayList<Double>();
		    /* --- Previous page balance --- */
		    double prevPageBalance=0;
		    if(strtRow>=length)
		    {
		    	int prevStrtRow=0;
		    	int prevEndRow=endRow-length;
		    	
		    	prevBalance = accountingDAO.getPrevPageBalance(customerId, prevStrtRow, prevEndRow);
			    
			    if(prevBalance.isEmpty())
			    {
			    	throw new NoRecordFound("No records found");
			    }
			    
					for (Double dateWiseBalance : prevBalance) {
						prevPageBalance = prevPageBalance + dateWiseBalance;
					}
			   
		    }
		    prevPageBalance= prevPageBalance+initialBalance;
		    
		    /* --- Current page details --- */
		    List<Content> content=new ArrayList<Content>();
		    content = accountingDAO.listActiveAccountDetails(customerId, strtRow, endRow);
		    
		    if(content.isEmpty())
		    {
		    	throw new NoRecordFound("No records found");
		    }
		    
		    double totalSalesSum=0;
		    double totalExpenditureSum=0;
		    for (Content con : content) {
		    	double dailySalesSum=0;
		    	double dailyPurchaseSum=0;
		    	double dailyFaultSum=0;
		    	double dailyIncomeSum=0;
		    	double dailyExpenditureSum=0;
		    	
		    	if(SALES.equalsIgnoreCase(con.getRecordType())){
		    		dailySalesSum = dailySalesSum + con.getTotal();
		    	}else if(PURCHASE.equalsIgnoreCase(con.getRecordType())){
		    		dailyPurchaseSum = dailyPurchaseSum - con.getTotal();
		    	}else if(FAULT.equalsIgnoreCase(con.getRecordType())){
		    		dailyFaultSum = dailyFaultSum - con.getTotal();
		    	}else if(INCOME.equalsIgnoreCase(con.getRecordType())){
		    		dailyIncomeSum = dailyIncomeSum - con.getTotal();
		    	}else if(EXPENDITURE.equalsIgnoreCase(con.getRecordType())){
		    		dailyExpenditureSum = dailyExpenditureSum + con.getTotal();
		    	}
		    	
		    	
		    	totalSalesSum=totalSalesSum+dailySalesSum+dailyExpenditureSum;// sub-total
		    	totalExpenditureSum=totalExpenditureSum+(dailyPurchaseSum+dailyFaultSum+dailyIncomeSum);// sub-total
			}
		    
		    // calculating the total sum of pages
		    int rsfinalinc=0;
		    double grossSum=0;
		    double grossPurchase=0;
		    double grossFault=0;
		    double grossIncome=0;
		    double grossExpenditure=0;
		    double grossTotal=0;
		    List<Double> grossCalculationsList = accountingDAO.listGrossCalculations(customerId);
		    for (Double gross : grossCalculationsList) {
		    	switch(rsfinalinc){
		    	case 0:
		    		grossSum = gross!=null?gross:0;
		    		break;
		    	case 1:
		    		grossPurchase = gross!=null?gross:0;
		    		break;
		    	case 2:
		    		grossFault = gross!=null?gross:0;
		    		break;
		    	case 3:
		    		grossIncome = gross!=null?gross:0;
		    		break;
		    	case 4:
		    		grossExpenditure = gross!=null?gross:0;
		    		break;
		    	}
				rsfinalinc++;
			}
		    grossTotal = grossSum+grossExpenditure-grossPurchase-grossFault-grossIncome;// final sum
		    
		    if(prevPageBalance<0)
		    {
		    	totalExpenditureSum=totalExpenditureSum+prevPageBalance;
		    }
		    else
		    {
		    	totalSalesSum=totalSalesSum+prevPageBalance;
		    }
		    
		    double total=totalSalesSum+totalExpenditureSum;// page sum
		    
		    //[to-do] pagetotal is equal to gross total so gross total should be removed
		    grossTotal = total;
		    
		    accountsMap.put("data", content);
		    accountsMap.put("total",total);
		    accountsMap.put("totalSales",totalSalesSum);
		    accountsMap.put("totalExpenditure",totalExpenditureSum);
		    accountsMap.put("grossTotal",grossTotal);
		    accountsMap.put("prevBalance",prevPageBalance);
 		return accountsMap;
	}

	@Override
	public long getAccountDetailsCount(long customerId) {
		long rowCount = 0;
		try {
			rowCount = accountingDAO.getActiveAccountsRowCount(customerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowCount;
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public boolean createTransaction(Transaction record) throws Exception {
		if(record.getIsIncome().equalsIgnoreCase("Y")){
			customerDAO.decreaseCurrentBalance(""+record.getTransactionCustomerId(), record.getTransactionAmount());
		}else{
			customerDAO.increaseCurrentBalance(""+record.getTransactionCustomerId(), record.getTransactionAmount());
		}
		return accountingDAO.createTransaction(record);
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public boolean editTransaction(Transaction record) throws Exception  {
		Transaction oldData = getRecordById(record.getTransactionRecordId());
		if(record.getIsIncome().equalsIgnoreCase(oldData.getIsIncome())&&record.getTransactionCustomerId().equals(oldData.getTransactionCustomerId())){
			// same transaction income type and customer
			double change = record.getTransactionAmount() - oldData.getTransactionAmount();
			if(change>0){
				if(record.getIsIncome().equalsIgnoreCase("Y")){
					customerDAO.decreaseCurrentBalance(""+record.getTransactionCustomerId(), change);
				}else{
					customerDAO.increaseCurrentBalance(""+record.getTransactionCustomerId(), change);
				}
			}else if (change<0){
				if(oldData.getIsIncome().equalsIgnoreCase("Y")){
					customerDAO.increaseCurrentBalance(""+oldData.getTransactionCustomerId(), -change);
				}else{
					customerDAO.decreaseCurrentBalance(""+oldData.getTransactionCustomerId(), -change);
				}
			}	
		}else if(record.getTransactionCustomerId().equals(oldData.getTransactionCustomerId())){
			// same customer
			// reversing effect , so double effect changes
			if(record.getIsIncome().equalsIgnoreCase("Y")){
				customerDAO.decreaseCurrentBalance(""+record.getTransactionCustomerId(), record.getTransactionAmount()*2);
			}else{
				customerDAO.increaseCurrentBalance(""+record.getTransactionCustomerId(), record.getTransactionAmount()*2);
			}
		}else{
			// different transaction type and customer
			// nullify oldcustomer changes
			if(oldData.getIsIncome().equalsIgnoreCase("Y")){
				customerDAO.increaseCurrentBalance(""+oldData.getTransactionCustomerId(), record.getTransactionAmount());
			}else{
				customerDAO.decreaseCurrentBalance(""+oldData.getTransactionCustomerId(), record.getTransactionAmount());
			}
			
			// recreate new customer changes
			if(record.getIsIncome().equalsIgnoreCase("Y")){
				customerDAO.decreaseCurrentBalance(""+record.getTransactionCustomerId(), record.getTransactionAmount());
			}else{
				customerDAO.increaseCurrentBalance(""+record.getTransactionCustomerId(), record.getTransactionAmount());
			}
		}
		
		return accountingDAO.editTransaction(record);
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public boolean removeTransaction(long transactionId) throws Exception  {
		Transaction oldData = getRecordById(transactionId);
		if(oldData.getIsIncome().equalsIgnoreCase("Y")){
			customerDAO.increaseCurrentBalance(""+oldData.getTransactionCustomerId(), oldData.getTransactionAmount());
		}else{
			customerDAO.decreaseCurrentBalance(""+oldData.getTransactionCustomerId(), oldData.getTransactionAmount());
		}
		return accountingDAO.softDeleteTransaction(transactionId);
	}

	@Override
	public Transaction getRecordById(long transactionId) {
		return accountingDAO.getTransactionById(transactionId);
	}

	@Override
	public List<BankAccount> listBankAccounts() {
		return accountingDAO.listBankAccounts();
	}

	@Override
	public boolean createBankAccount(BankAccount bankAccount) throws Exception {
		return accountingDAO.createBankAccount(bankAccount);
	}

	@Override
	public List<Transaction> listPendingTransactions(int strtRow,int endRow,int length) {
		return accountingDAO.listTransactionByStatus(TransactionStatus.PENDING.getValue(),strtRow,endRow,length);
	}

	@Override
	public long countPendingTransactions() {
		return accountingDAO.countTransactionByStatus(TransactionStatus.PENDING.getValue());
	}

}
