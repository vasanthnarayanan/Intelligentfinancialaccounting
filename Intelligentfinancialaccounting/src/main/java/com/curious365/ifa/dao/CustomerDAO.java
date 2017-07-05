package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.dto.State;

public interface CustomerDAO {
	public List<Customer> listCustomers(int isActive);
	public List<Customer> listCustomersInclPriveleged(int isActive);
	public List<State> listIndianState();
	public boolean checkCustomerNameAvailability(String customerName);
	public boolean addNewCustomer(Customer bean);
	public List<Customer> listCustomerLike(String query);
	public List<Customer> listCustomerInclPrivelegedLike(String query);
	public Customer getCustomerById(String customerId);
	public List<String> listCustomerIdLike(String query);
	public List<String> listCustomerIdInclPrivelegedLike(String query);
	public boolean increaseCurrentBalance(String customerId,double value);
	public boolean decreaseCurrentBalance(String customerId,double value);
	public boolean editCustomer(Customer bean);
	public boolean removeCustomer(String customerId);
	public long getCurrentCustomerId();
}
