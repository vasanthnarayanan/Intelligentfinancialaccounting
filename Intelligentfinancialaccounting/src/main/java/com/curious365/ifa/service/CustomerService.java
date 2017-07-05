package com.curious365.ifa.service;

import java.util.List;
import java.util.Map;

import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.dto.State;

public interface CustomerService {
	public List<State> listState();
	public List<Customer> listActiveCustomers();
	public List<Customer> listActiveCustomersInclPrivileged();
	public String checkCustomerNameAvailability(String customerName);
	public long addNewCustomer(Customer customer);
	public Map<String,Object> populateAutocomplete(String query);
	public Map<String,Object> populateAutocompleteInclPrivileged(String query);
	public Customer getCustomerById(String customerId);
	public Map<String,Object> populateIdAutocomplete(String query);
	public Map<String,Object> populateIdAutocompleteInclPrivileged(String query);
	public boolean editCustomer(Customer customer) throws Exception;
	public boolean removeCustomer(String customerId);
}
