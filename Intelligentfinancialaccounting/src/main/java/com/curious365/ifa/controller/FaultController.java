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

import com.curious365.ifa.dto.Fault;
import com.curious365.ifa.dto.FaultForm;
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
	
	@RequestMapping(value="/addMultiFault",method=RequestMethod.GET)
	public ModelAndView addMultiFault(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addmultifault");
		return mav;
	}
	
	@RequestMapping(value="/addMultiFault",method=RequestMethod.POST)
	public ModelAndView addMultiFault(@ModelAttribute FaultForm faultForm){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		for (Fault fault : faultForm.getRecords()) {
			/**
			 * setting customer name and id from common fields
			 */
			fault.setFaultCustomerName(faultForm.getFaultCustomerName());
			fault.setFaultCustomerId(faultForm.getFaultCustomerId());
			
			/* current date */
			Date date = new Date();
			SimpleDateFormat formatter= 
			new SimpleDateFormat("dd/MMM/yyyy");
			String dateNow = formatter.format(date.getTime());
			/* prev date */
			int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
			String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
			if(fault.getFaultDate().equalsIgnoreCase("today"))
			{
				fault.setFaultDate(dateNow);
			}else if(fault.getFaultDate().equalsIgnoreCase("yesterday"))
			{
				fault.setFaultDate(datePrev);
			}else
			{
				DateFormat newformatter ; 
				Date newdate = null ; 
				newformatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
					newdate = (Date)newformatter.parse(fault.getFaultDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				fault.setFaultDate(formatter.format(newdate));
			}
			faultService.create(fault);
		}
		mav.setViewName("redirect:/showFaultSheet");
		return mav;
	}
	
	@RequestMapping(value = "/addFault", method=RequestMethod.POST)
	public ModelAndView addFault(@ModelAttribute Fault record){
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
		faultService.create(record);
		//[to-do] check cash checkbox and insert income directly
		mav.setViewName("redirect:/showFaultSheet");
		return mav;
	}

	
	@RequestMapping(value="/editFault",method=RequestMethod.GET)
	public ModelAndView editFaultDisplay(@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Fault fault = faultService.getRecordById(recordid);
		mav.addObject("record", fault);
		mav.setViewName("editfault");
		return mav;
	}
	
	@RequestMapping(value="/editFault",method=RequestMethod.POST)
	public ModelAndView editItem(@ModelAttribute Fault fault){
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
		faultService.edit(fault);
		mav.setViewName("redirect:/showFaultSheet");
		return mav;
	}
	
}
