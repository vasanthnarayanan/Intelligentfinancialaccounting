package com.curious365.ifa.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.curious365.ifa.IntelligentFinancialAccountingApplicationTests;
import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.dto.Transaction;

public class AccountDAOTest extends IntelligentFinancialAccountingApplicationTests {
	
	@Autowired
	private AccountingDAO accountingDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private ItemDAO itemDAO;
	@Autowired
	private SalesDAO salesDAO;
	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public ItemDAO getItemDAO() {
		return itemDAO;
	}

	public void setItemDAO(ItemDAO itemDAO) {
		this.itemDAO = itemDAO;
	}

	public SalesDAO getSalesDAO() {
		return salesDAO;
	}

	public void setSalesDAO(SalesDAO salesDAO) {
		this.salesDAO = salesDAO;
	}

	public PurchaseDAO getPurchaseDAO() {
		return purchaseDAO;
	}

	public void setPurchaseDAO(PurchaseDAO purchaseDAO) {
		this.purchaseDAO = purchaseDAO;
	}

	public FaultDAO getFaultDAO() {
		return faultDAO;
	}

	public void setFaultDAO(FaultDAO faultDAO) {
		this.faultDAO = faultDAO;
	}

	@Autowired
	private PurchaseDAO purchaseDAO;
	@Autowired
	private FaultDAO faultDAO;
	
	@Before
	public void setUp(){
		/**
		 *  Setup Dummy customer with valid customer name and initial balance
		 */
		Customer customer = new Customer();
		customer.setName("temp");
		customer.setInitialBalance(1000);
		customerDAO.addNewCustomer(customer);
		
		/**
		 * Setup Dummy Item
		 */
	}
	
	@After
	public void tearDown(){
		/**
		 * Destroy dummy customer
		 */
		
	}
	
	@Test
	public void testGetTransaction(){
		Transaction record = accountingDAO.getTransactionById(1044);
		Assert.assertNotNull("Transaction cannot be null", record);
		log.info(record);
	}
	
	public AccountingDAO getAccountingDAO() {
		return accountingDAO;
	}

	public void setAccountingDAO(AccountingDAO accountingDAO) {
		this.accountingDAO = accountingDAO;
	}

}
