package com.curious365.ifa.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.dto.PurchaseForm;
import com.curious365.ifa.exceptions.NoStockInHand;
import com.curious365.ifa.service.PurchaseService;

@Controller
public class PurchaseController {
	private Log log = LogFactory.getLog(PurchaseController.class);
	
	@Autowired
	private PurchaseService purchaseService;

	public PurchaseService getPurchaseService() {
		return purchaseService;
	}

	public void setPurchaseService(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}
	
	@RequestMapping(value = "/showPurchaseSheet", method=RequestMethod.GET)
	public ModelAndView showPurchaseSheet(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "info", required = false) String info){
		log.debug("entering..");
		long rowCount = 0;
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
			rowCount = purchaseService.getActivePurchaseRowCountInclPriveleged();
		}else{
			rowCount = purchaseService.getActivePurchaseRowCount();
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("transactionRecordId", 0);
		mav.addObject("pageNo", 0);
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.addObject("rowCount", rowCount);
		mav.setViewName("purchasesheet");
		return mav;
	}

	
	@RequestMapping(value="/addMultiPurchase",method=RequestMethod.GET)
	public ModelAndView addMultiPurchase(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "info", required = false) String info){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("addmultipurchase");
		return mav;
	}
	
	@RequestMapping(value="/addMultiPurchase",method=RequestMethod.POST)
	public ModelAndView addMultiPurchase(@ModelAttribute PurchaseForm purchaseForm){
		log.debug("entering..");
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showPurchaseSheet");
		ModelAndView mav = new ModelAndView();
		List<Purchase> records = new ArrayList<Purchase>();
		String purchaseDate;
		Invoice invoice = new Invoice();
		invoice.setInvoiceCustomerId(purchaseForm.getPurchaseCustomerId());
		invoice.setRemarks(purchaseForm.getInvoiceRemarks());
		invoice.setInvoiceType(InvoiceType.PURCHASE.getValue());
		invoice.setCashPaid(purchaseForm.getCashPaid());
		/* current date */
		Date date = new Date();
		SimpleDateFormat formatter= 
		new SimpleDateFormat("dd/MMM/yyyy");
		String dateNow = formatter.format(date.getTime());
		/* prev date */
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
		if(purchaseForm.getPurchaseDate().equalsIgnoreCase("today"))
		{
			purchaseDate=dateNow;
		}else if(purchaseForm.getPurchaseDate().equalsIgnoreCase("yesterday"))
		{
			purchaseDate=datePrev;
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(purchaseForm.getPurchaseDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			purchaseDate=formatter.format(newdate);
		}
		
		invoice.setInvoiceDate(purchaseDate);
		for (Purchase purchase : purchaseForm.getRecords()) {
			/**
			 * setting customer name and id from common fields
			 */
			purchase.setPurchaseCustomerName(purchaseForm.getPurchaseCustomerName());
			purchase.setPurchaseCustomerId(purchaseForm.getPurchaseCustomerId());
			purchase.setPurchaseDate(purchaseDate);
			records.add(purchase);
		}
		
		try {
			purchaseService.create(records,invoice);
		} catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());	
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to add purchase. Please try with valid data");
		}
		finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/addMultiPurchase");
			}else{
				urlBuilder.queryParam("info", "Purchase successfully added");
			}
		}
		
		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}
	
	@RequestMapping(value = "/addPurchase", method=RequestMethod.POST)
	public ModelAndView addPurchase(@ModelAttribute Purchase record){
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showPurchaseSheet");
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
		if(record.getPurchaseDate().equalsIgnoreCase("today"))
		{
			record.setPurchaseDate(dateNow);
		}else if(record.getPurchaseDate().equalsIgnoreCase("yesterday"))
		{
			record.setPurchaseDate(datePrev);
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(record.getPurchaseDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			record.setPurchaseDate(formatter.format(newdate));
		}
		try{
			//[to-do] check cash checkbox and insert income directly
			purchaseService.create(record);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to add purchase. Please try with valid data");
		}finally{
			if(!hasError){
				urlBuilder.queryParam("info", "Purchase successfully added");
			}
		}
		
		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}

	
	@RequestMapping(value="/editPurchase",method=RequestMethod.GET)
	public ModelAndView editPurchaseDisplay(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "info", required = false) String info,@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Purchase purchase = purchaseService.getRecordById(recordid);
		mav.addObject("record", purchase);
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("editpurchase");
		return mav;
	}
	
	@RequestMapping(value="/editPurchase",method=RequestMethod.POST)
	public ModelAndView editPurchase(@ModelAttribute Purchase purchase){
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showPurchaseSheet");
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		// converting to db supported date format
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MMM/yyyy");
		DateFormat newformatter ; 
		Date newdate = null ; 
		newformatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			newdate = (Date)newformatter.parse(purchase.getPurchaseDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateString=formatter.format(newdate);
		purchase.setPurchaseDate(dateString);
		try{
			purchaseService.edit(purchase);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to update purchase. Please try with valid data");
		}finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/editPurchase");
				urlBuilder.queryParam("recordid", purchase.getPurchaseRecordId());
			}else{
				urlBuilder.queryParam("info", "Purchase successfully updated");
			}
		}

		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}
	
}
