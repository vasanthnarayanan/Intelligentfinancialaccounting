package com.curious365.ifa.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.dto.PurchaseForm;
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
	
	@RequestMapping(value="/addMultiPurchase",method=RequestMethod.GET)
	public ModelAndView addMultiPurchase(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addmultipurchase");
		return mav;
	}
	
	@RequestMapping(value="/addMultiPurchase",method=RequestMethod.POST)
	public ModelAndView addMultiPurchase(@ModelAttribute PurchaseForm purchaseForm){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		for (Purchase purchase : purchaseForm.getRecords()) {
			/**
			 * setting customer name and id from common fields
			 */
			purchase.setPurchaseCustomerName(purchaseForm.getPurchaseCustomerName());
			purchase.setPurchaseCustomerId(purchaseForm.getPurchaseCustomerId());
			
			/* current date */
			Date date = new Date();
			SimpleDateFormat formatter= 
			new SimpleDateFormat("dd/MMM/yyyy");
			String dateNow = formatter.format(date.getTime());
			/* prev date */
			int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
			String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
			if(purchase.getPurchaseDate().equalsIgnoreCase("today"))
			{
				purchase.setPurchaseDate(dateNow);
			}else if(purchase.getPurchaseDate().equalsIgnoreCase("yesterday"))
			{
				purchase.setPurchaseDate(datePrev);
			}else
			{
				DateFormat newformatter ; 
				Date newdate = null ; 
				newformatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
					newdate = (Date)newformatter.parse(purchase.getPurchaseDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				purchase.setPurchaseDate(formatter.format(newdate));
			}
			purchaseService.create(purchase);
		}
		mav.setViewName("redirect:/showPurchaseSheet");
		return mav;
	}
	
	@RequestMapping(value = "/addPurchase", method=RequestMethod.POST)
	public ModelAndView addPurchase(@ModelAttribute Purchase record){
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
		purchaseService.create(record);
		//[to-do] check cash checkbox and insert income directly
		mav.setViewName("redirect:/showPurchaseSheet");
		return mav;
	}

	
	@RequestMapping(value="/editPurchase",method=RequestMethod.GET)
	public ModelAndView editPurchaseDisplay(@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Purchase purchase = purchaseService.getRecordById(recordid);
		mav.addObject("record", purchase);
		mav.setViewName("editpurchase");
		return mav;
	}
	
	@RequestMapping(value="/editPurchase",method=RequestMethod.POST)
	public ModelAndView editItem(@ModelAttribute Purchase purchase){
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
		purchaseService.edit(purchase);
		mav.setViewName("redirect:/showPurchaseSheet");
		return mav;
	}
	
}
