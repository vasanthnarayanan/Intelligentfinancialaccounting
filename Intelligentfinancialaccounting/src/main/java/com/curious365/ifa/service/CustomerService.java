package com.curious365.ifa.service;

import java.util.List;
import java.util.Map;

import com.curious365.ifa.dto.Customer;

public interface CustomerService {
	public List<Customer> listActiveCustomers();
	public String checkCustomerNameAvailability(String customerName);
	public boolean addNewCustomer(Customer customer);
	public Map<String,Object> populateAutocomplete(String query);
	public Customer getCustomerById(String customerId);
	public Map<String,Object> populateIdAutocomplete(String query);
	public boolean editCustomer(Customer customer);
	public boolean removeCustomer(String customerId);
}
