package com.curious365.ifa.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.curious365.ifa.dto.AuditedSales;
import com.curious365.ifa.exceptions.NoStockInHand;
import com.curious365.ifa.service.AuditedSalesService;

@Controller
public class AuditedSaleController {
	private Log log = LogFactory.getLog(AuditedSaleController.class);
	
	@Autowired
	private AuditedSalesService auditedSalesService;
	
	@RequestMapping(value="/listAuditedSales",method=RequestMethod.GET)
	public ModelAndView showAuditedSalesByMonth(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		/** set default sales month view **/
		Calendar cal = Calendar.getInstance();
		
		DateFormat month = new SimpleDateFormat("MMM"); // month, like JUN digits
		DateFormat year = new SimpleDateFormat("yy"); // year, with 2 digits
		
		StringBuffer sb = new StringBuffer();
		sb.append((month.format(cal.getTime())).toUpperCase());
		sb.append("-");
		sb.append(year.format(cal.getTime()));
		
		mav.addObject("monthOfYear", sb.toString());
		mav.setViewName("listauditedsales");
		return mav;
	}
	
	@RequestMapping(value="/editAuditedSales",method=RequestMethod.GET)
	public ModelAndView editSalesDisplay(@RequestParam(value = "error", required = false) String error,
	@RequestParam(value = "info", required = false) String info,@RequestParam long recordid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		AuditedSales auditedSales = auditedSalesService.getRecordById(recordid);
		mav.addObject("record", auditedSales);
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("editauditedsales");
		return mav;
	}
	
	@RequestMapping(value="/editAuditedSales",method=RequestMethod.POST)
	public ModelAndView editSales(@ModelAttribute AuditedSales auditedSales){
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/listAuditedSales");
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		// converting to db supported date format
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MMM/yyyy");
		DateFormat newformatter ; 
		Date newdate = null ; 
		newformatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			newdate = (Date)newformatter.parse(auditedSales.getSalesDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String dateString=formatter.format(newdate);
		auditedSales.setSalesDate(dateString);
		try{
			auditedSalesService.editAuditedSales(auditedSales);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to update auditedSales. Please try with valid data");
		}finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/editAuditedSales");
				urlBuilder.queryParam("recordid", auditedSales.getSalesRecordId());
			}else{
				urlBuilder.queryParam("info", "Audited Sales successfully updated");
			}
		}

		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}

}
