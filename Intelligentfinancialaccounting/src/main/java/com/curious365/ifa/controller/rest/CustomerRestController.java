package com.curious365.ifa.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.service.CustomerService;

@RestController
public class CustomerRestController {
	private Log log = LogFactory.getLog(CustomerRestController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping("/listActiveCustomers")
	public Map<String,List<Customer>> listActiveCustomers(){
		log.debug("entering..listActiveCustomers");
		Map<String,List<Customer>> activeCustomerMap = new HashMap<String,List<Customer>>();
		List<Customer> activeCustomerList = customerService.listActiveCustomers();
		activeCustomerMap.put("data", activeCustomerList);
		return activeCustomerMap;
	}
	
	@RequestMapping("/checkCustomerName")
	public String checkCustomerName(@RequestParam("customername") String customerName){
		log.debug("entering..");
		return customerService.checkCustomerNameAvailability(customerName);
	}
	
	@RequestMapping("/listCustomersLike")
	public Map<String,Object> populateAutocomplete(@RequestParam("query") String query){
		log.debug("entering..");
		if(StringUtils.hasText(query)){
			query = query.toLowerCase();
		}
		return customerService.populateAutocomplete(query);
	}
	
	@RequestMapping("/listCustomerIdLike")
	public Map<String,Object> populateIdAutocomplete(@RequestParam("query") String query){
		log.debug("entering..");
		return customerService.populateIdAutocomplete(query);
	}
	
	@RequestMapping("/getCustomerById")
	public Customer getCustomerById(@RequestParam("customerid")String customerId){
		log.debug("entering..");
		Customer customer = null;
		if(StringUtils.hasText(customerId)){
			customer = customerService.getCustomerById(customerId);	
		}
		
		if(null != customer){
			customer.setActiveFlag(Constants.ONE);
		}else{
			customer = new Customer();
			customer.setName(Constants.EMPTY_STRING);
			customer.setCustomerId(Constants.EMPTY_STRING);
			customer.setActiveFlag(Constants.ZERO);
		}
		return customer;
	}
	
	@RequestMapping(value="/removeCustomer", method=RequestMethod.POST)
	public boolean removeCustomer(@RequestParam("customerid")String customerId){
		log.debug("entering..");
		return customerService.removeCustomer(customerId);
	}


	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
