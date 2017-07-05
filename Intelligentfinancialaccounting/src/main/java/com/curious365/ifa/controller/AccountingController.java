package com.curious365.ifa.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.dto.Transaction;
import com.curious365.ifa.service.AccountingService;
import com.curious365.ifa.service.CustomerService;

@Controller
public class AccountingController {
	private Log log = LogFactory.getLog(CustomerController.class);
	
	public AccountingService getAccountingService() {
		return accountingService;
	}

	public void setAccountingService(AccountingService accountingService) {
		this.accountingService = accountingService;
	}
	
	@Autowired
	private AccountingService accountingService;
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value = "/customerAccountSheet", method=RequestMethod.GET)
	public ModelAndView listCustomers(@RequestParam("pageno")int pageNo,@RequestParam("customername")String customerName,@RequestParam("customerid")long customerId,@RequestParam(value="initialbalance",required=false)Double initialbalance){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("customeraccountsheet");
		long rowCount = accountingService.getAccountDetailsCount(customerId);
		if(null==initialbalance){
			Customer customer = customerService.getCustomerById(String.valueOf(customerId));
			initialbalance =  customer.getInitialBalance();
		}
		mav.addObject("rowCount", rowCount);
		mav.addObject("pageNo", pageNo);
		mav.addObject("customerName",StringUtils.capitalize(customerName));
		mav.addObject("customerId",customerId);
		mav.addObject("initialBalance",initialbalance);
		return mav;
	}
	
	@RequestMapping(value = "/addTransaction", method=RequestMethod.GET)
	public ModelAndView addTransactionView(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.addObject("banks", accountingService.listBankAccounts());
		mav.setViewName("addtransaction");
		return mav;
	}
	
	@RequestMapping(value = "/addTransaction", method=RequestMethod.POST)
	public ModelAndView createTransaction(@ModelAttribute Transaction record){
		log.debug("entering.."+ record);
		String transactionDateString=null;
		String dueDateString=null;
		/* current date */
		Date date = new Date();
		SimpleDateFormat formatter= 
		new SimpleDateFormat("dd/MMM/yyyy");
		String dateNow = formatter.format(date.getTime());
		/* prev date */
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
		String dateNext = formatter.format(date.getTime() + MILLIS_IN_DAY);
		if(record.getTransactionDate().equalsIgnoreCase("today"))
		{
			transactionDateString=dateNow;
		}else if(record.getTransactionDate().equalsIgnoreCase("yesterday"))
		{
			transactionDateString=datePrev;
		}else
		{
			DateFormat newformatter ; 
			Date transactionDate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				transactionDate = (Date)newformatter.parse(record.getTransactionDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			transactionDateString=formatter.format(transactionDate);
		}
		record.setTransactionDate(transactionDateString);
		
		if(record.getDueDate().equalsIgnoreCase("today"))
		{
			dueDateString=dateNow;
		}else if(record.getDueDate().equalsIgnoreCase("tomorrow"))
		{
			dueDateString=dateNext;
		}else
		{
			DateFormat newformatter ; 
			Date dueDate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				dueDate = (Date)newformatter.parse(record.getDueDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dueDateString=formatter.format(dueDate);
		}
		record.setDueDate(dueDateString);
		ModelAndView mav = new ModelAndView();
		log.info("printing "+record);
		
		boolean isSuccess = false;
		try {
			isSuccess = accountingService.createTransaction(record);
		} catch (Exception e) {
		}
		
		
		if(isSuccess){
			mav.setViewName("redirect:/customerAccountSheet?pageno="
					+ Constants.DEFAULT_PAGE_NO + "&customername="
					+ record.getTransactionCustomerName()+"&customerid="+record.getTransactionCustomerId());
		}else{
			//if transaction not successful
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/editTransaction",method=RequestMethod.GET)
	public ModelAndView editTransactionDisplay(@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Transaction transaction = accountingService.getRecordById(recordid);
		mav.addObject("banks", accountingService.listBankAccounts());
		mav.addObject("record", transaction);
		mav.setViewName("edittransaction");
		return mav;
	}
	
	@RequestMapping(value="/editTransaction",method=RequestMethod.POST)
	public ModelAndView editTransaction(@ModelAttribute Transaction transaction){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		// converting to db supported date format
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MMM/yyyy");
		DateFormat newformatter ; 
		Date transactionDate = null ;
		Date dueDate = null ; 
		newformatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			transactionDate = (Date)newformatter.parse(transaction.getTransactionDate());
			dueDate = (Date)newformatter.parse(transaction.getDueDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String transactionDateString=formatter.format(transactionDate);
		String dueDateString=formatter.format(dueDate);
		transaction.setTransactionDate(transactionDateString);
		transaction.setDueDate(dueDateString);
		boolean isSuccess = false;
		try {
			isSuccess = accountingService.editTransaction(transaction);
		} catch (Exception e) {
		}
		if(isSuccess){
			mav.setViewName("redirect:/customerAccountSheet?pageno="
					+ Constants.DEFAULT_PAGE_NO + "&customername="
					+ transaction.getTransactionCustomerName()+"&customerid="+transaction.getTransactionCustomerId());
		}else{
			//if transaction not successful
			mav.setViewName("error");
		}

		return mav;
	}
	
	@RequestMapping(value="/removeTransaction",method=RequestMethod.POST)
	public ModelAndView removeTransaction(@ModelAttribute Transaction transaction){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		boolean isSuccess = false;
		try {
			isSuccess = accountingService.removeTransaction(transaction.getTransactionRecordId());
		} catch (Exception e) {
		}
		if(isSuccess){
			mav.setViewName("redirect:/customerAccountSheet?pageno="
					+ Constants.DEFAULT_PAGE_NO + "&customername="
					+ transaction.getTransactionCustomerName()+"&customerid="+transaction.getTransactionCustomerId());
		}else{
			//if transaction not successful
			mav.setViewName("error");
		}
		return mav;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

}
