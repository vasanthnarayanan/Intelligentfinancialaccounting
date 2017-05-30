package com.curious365.ifa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.CustomerDAO;
import com.curious365.ifa.dto.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	@Override
	public List<Customer> listCustomers(int isActive){
		List<Customer> customers = new ArrayList<Customer>();
		try{
			customers = jdbcTemplate.query(QueryConstants.LIST_CUSTOMERS, new BeanPropertyRowMapper<Customer>(Customer.class), new Object[]{isActive}); 
		}catch(Exception e){
			
		}
		return customers;
	}


	@Override
	public boolean checkCustomerNameAvailability(String customerName) {
		Long count = jdbcTemplate.queryForObject(QueryConstants.CHECK_CUSTOMERNAME_AVAILABILITY,Long.class,new Object[]{Constants.ACTIVE,customerName});
		if(null!=count){
			if(count>0){
				return false;
			}else{
				return true;
			}
		}
		return true;
	}


	@Override
	public boolean addNewCustomer(Customer bean) {
		int flag = jdbcTemplate.update(QueryConstants.INSERT_CUSTOMER, new Object[]{bean.getName(),Constants.ACTIVE,bean.getInitialBalance(),bean.getCustomerAddress(),bean.getCustomerPhoneNumber()});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public List<Customer> listCustomerLike(String query) {
		List<Customer> customers = new ArrayList<Customer>();
		try{
			customers = jdbcTemplate.query(QueryConstants.LIST_CUSTOMERS_LIKE, new BeanPropertyRowMapper<Customer>(Customer.class),new Object[]{Constants.ACTIVE,query});			
		}catch(Exception e){
			
		}
		return customers;
	}


	@Override
	public Customer getCustomerById(String customerId) {
		Customer customer = null;
		try{
			customer = jdbcTemplate.queryForObject(QueryConstants.GET_CUSTOMER_BY_ID,  new BeanPropertyRowMapper<Customer>(Customer.class),new Object[]{Constants.ACTIVE,customerId});
		}catch(Exception e){
			
		}
		return customer;
	}
	
	@Override
	public List<String> listCustomerIdLike(String query) {
		List<String> customers = new ArrayList<String>();
		try{
			customers = jdbcTemplate.queryForList(QueryConstants.LIST_CUSTOMER_IDS_LIKE, String.class,new Object[]{Constants.ACTIVE,query});			
		}catch(Exception e){
			
		}
		return customers;
	}
	
	@Override
	public boolean editCustomer(Customer bean) {
		int flag = jdbcTemplate.update(QueryConstants.UPDATE_CUSTOMER, new Object[]{bean.getName(),bean.getInitialBalance(),bean.getCustomerAddress(),bean.getCustomerPhoneNumber(),bean.getCustomerId()});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean removeCustomer(String customerId) {
		int flag = jdbcTemplate.update(QueryConstants.SOFT_DELETE_CUSTOMER, new Object[]{Constants.INACTIVE,customerId});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public long getCurrentCustomerId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_CUSTOMER_CURR_SEQ, Long.class);
	}


}
