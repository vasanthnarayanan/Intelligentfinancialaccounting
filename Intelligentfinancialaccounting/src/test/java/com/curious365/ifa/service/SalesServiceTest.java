package com.curious365.ifa.service;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.curious365.ifa.IntelligentFinancialAccountingApplicationTests;
import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.dto.Item;
import com.curious365.ifa.dto.ItemType;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.exceptions.NoStockInHand;

public class SalesServiceTest extends IntelligentFinancialAccountingApplicationTests{
	@Autowired
	SalesService salesService;
	@Autowired
	CustomerService customerService;
	@Autowired
	ItemService itemService;
	
	private long customerId;
	private long itemTypeId;
	private long itemQuantityId;
	private long itemId;
	
	@Before
	public void setUp(){
		Customer customer = new Customer();
		customer.setName("Vasanth");
		customer.setInitialBalance(0);
		customer.setCustomerAddress("Redhills");
		customer.setCustomerPhoneNumber("9789026689");
		customerId = customerService.addNewCustomer(customer);
		ItemType itemType= new ItemType(); 
		itemType.setTaxRate(15);
		itemType.setType("Non Woven");
		itemTypeId = itemService.addNewItemType(itemType);
		itemQuantityId = itemService.addNewItemQuantity(25);
		Item item = new Item();
		item.setItemName("Krishna");
		item.setQuantityId(itemQuantityId);
		item.setTypeId(itemTypeId);
		item.setCost(19);
		item.setStock(3000);
		itemId = itemService.addNewItem(item);
	}
	
	@After
	public void tearDown(){
		/*itemService.removeItem(""+itemId);
		itemService.removeItemQuantityById(""+itemQuantityId);
		itemService.removeItemTypeById(""+itemTypeId);
		
		customerService.removeCustomer(""+customerId);*/
	}

	@Test
	public void testCreateSales() throws NoStockInHand, Exception{
		Sales record = new Sales();
		record.setSalesDate(new Date().toString());
		record.setSalesCustomerId(customerId);
		record.setSalesItemId(itemId);
		record.setSalesPieces(1000);
		record.setSalesCost(18.6);
		record.setSalesRemarks("Bike");
		
		salesService.create(record);
	}
	
}
