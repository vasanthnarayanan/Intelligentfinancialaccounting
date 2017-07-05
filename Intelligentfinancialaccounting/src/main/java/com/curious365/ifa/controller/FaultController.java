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
import com.curious365.ifa.dto.Fault;
import com.curious365.ifa.dto.FaultForm;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.exceptions.NoStockInHand;
import com.curious365.ifa.service.FaultService;

@Controller
public class FaultController {
	private Log log = LogFactory.getLog(FaultController.class);
	
	@Autowired
	private FaultService faultService;

	public FaultService getFaultService() {
		return faultService;
	}

	public void setFaultService(FaultService faultService) {
		this.faultService = faultService;
	}
	
	@RequestMapping(value = "/showFaultSheet", method=RequestMethod.GET)
	public ModelAndView showFaultSheet(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "info", required = false) String info){
		log.debug("entering..");
		long rowCount = 0;
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
			rowCount = faultService.getActiveFaultRowCountInclPriveleged();
		}else{
			rowCount = faultService.getActiveFaultRowCount();
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("transactionRecordId", 0);
		mav.addObject("pageNo", 0);
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.addObject("rowCount", rowCount);
		mav.setViewName("faultsheet");
		return mav;
	}
	
	@RequestMapping(value="/addMultiFault",method=RequestMethod.GET)
	public ModelAndView addMultiFault(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "info", required = false) String info){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("addmultifault");
		return mav;
	}
	
	@RequestMapping(value="/addMultiFault",method=RequestMethod.POST)
	public ModelAndView addMultiFault(@ModelAttribute FaultForm faultForm){
		log.debug("entering..");
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showFaultSheet");
		ModelAndView mav = new ModelAndView();
		List<Fault> records = new ArrayList<Fault>();
		String faultDate;
		Invoice invoice = new Invoice();
		invoice.setInvoiceCustomerId(faultForm.getFaultCustomerId());
		invoice.setRemarks(faultForm.getInvoiceRemarks());
		invoice.setInvoiceType(InvoiceType.FAULT.getValue());
		invoice.setCashPaid(faultForm.getCashPaid());
		/* current date */
		Date date = new Date();
		SimpleDateFormat formatter= 
		new SimpleDateFormat("dd/MMM/yyyy");
		String dateNow = formatter.format(date.getTime());
		/* prev date */
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
		if(faultForm.getFaultDate().equalsIgnoreCase("today"))
		{
			faultDate=dateNow;
		}else if(faultForm.getFaultDate().equalsIgnoreCase("yesterday"))
		{
			faultDate=datePrev;
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(faultForm.getFaultDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			faultDate=formatter.format(newdate);
		}
		
		invoice.setInvoiceDate(faultDate);
		for (Fault fault : faultForm.getRecords()) {
			/**
			 * setting customer name and id from common fields
			 */
			fault.setFaultCustomerName(faultForm.getFaultCustomerName());
			fault.setFaultCustomerId(faultForm.getFaultCustomerId());
			fault.setFaultDate(faultDate);
			records.add(fault);
		}
		
		try {
			faultService.create(records,invoice);
		} catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());	
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to add fault. Please try with valid data");
		}
		finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/addMultiFault");
			}else{
				urlBuilder.queryParam("info", "Fault successfully added");
			}
		}
		
		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}
	
	@RequestMapping(value = "/addFault", method=RequestMethod.POST)
	public ModelAndView addFault(@ModelAttribute Fault record){
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showFaultSheet");
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
		if(record.getFaultDate().equalsIgnoreCase("today"))
		{
			record.setFaultDate(dateNow);
		}else if(record.getFaultDate().equalsIgnoreCase("yesterday"))
		{
			record.setFaultDate(datePrev);
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(record.getFaultDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			record.setFaultDate(formatter.format(newdate));
		}
		try{
			//[to-do] check cash checkbox and insert income directly
			faultService.create(record);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to add fault. Please try with valid data");
		}finally{
			if(!hasError){
				urlBuilder.queryParam("info", "Fault successfully added");
			}
		}
		
		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}

	
	@RequestMapping(value="/editFault",method=RequestMethod.GET)
	public ModelAndView editFaultDisplay(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "info", required = false) String info,@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Fault fault = faultService.getRecordById(recordid);
		mav.addObject("record", fault);
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("editfault");
		return mav;
	}
	
	@RequestMapping(value="/editFault",method=RequestMethod.POST)
	public ModelAndView editFault(@ModelAttribute Fault fault){
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showFaultSheet");
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		// converting to db supported date format
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MMM/yyyy");
		DateFormat newformatter ; 
		Date newdate = null ; 
		newformatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			newdate = (Date)newformatter.parse(fault.getFaultDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateString=formatter.format(newdate);
		fault.setFaultDate(dateString);
		try{
			faultService.edit(fault);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to update fault. Please try with valid data");
		}finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/editFault");
				urlBuilder.queryParam("recordid", fault.getFaultRecordId());
			}else{
				urlBuilder.queryParam("info", "Fault successfully updated");
			}
		}

		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}
	
}
