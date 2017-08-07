package com.curious365.ifa.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrintController {
	
	private Log log = LogFactory.getLog(HomeController.class);
	
	@RequestMapping(value={"/taxInvoicePrint"})
	public ModelAndView taxInvoicePrint(){
		log.debug("/taxInvoicePrint");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("taxinvoiceprint");
		return mav;
	}

}
