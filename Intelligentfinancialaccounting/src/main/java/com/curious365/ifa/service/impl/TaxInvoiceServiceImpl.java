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
import com.curious365.ifa.dao.InvoiceDAO;
import com.curious365.ifa.dao.SalesDAO;
import com.curious365.ifa.dao.TaxInvoiceDAO;
import com.curious365.ifa.dto.AuditedSales;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.dto.TaxInvoice;
import com.curious365.ifa.exceptions.InvoiceLimitExceeded;
import com.curious365.ifa.service.TaxInvoiceService;

@Service
public class TaxInvoiceServiceImpl implements TaxInvoiceService {
	
	@Autowired
	private TaxInvoiceDAO taxInvoiceDAO;
	@Autowired
	private InvoiceDAO invoiceDAO;
	@Autowired
	private AuditedSalesDAO auditedSalesDAO;
	@Autowired
	private SalesDAO salesDAO;

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public long createInstantTaxInvoice(Invoice invoice) throws ParseException,InvoiceLimitExceeded {
		int invoicesPerDay = 10;
		
		// if tax invoice already present for the estimate, skip estimate
		if(null != invoice && invoice.getTaxInvoiceId()>0){
			return invoice.getTaxInvoiceId();
		}
		
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat newFormat= 
				new SimpleDateFormat("dd/MMM/yyyy");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatter.parse(invoice.getInvoiceDate()));
		// converting to database 
		String invoiceDate = newFormat.format(cal.getTime());
		invoice.setInvoiceDate(invoiceDate);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		// check last used invoice id for the day of month
		Long countInvoice = taxInvoiceDAO.countInvoiceByMonth(invoice);
		if(countInvoice>invoicesPerDay){
			throw new InvoiceLimitExceeded("Invoice Limit Exceeded for the day!");
		}
		Long lastInvoiceId = taxInvoiceDAO.getLastTaxInvoiceByMonth(invoice);
		
		long currentInvoiceId = 0;
		if(null==lastInvoiceId){
			currentInvoiceId = -1;
		}else{
			currentInvoiceId = lastInvoiceId;
		}
		long newInvoiceId = 0;
		if(currentInvoiceId>-1){
			// if same day invoice are present increment invoice by 1
				newInvoiceId = currentInvoiceId+1;				
		}else{
			newInvoiceId = ((dayOfMonth-1)*invoicesPerDay)+1;
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
		
		List<Sales> records = salesDAO.listSalesByInvoiceId(invoice.getInvoiceId());
		
		for (Sales sales : records) {
			AuditedSales auditedSales = new AuditedSales();
			BeanUtils.copyProperties(sales, auditedSales);
			auditedSales.setTaxInvoiceId(newInvoiceId);
			Calendar salescal = Calendar.getInstance();
			salescal.setTime(formatter.parse(sales.getSalesDate()));
			auditedSales.setSalesDate(newFormat.format(salescal.getTime()));
			auditedSalesDAO.create(auditedSales);
		}
		
		invoice.setTaxInvoiceId(newInvoiceId);
		taxInvoiceDAO.instantCreate(invoice);
		invoiceDAO.updateInvoiceWtTaxInvoice(invoice);
		return newInvoiceId;
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

	@Override
	public List<TaxInvoice> listTaxInvoicesInclPrivilegedForMonth(
			String monthOfYear) {
		StringBuffer sb = new StringBuffer("%-");
		sb.append(monthOfYear);
		return taxInvoiceDAO.listInvoiceInclPrivelegedByMonth(sb.toString());
	}

	@Override
	public List<TaxInvoice> listTaxInvoicesForMonth(String monthOfYear) {
		StringBuffer sb = new StringBuffer("%-");
		sb.append(monthOfYear);
		return taxInvoiceDAO.listInvoiceByMonth(sb.toString());
	}

	@Override
	public TaxInvoice getTaxInvoiceWtDetails(long taxInvoiceId)
			throws Exception {
		TaxInvoice taxInvoice = taxInvoiceDAO.getTaxInvoiceById(taxInvoiceId);
		List<AuditedSales> auditedSalesList = auditedSalesDAO.listAuditedSalesByTaxInvoiceId(taxInvoiceId);
		taxInvoice.setRecords(auditedSalesList);
		return taxInvoice;
	}

}
