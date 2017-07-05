package com.curious365.ifa.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.service.CustomerService;

@Controller
public class CustomerController {

	private Log log = LogFactory.getLog(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	@RequestMapping(value = "/listCustomers", method=RequestMethod.GET)
	public ModelAndView listCustomers(@RequestParam("pageno")int pageNo,@RequestParam("sorttype")String sortType){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageno", pageNo);
		mav.addObject("sorttype", sortType);
		mav.setViewName("customerindex");
		return mav;
	}
	
	@RequestMapping(value="/addCustomer",method=RequestMethod.GET)
	public ModelAndView addCustomerDisplay(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.addObject("states", customerService.listState());
		mav.setViewName("addcustomer");
		return mav;
	}
	
	@RequestMapping(value="/addCustomer",method=RequestMethod.POST)
	public ModelAndView addCustomer(@ModelAttribute Customer customer){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		customerService.addNewCustomer(customer);
		mav.addObject("pageno", 1);
		mav.addObject("sorttype", "normal");
		mav.setViewName("customerindex");
		return mav;
	}
	
	@RequestMapping(value="/editCustomer",method=RequestMethod.GET)
	public ModelAndView editCustomerDisplay(@RequestParam(value="customerid",required=false)String customerId){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Customer customer = null;
		if(StringUtils.hasText(customerId)){
			customer = customerService.getCustomerById(customerId);	
		}
		
		if(null != customer){
			customer.setActiveFlag(Constants.ONE);
		}else{
			customer = new Customer();
			/*customer.setName(Constants.EMPTY_STRING);
			customer.setCustomerId(Constants.EMPTY_STRING); 
			customer.setInitialBalance(Constants.ZERO);
			customer.setCustomerAddress(Constants.EMPTY_STRING);
			customer.setCustomerPhoneNumber(Constants.EMPTY_STRING);*/
			customer.setActiveFlag(Constants.ZERO);
		}
		mav.addObject("customer", customer);
		mav.addObject("states", customerService.listState());
		mav.setViewName("editcustomer");
		return mav;
	}
	
	@RequestMapping(value="/editCustomer",method=RequestMethod.POST)
	public ModelAndView editCustomer(@ModelAttribute Customer customer){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		try {
			customerService.editCustomer(customer);
		} catch (Exception e) {
		}
		mav.addObject("pageno", 1);
		mav.addObject("sorttype", "normal");
		mav.setViewName("customerindex");
		return mav;
	}
	
	
	@RequestMapping(value="/removeCustomer",method=RequestMethod.GET)
	public ModelAndView removeCustomerDisplay(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("removecustomer");
		return mav;
	}
	
	@RequestMapping(value="/removeCustomerNormal",method=RequestMethod.POST)
	public ModelAndView removeCustomer(@ModelAttribute Customer customer){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		customerService.removeCustomer(customer.getCustomerId());
		mav.addObject("pageno", 1);
		mav.addObject("sorttype", "normal");
		mav.setViewName("customerindex");
		return mav;
	}

	

}
