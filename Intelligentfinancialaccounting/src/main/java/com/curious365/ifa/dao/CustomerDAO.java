package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Customer;

public interface CustomerDAO {
	public List<Customer> listCustomers(int isActive);
	public boolean checkCustomerNameAvailability(String customerName);
	public boolean addNewCustomer(Customer bean);
	public List<Customer> listCustomerLike(String query);
	public Customer getCustomerById(String customerId);
	public List<String> listCustomerIdLike(String query);
	public boolean editCustomer(Customer bean);
	public boolean removeCustomer(String customerId);
	public long getCurrentCustomerId();
}
