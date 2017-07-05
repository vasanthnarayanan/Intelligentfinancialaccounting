package com.curious365.ifa.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.Roles;
import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.dto.State;
import com.curious365.ifa.service.CustomerService;

@RestController
public class CustomerRestController {
	private Log log = LogFactory.getLog(CustomerRestController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping("/listActiveCustomers")
	public Map<String,List<Customer>> listActiveCustomers(){
		log.debug("entering..listActiveCustomers");
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Map<String,List<Customer>> activeCustomerMap = new HashMap<String,List<Customer>>();
		List<Customer> activeCustomerList = new ArrayList<Customer>();
		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
			activeCustomerList = customerService.listActiveCustomersInclPrivileged();
		}else{
			activeCustomerList = customerService.listActiveCustomers();
		}
		
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
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
			return customerService.populateAutocompleteInclPrivileged(query);
		}else{
			return customerService.populateAutocomplete(query);
		}
		
	}
	
	@RequestMapping("/listCustomerIdLike")
	public Map<String,Object> populateIdAutocomplete(@RequestParam("query") String query){
		log.debug("entering..");
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
			return customerService.populateIdAutocompleteInclPrivileged(query);
		}else{
			return customerService.populateIdAutocomplete(query);
		}
	}
	
	@RequestMapping("/listState")
	public List<State> listState(){
		log.debug("entering..");
		return customerService.listState();
	}
	
	
	@RequestMapping(value="/addCustomerAsync",method=RequestMethod.POST)
	public String addCustomer(@ModelAttribute Customer customer){
		log.debug("entering..");
		String message = "Unable to add customer!Please retry.";
		try{
		customerService.addNewCustomer(customer);
		message = "Success";
		}catch(Exception e){
			
		}
		return message;
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
