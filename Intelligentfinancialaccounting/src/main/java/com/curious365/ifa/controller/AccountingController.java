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
		mav.setViewName("addtransaction");
		return mav;
	}
	
	@RequestMapping(value = "/addTransaction", method=RequestMethod.POST)
	public ModelAndView createTransaction(@ModelAttribute Transaction record){
		log.debug("entering.."+ record);
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
		ModelAndView mav = new ModelAndView();
		log.info("printing "+record);
		if(accountingService.createTransaction(record)){
			mav.setViewName("redirect:/customerAccountSheet?pageno="
					+ Constants.DEFAULT_PAGE_NO + "&customername="
					+ record.getTransactionCustomerName()+"&customerid="+record.getTransactionCustomerId());
		}else{
			//if transaction not successful
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value = "/showSalesSheet", method=RequestMethod.GET)
	public ModelAndView showSalesSheet(){
		log.debug("entering..");
		long rowCount = accountingService.getActiveSalesRowCount();
		ModelAndView mav = new ModelAndView();
		mav.addObject("transactionRecordId", 0);
		mav.addObject("pageNo", 0);
		mav.addObject("rowCount", rowCount);
		mav.setViewName("salessheet");
		return mav;
	}
	
	@RequestMapping(value = "/showPurchaseSheet", method=RequestMethod.GET)
	public ModelAndView showPurchaseSheet(){
		log.debug("entering..");
		long rowCount = accountingService.getActivePurchaseRowCount();
		ModelAndView mav = new ModelAndView();
		mav.addObject("transactionRecordId", 0);
		mav.addObject("pageNo", 0);
		mav.addObject("rowCount", rowCount);
		mav.setViewName("purchasesheet");
		return mav;
	}
	
	@RequestMapping(value = "/showFaultSheet", method=RequestMethod.GET)
	public ModelAndView showFaultSheet(){
		log.debug("entering..");
		long rowCount = accountingService.getActiveFaultRowCount();
		ModelAndView mav = new ModelAndView();
		mav.addObject("transactionRecordId", 0);
		mav.addObject("pageNo", 0);
		mav.addObject("rowCount", rowCount);
		mav.setViewName("faultsheet");
		return mav;
	}
	
	@RequestMapping(value="/editTransaction",method=RequestMethod.GET)
	public ModelAndView editTransactionDisplay(@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Transaction transaction = accountingService.getRecordById(recordid);
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
		Date newdate = null ; 
		newformatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			newdate = (Date)newformatter.parse(transaction.getTransactionDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateString=formatter.format(newdate);
		transaction.setTransactionDate(dateString);
		if(accountingService.editTransaction(transaction)){
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
		if(accountingService.removeTransaction(transaction.getTransactionRecordId())){
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
