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
import org.springframework.web.util.UriComponentsBuilder;

import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.exceptions.NoStockInHand;
import com.curious365.ifa.service.InvoiceService;

@Controller
public class InvoiceController {
	private Log log = LogFactory.getLog(InvoiceController.class);
	
	@Autowired
	private InvoiceService invoiceService;
	
	@RequestMapping(value="/editInvoice",method=RequestMethod.GET)
	public ModelAndView editInvoiceDisplay(@RequestParam(value = "error", required = false) String error,
	@RequestParam(value = "info", required = false) String info,@RequestParam long invoiceid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Invoice invoice = null;
		try {
			invoice = invoiceService.getInvoiceWtDetails(invoiceid);
		} catch (Exception e) {
		}
		mav.addObject("invoice", invoice);
		mav.addObject("records", invoice.getRecords());
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("editinvoice");
		return mav;
	}
	
	@RequestMapping(value="/editInvoice",method=RequestMethod.POST)
	public ModelAndView editInvoice(@ModelAttribute Invoice invoice){
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
			newdate = (Date)newformatter.parse(invoice.getInvoiceDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateString=formatter.format(newdate);
		invoice.setInvoiceDate(dateString);
		try{
			invoiceService.edit(invoice);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to update invoice. Please try with valid data");
		}finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/editInvoice");
				urlBuilder.queryParam("invoiceid", invoice.getInvoiceId());
			}else{
				urlBuilder.queryParam("info", "Invoice successfully updated");
			}
		}

		mav.setViewName(urlBuilder.build().encode().toUriString());
		return mav;
	}
	
	
}
