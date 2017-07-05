package com.curious365.ifa.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.curious365.ifa.service.AccountingService;
import com.curious365.ifa.service.ItemService;

@Controller
@SessionAttributes({"quantities","types"})
public class HomeController {
	
	private Log log = LogFactory.getLog(HomeController.class);
	
	@Autowired
	private AccountingService accountingService;
	
	@Autowired
	private ItemService itemService;
	

	@RequestMapping(value={"/home","/"})
	public ModelAndView welcome(){
		log.debug("welcome");
		ModelAndView mav = new ModelAndView();
		mav.addObject("rowCount", accountingService.countPendingTransactions());
		mav.setViewName("welcome");
		return mav;
	}
	
	

}
