package com.curious365.ifa.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.curious365.ifa.common.InvoiceType;
import com.curious365.ifa.common.Roles;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.dto.SalesForm;
import com.curious365.ifa.exceptions.NoStockInHand;
import com.curious365.ifa.service.SalesService;

@Controller
public class SalesController {
	private Log log = LogFactory.getLog(SalesController.class);
	
	@Autowired
	private SalesService salesService;

	public SalesService getSalesService() {
		return salesService;
	}

	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}
	
	@RequestMapping(value = "/showSalesSheet", method=RequestMethod.GET)
	public ModelAndView showSalesSheet(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "info", required = false) String info){
		log.debug("entering..");
		long rowCount = 0;
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
			rowCount = salesService.getActiveSalesRowCountInclPriveleged();
		}else{
			rowCount = salesService.getActiveSalesRowCount();
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("transactionRecordId", 0);
		mav.addObject("pageNo", 0);
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.addObject("rowCount", rowCount);
		mav.setViewName("salessheet");
		return mav;
	}
	
	@RequestMapping(value="/addMultiSales",method=RequestMethod.GET)
	public ModelAndView addMultiSales(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "info", required = false) String info){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("addmultisales");
		return mav;
	}
	
	@RequestMapping(value="/addMultiSales",method=RequestMethod.POST)
	public ModelAndView addMultiSales(@ModelAttribute SalesForm salesForm){
		log.debug("entering..");
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showSalesSheet");
		ModelAndView mav = new ModelAndView();
		List<Sales> records = new ArrayList<Sales>();
		String salesDate;
		Invoice invoice = new Invoice();
		invoice.setInvoiceCustomerId(salesForm.getSalesCustomerId());
		invoice.setRemarks(salesForm.getInvoiceRemarks());
		invoice.setInvoiceType(InvoiceType.SALES.getValue());
		invoice.setCashPaid(salesForm.getCashPaid());
		/* current date */
		Date date = new Date();
		SimpleDateFormat formatter= 
		new SimpleDateFormat("dd/MMM/yyyy");
		String dateNow = formatter.format(date.getTime());
		/* prev date */
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
		if(salesForm.getSalesDate().equalsIgnoreCase("today"))
		{
			salesDate=dateNow;
		}else if(salesForm.getSalesDate().equalsIgnoreCase("yesterday"))
		{
			salesDate=datePrev;
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(salesForm.getSalesDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			salesDate=formatter.format(newdate);
		}
		
		invoice.setInvoiceDate(salesDate);
		for (Sales sales : salesForm.getRecords()) {
			/**
			 * setting customer name and id from common fields
			 */
			sales.setSalesCustomerName(salesForm.getSalesCustomerName());
			sales.setSalesCustomerId(salesForm.getSalesCustomerId());
			sales.setSalesDate(salesDate);
			records.add(sales);
		}
		
		try {
			salesService.create(records,invoice);
		} catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());	
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to add sales. Please try with valid data");
		}
		finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/addMultiSales");
			}else{
				urlBuilder.queryParam("info", "Sales successfully added");
			}
		}
		
		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}
	
	@RequestMapping(value = "/addSales", method=RequestMethod.POST)
	public ModelAndView addSales(@ModelAttribute Sales record){
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showSalesSheet");
		log.debug("entering.."+ record);
		ModelAndView mav = new ModelAndView();
		/* current date */
		Date date = new Date();
		SimpleDateFormat formatter= 
		new SimpleDateFormat("dd/MMM/yyyy");
		String dateNow = formatter.format(date.getTime());
		/* prev date */
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
		if(record.getSalesDate().equalsIgnoreCase("today"))
		{
			record.setSalesDate(dateNow);
		}else if(record.getSalesDate().equalsIgnoreCase("yesterday"))
		{
			record.setSalesDate(datePrev);
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(record.getSalesDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			record.setSalesDate(formatter.format(newdate));
		}
		try{
			//[to-do] check cash checkbox and insert income directly
			salesService.create(record);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to add sales. Please try with valid data");
		}finally{
			if(!hasError){
				urlBuilder.queryParam("info", "Sales successfully added");
			}
		}
		
		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}

	
	@RequestMapping(value="/editSales",method=RequestMethod.GET)
	public ModelAndView editSalesDisplay(@RequestParam(value = "error", required = false) String error,
	@RequestParam(value = "info", required = false) String info,@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Sales sales = salesService.getRecordById(recordid);
		mav.addObject("record", sales);
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("editsales");
		return mav;
	}
	
	@RequestMapping(value="/editSales",method=RequestMethod.POST)
	public ModelAndView editSales(@ModelAttribute Sales sales){
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showSalesSheet");
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		// converting to db supported date format
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MMM/yyyy");
		DateFormat newformatter ; 
		Date newdate = null ; 
		newformatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			newdate = (Date)newformatter.parse(sales.getSalesDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String dateString=formatter.format(newdate);
		sales.setSalesDate(dateString);
		try{
			salesService.edit(sales);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to update sales. Please try with valid data");
		}finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/editSales");
				urlBuilder.queryParam("recordid", sales.getSalesRecordId());
			}else{
				urlBuilder.queryParam("info", "Sales successfully updated");
			}
		}

		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}
	
}
