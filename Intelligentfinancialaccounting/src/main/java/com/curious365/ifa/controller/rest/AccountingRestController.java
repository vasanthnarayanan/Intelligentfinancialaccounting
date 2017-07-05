package com.curious365.ifa.controller.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.dto.BankAccount;
import com.curious365.ifa.dto.Datatable;
import com.curious365.ifa.dto.Transaction;
import com.curious365.ifa.service.AccountingService;

@RestController
public class AccountingRestController {
	private Log log = LogFactory.getLog(AccountingRestController.class);
	
	@Autowired
	private AccountingService accountingService;

	public AccountingService getAccountingService() {
		return accountingService;
	}

	public void setAccountingService(AccountingService accountingService) {
		this.accountingService = accountingService;
	}
	
	@RequestMapping(value = "/listActiveAccounts")
	public Map<String,Object> listActiveAccounts(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		Map<String,Object> accountMap = new HashMap<String,Object>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			accountMap = accountingService.getCustomerAccountDetail(datatable.getCustomerId(),datatable.getInitialBalance(),strtRow, endRow,datatable.getLength());
		} catch (Exception e) {
			accountMap.put("data", new ArrayList());
		};
			accountMap.put("draw", datatable.getDraw());
			accountMap.put("recordsTotal", datatable.getRowCount());
			accountMap.put("recordsFiltered", datatable.getRowCount());	
		return accountMap;
	}
	
	@RequestMapping(value = "/listPendingTransactions")
	public Map<String,Object> listPendingTransactions(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		Map<String,Object> transactionMap = new HashMap<String,Object>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			List<Transaction> pendingTransactionList = accountingService.listPendingTransactions(strtRow, endRow,datatable.getLength());
			transactionMap.put("data", pendingTransactionList);
		} catch (Exception e) {
			transactionMap.put("data", new ArrayList());
		};
			transactionMap.put("draw", datatable.getDraw());
			transactionMap.put("recordsTotal", datatable.getRowCount());
			transactionMap.put("recordsFiltered", datatable.getRowCount());	
		return transactionMap;
	}
	
	@RequestMapping(value = "/addBankAsync", method=RequestMethod.POST)
	public String createBankAccount(@ModelAttribute BankAccount bankAccount){
		log.debug("entering..");
		String message = "Unable to add account! Please try with valid data";
		try{
			if(accountingService.createBankAccount(bankAccount)){
				message = "Success";
			}
		}catch(Exception e){
			
		}
		
		return message;
	}
	
	@RequestMapping(value = "/addTransactionAsync", method=RequestMethod.POST)
	public String createTransaction(@ModelAttribute Transaction record){
		log.debug("entering.."+ record);
		String message = "Unable to add transaction! Please try with valid data";
		String dateString=null;
		/* current date */
		Date date = new Date();
		SimpleDateFormat formatter= 
		new SimpleDateFormat("dd/MMM/yyyy");
		String dateNow = formatter.format(date.getTime());
		/* prev date */
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
		
		if(record.getTransactionDate().equalsIgnoreCase("today"))
		{
			dateString=dateNow;
		}else if(record.getTransactionDate().equalsIgnoreCase("yesterday"))
		{
			dateString=datePrev;
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(record.getTransactionDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dateString=formatter.format(newdate);
		}
		record.setTransactionDate(dateString);
		log.info("printing "+record);
		try{
			if(accountingService.createTransaction(record)){
				message = "Success";
			}
		}catch(Exception e){
			
		}
		
		return message;
	}
	
	
	@RequestMapping(value = "/editTransactionAsync", method=RequestMethod.POST)
	public String editTransaction(@ModelAttribute Transaction transaction){
		log.debug("entering..");
		Transaction transactionToUpdate = accountingService.getRecordById(transaction.getTransactionRecordId());
		transactionToUpdate.setTransactionStatus(transaction.getTransactionStatus());
		String message = "Unable to update transaction! Please try with valid data";
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MMM/yyyy");
		DateFormat newformatter ; 
		Date transactionDate = null ;
		Date dueDate = null ; 
		newformatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			transactionDate = (Date)newformatter.parse(transactionToUpdate.getTransactionDate());
			dueDate = (Date)newformatter.parse(transactionToUpdate.getDueDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String transactionDateString=formatter.format(transactionDate);
		String dueDateString=formatter.format(dueDate);
		transactionToUpdate.setTransactionDate(transactionDateString);
		transactionToUpdate.setDueDate(dueDateString);
		log.info("printing "+transactionToUpdate);
		try{
			if(accountingService.editTransaction(transactionToUpdate)){
				message = "Success";
			}
		}catch(Exception e){
			
		}
		
		return message;
	}
	
}
