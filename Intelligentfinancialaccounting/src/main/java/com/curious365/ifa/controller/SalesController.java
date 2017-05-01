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

import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.dto.SalesForm;
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
	
	@RequestMapping(value="/addMultiSales",method=RequestMethod.GET)
	public ModelAndView addMultiSales(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addmultisales");
		return mav;
	}
	
	@RequestMapping(value="/addMultiSales",method=RequestMethod.POST)
	public ModelAndView addMultiSales(@ModelAttribute SalesForm salesForm){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		for (Sales sales : salesForm.getRecords()) {
			/**
			 * setting customer name and id from common fields
			 */
			sales.setSalesCustomerName(salesForm.getSalesCustomerName());
			sales.setSalesCustomerId(salesForm.getSalesCustomerId());
			
			/* current date */
			Date date = new Date();
			SimpleDateFormat formatter= 
			new SimpleDateFormat("dd/MMM/yyyy");
			String dateNow = formatter.format(date.getTime());
			/* prev date */
			int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
			String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
			if(sales.getSalesDate().equalsIgnoreCase("today"))
			{
				sales.setSalesDate(dateNow);
			}else if(sales.getSalesDate().equalsIgnoreCase("yesterday"))
			{
				sales.setSalesDate(datePrev);
			}else
			{
				DateFormat newformatter ; 
				Date newdate = null ; 
				newformatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
					newdate = (Date)newformatter.parse(sales.getSalesDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				sales.setSalesDate(formatter.format(newdate));
			}
			salesService.create(sales);
		}
		mav.setViewName("redirect:/showSalesSheet");
		return mav;
	}
	
	@RequestMapping(value = "/addSales", method=RequestMethod.POST)
	public ModelAndView addSales(@ModelAttribute Sales record){
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
		salesService.create(record);
		//[to-do] check cash checkbox and insert income directly
		mav.setViewName("redirect:/showSalesSheet");
		return mav;
	}

	
	@RequestMapping(value="/editSales",method=RequestMethod.GET)
	public ModelAndView editSalesDisplay(@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Sales sales = salesService.getRecordById(recordid);
		mav.addObject("record", sales);
		mav.setViewName("editsales");
		return mav;
	}
	
	@RequestMapping(value="/editSales",method=RequestMethod.POST)
	public ModelAndView editItem(@ModelAttribute Sales sales){
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
		salesService.edit(sales);
		mav.setViewName("redirect:/showSalesSheet");
		return mav;
	}
	
}
