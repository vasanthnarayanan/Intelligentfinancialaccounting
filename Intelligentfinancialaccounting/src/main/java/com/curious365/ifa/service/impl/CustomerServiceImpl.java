package com.curious365.ifa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dao.CustomerDAO;
import com.curious365.ifa.dto.Autocomplete;
import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.dto.State;
import com.curious365.ifa.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDAO customerDAO;

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	@Override
	public List<Customer> listActiveCustomers() {
		return customerDAO.listCustomers(Constants.ACTIVE);
	}
	
	@Override
	public List<Customer> listActiveCustomersInclPrivileged() {
		return customerDAO.listCustomersInclPriveleged(Constants.ACTIVE);
	}


	@Override
	public String checkCustomerNameAvailability(String customerName) {
		String availability = Constants.UNAVAILABLE;
		if(StringUtils.hasText(customerName.trim()) && customerDAO.checkCustomerNameAvailability(customerName)){
			availability = Constants.AVAILABLE;
		}
		return availability;
	}

	@Override
	public long addNewCustomer(Customer customer) {
		customerDAO.addNewCustomer(customer);
		return customerDAO.getCurrentCustomerId();
	}

	@Override
	public Map<String, Object> populateAutocomplete(String query) {
		StringBuilder sb = new StringBuilder();
		Map<String,Object> similarItems = new HashMap<String,Object>();
		List<Autocomplete> suggestions = new ArrayList<Autocomplete>();
		sb.append(query);
		sb.append(Constants.PERCENTAGE);
		List<Customer> items = customerDAO.listCustomerLike(sb.toString());
		for (Customer customer : items) {
			Autocomplete suggestion = new Autocomplete();
			suggestion.setData(customer.getCustomerId());
			suggestion.setValue(customer.getName());
			suggestions.add(suggestion);
		}
		similarItems.put(Constants.QUERY, query);
		similarItems.put(Constants.SUGGESTIONS, suggestions);
		return similarItems;
	}
	
	@Override
	public Map<String, Object> populateAutocompleteInclPrivileged(String query) {
		StringBuilder sb = new StringBuilder();
		Map<String,Object> similarItems = new HashMap<String,Object>();
		List<Autocomplete> suggestions = new ArrayList<Autocomplete>();
		sb.append(query);
		sb.append(Constants.PERCENTAGE);
		List<Customer> items = customerDAO.listCustomerInclPrivelegedLike(sb.toString());
		for (Customer customer : items) {
			Autocomplete suggestion = new Autocomplete();
			suggestion.setData(customer.getCustomerId());
			suggestion.setValue(customer.getName());
			suggestions.add(suggestion);
		}
		similarItems.put(Constants.QUERY, query);
		similarItems.put(Constants.SUGGESTIONS, suggestions);
		return similarItems;
	}

	@Override
	public Customer getCustomerById(String customerId) {
		return customerDAO.getCustomerById(customerId);
	}

	@Override
	public Map<String, Object> populateIdAutocomplete(String query) {
		StringBuilder sb = new StringBuilder();
		Map<String,Object> similarItems = new HashMap<String,Object>();
		List<Autocomplete> suggestions = new ArrayList<Autocomplete>();
		sb.append(query);
		sb.append(Constants.PERCENTAGE);
		List<String> items = customerDAO.listCustomerIdLike(sb.toString());
		for (String item : items) {
			Autocomplete suggestion = new Autocomplete();
			suggestion.setData(item);
			suggestion.setValue(item);
			suggestions.add(suggestion);
		}
		similarItems.put(Constants.QUERY, query);
		similarItems.put(Constants.SUGGESTIONS, suggestions);
		return similarItems;
	}
	
	@Override
	public Map<String, Object> populateIdAutocompleteInclPrivileged(String query) {
		StringBuilder sb = new StringBuilder();
		Map<String,Object> similarItems = new HashMap<String,Object>();
		List<Autocomplete> suggestions = new ArrayList<Autocomplete>();
		sb.append(query);
		sb.append(Constants.PERCENTAGE);
		List<String> items = customerDAO.listCustomerIdInclPrivelegedLike(sb.toString());
		for (String item : items) {
			Autocomplete suggestion = new Autocomplete();
			suggestion.setData(item);
			suggestion.setValue(item);
			suggestions.add(suggestion);
		}
		similarItems.put(Constants.QUERY, query);
		similarItems.put(Constants.SUGGESTIONS, suggestions);
		return similarItems;
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public boolean editCustomer(Customer customer) throws Exception {
		Customer oldData = customerDAO.getCustomerById(customer.getCustomerId());
		double change = customer.getInitialBalance() - oldData.getInitialBalance();
		
		if(change>0){
			customerDAO.increaseCurrentBalance(customer.getCustomerId(), change);
		}else if(change<0){
			customerDAO.decreaseCurrentBalance(customer.getCustomerId(), -change);
		}
		return customerDAO.editCustomer(customer);
	}

	@Override
	public boolean removeCustomer(String customerId) {
		return customerDAO.removeCustomer(customerId);
	}

	@Override
	public List<State> listState() {
		return customerDAO.listIndianState();
	}
	
}
