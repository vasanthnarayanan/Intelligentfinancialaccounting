package com.curious365.ifa.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curious365.ifa.dao.AuditedSalesDAO;
import com.curious365.ifa.dao.SalesDAO;
import com.curious365.ifa.dao.TaxInvoiceDAO;
import com.curious365.ifa.dto.AuditedSales;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.dto.TaxInvoice;
import com.curious365.ifa.service.TaxInvoiceService;

@Service
public class TaxInvoiceServiceImpl implements TaxInvoiceService {
	
	@Autowired
	private TaxInvoiceDAO taxInvoiceDAO;
	@Autowired
	private AuditedSalesDAO auditedSalesDAO;
	@Autowired
	private SalesDAO salesDAO;

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void createInstantTaxInvoice(Invoice invoice) throws ParseException {
		int invoicesPerDay = 10;
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MMM/yyyy");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatter.parse(invoice.getInvoiceDate()));
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		// check last used invoice id for the day of month
		Long lastInvoiceId = taxInvoiceDAO.getLastTaxInvoiceByMonth(invoice);
		long currentInvoiceId = 0;
		if(null==lastInvoiceId){
			currentInvoiceId = 0;
		}else{
			currentInvoiceId = lastInvoiceId;
		}
		long newInvoiceId = 0;
		if(currentInvoiceId>-1){
			newInvoiceId = currentInvoiceId+1;
		}else{
			// for this to work as expected last month invoices should be generated
			lastInvoiceId = taxInvoiceDAO.getLastTaxInvoice();
			if(null==lastInvoiceId){
				currentInvoiceId = 0;
			}else{
				currentInvoiceId = lastInvoiceId;
			}
			newInvoiceId = currentInvoiceId+((dayOfMonth-1)*invoicesPerDay)+1;
		}
		
		// loop until finding a suitable invoice number
		do{
			TaxInvoice taxInvoice = taxInvoiceDAO.getTaxInvoiceById(newInvoiceId);
			if(null!=taxInvoice && taxInvoice.getTaxInvoiceId()==newInvoiceId){
				newInvoiceId++;
			}else{
				break;	
			}
		}while(true);
		
		invoice.setInvoiceId(newInvoiceId);
		
		List<Sales> records = salesDAO.listSalesByInvoiceId(invoice.getInvoiceId());
		
		for (Sales sales : records) {
			AuditedSales auditedSales = new AuditedSales();
			BeanUtils.copyProperties(sales, auditedSales);
			auditedSales.setTaxInvoiceId(newInvoiceId);
			auditedSalesDAO.create(auditedSales);
		}
		
		taxInvoiceDAO.instantCreate(invoice);
		
	}

	@Override
	public void generateTaxInvoiceForMonth(Calendar cal,List<Purchase> purchases) {
		DateFormat month = new SimpleDateFormat("MMM"); // month, like JUN digits
		DateFormat year = new SimpleDateFormat("yy"); // year, with 2 digits
		
		StringBuffer sb = new StringBuffer("%-");
		sb.append(month.format(cal.getTime()));
		sb.append("-");
		sb.append(year.format(cal.getTime()));
		
		
		List<AuditedSales> finalAuditedSalesList = new ArrayList<AuditedSales>();
		List<AuditedSales> auditedSalesList = auditedSalesDAO.listAuditedSalesByMonthOfYear(sb.toString());
		List<Sales> salesList = salesDAO.listSalesByMonthOfYear(sb.toString());
		
		Collections.sort(auditedSalesList);
		Collections.sort(salesList);
		
		Map<String,Long> bagsByCategory = new HashMap<String,Long>();
		for (Purchase purchase : purchases) {
			StringBuffer key = new StringBuffer();
			key.append(purchase.getPurchaseItemQuantity());
			key.append(purchase.getPurchaseItemType());
			long purchasepieces = 0;
			if(bagsByCategory.containsKey(key.toString())){
				purchasepieces = bagsByCategory.get(key.toString());
			}
			
			bagsByCategory.put(key.toString(), purchasepieces+purchase.getPurchasePieces());
			
		}
		
		// audited sales are deducted upfront
		for (AuditedSales auditedSales : auditedSalesList) {
			StringBuffer key = new StringBuffer();
			key.append(auditedSales.getSalesItemQuantity());
			key.append(auditedSales.getSalesItemType());
			
			if(bagsByCategory.containsKey(key)){
				long bags = bagsByCategory.get(key)-auditedSales.getSalesPieces();
				bagsByCategory.put(key.toString(),bags);	
			}
			
		}
		
		int numdays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Map<String,Long> bagsByCatPerDay = new HashMap<String,Long>();
		Map<String,Long> bagsByCatPerDayCopy = new HashMap<String,Long>();
		for (Map.Entry<String, Long> entry : bagsByCategory.entrySet())
		{
			bagsByCatPerDay.put(entry.getKey(),entry.getValue()/numdays);
		}
		// minimum 10 invoices should be generated per day
		boolean dateChanged = false;
		String salesDate = null;
		for (Sales sales : salesList) {
			if(null == salesDate){
				bagsByCatPerDayCopy = bagsByCatPerDay;
				salesDate = sales.getSalesDate();
			}
			if(null != salesDate && !salesDate.equalsIgnoreCase(sales.getSalesDate())){
				salesDate = sales.getSalesDate();
				dateChanged = true;
				bagsByCatPerDayCopy = bagsByCatPerDay;
			}else{
				dateChanged = false;
			}
			AuditedSales auditedSales = new AuditedSales();
			auditedSales.setSalesRecordId(sales.getSalesRecordId());
			StringBuffer bagKey = new StringBuffer();
			bagKey.append(sales.getSalesItemQuantity());
			bagKey.append(sales.getSalesItemType());
			long bags = bagsByCatPerDayCopy.get(bagKey.toString());
			if(bags>0){
				if(auditedSalesList.contains(auditedSales)){
						// skip new tax invoice sequence
				}else{
					// create new tax invoice sequence
				}
			}
			
		}
	}

}
