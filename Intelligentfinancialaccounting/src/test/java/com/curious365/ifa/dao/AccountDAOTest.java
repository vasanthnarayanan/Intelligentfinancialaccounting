package com.curious365.ifa.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.curious365.ifa.IntelligentFinancialAccountingApplicationTests;

public class AccountDAOTest extends IntelligentFinancialAccountingApplicationTests {
	
	@Autowired
	private AccountingDAO accountingDAO;
	
	@Before
	public void setUp(){
		
	}
	
	@After
	public void tearDown(){
	}
	
	@Test
	public void testActiveSalesRowCount() throws Exception{
		Long salesCount = accountingDAO.getActiveSalesCount();
		Assert.assertNotNull(salesCount);
		Assert.assertEquals(6, (long) salesCount);
	}
	
}
