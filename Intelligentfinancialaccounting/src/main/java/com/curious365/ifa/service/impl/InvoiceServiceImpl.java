package com.curious365.ifa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.InvoiceType;
import com.curious365.ifa.dao.FaultDAO;
import com.curious365.ifa.dao.InvoiceDAO;
import com.curious365.ifa.dao.PurchaseDAO;
import com.curious365.ifa.dao.SalesDAO;
import com.curious365.ifa.dto.Fault;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.InvoiceAutocomplete;
import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService{
	
	@Autowired
	private InvoiceDAO invoiceDAO;
	@Autowired
	private SalesDAO salesDAO;
	@Autowired
	private PurchaseDAO purchaseDAO;
	@Autowired
	private FaultDAO faultDAO;

	@Override
	public Map<String, Object> populateAutocomplete(String query,String invoiceType) {
		StringBuilder sb = new StringBuilder();
		Map<String,Object> similarItems = new HashMap<String,Object>();
		List<InvoiceAutocomplete> suggestions = new ArrayList<InvoiceAutocomplete>();
		sb.append(query);
		sb.append(Constants.PERCENTAGE);
		List<Invoice> invoices = invoiceDAO.listInvoiceLike(sb.toString(),invoiceType);
		for (Invoice invoice : invoices) {
			InvoiceAutocomplete suggestion = new InvoiceAutocomplete();
			suggestion.setData(invoice);
			suggestion.setValue(""+invoice.getInvoiceId());
			suggestions.add(suggestion);
		}
		similarItems.put(Constants.QUERY, query);
		similarItems.put(Constants.SUGGESTIONS, suggestions);
		return similarItems;
	}

	@Override
	public Map<String, Object> populateAutocompleteInclPrivileged(String query,String invoiceType) {
		StringBuilder sb = new StringBuilder();
		Map<String,Object> similarItems = new HashMap<String,Object>();
		List<InvoiceAutocomplete> suggestions = new ArrayList<InvoiceAutocomplete>();
		sb.append(query);
		sb.append(Constants.PERCENTAGE);
		List<Invoice> invoices = invoiceDAO.listInvoiceInclPrivelegedLike(sb.toString(),invoiceType);
		for (Invoice invoice : invoices) {
			InvoiceAutocomplete suggestion = new InvoiceAutocomplete();
			suggestion.setData(invoice);
			suggestion.setValue(""+invoice.getInvoiceId());
			suggestions.add(suggestion);
		}
		similarItems.put(Constants.QUERY, query);
		similarItems.put(Constants.SUGGESTIONS, suggestions);
		return similarItems;
	}

	@Transactional(readOnly = false , rollbackFor = Exception.class)
	@Override
	public void edit(Invoice invoice) throws Exception {
		invoiceDAO.edit(invoice);
		
		if(InvoiceType.SALES.getValue().equalsIgnoreCase(invoice.getInvoiceType())){
			List<Sales> salesList = salesDAO.listSalesByInvoiceId(invoice.getInvoiceId());
			for (Sales sales : salesList) {
				Sales salesCopy = new Sales();
				BeanUtils.copyProperties(sales, salesCopy);
				salesCopy.setSalesCustomerId(invoice.getInvoiceCustomerId());
				salesCopy.setSalesDate(invoice.getInvoiceDate());
				salesDAO.edit(salesCopy);
			}
		}else if(InvoiceType.PURCHASE.getValue().equalsIgnoreCase(invoice.getInvoiceType())){
			List<Purchase> purchaseList = purchaseDAO.listPurchaseByInvoiceId(invoice.getInvoiceId());
			for (Purchase purchase : purchaseList) {
				Purchase purchaseCopy = new Purchase();
				BeanUtils.copyProperties(purchase, purchaseCopy);
				purchaseCopy.setPurchaseCustomerId(invoice.getInvoiceCustomerId());
				purchaseCopy.setPurchaseDate(invoice.getInvoiceDate());
				purchaseDAO.edit(purchaseCopy);
			}
		}else if(InvoiceType.FAULT.getValue().equalsIgnoreCase(invoice.getInvoiceType())){
			List<Fault> faultList = faultDAO.listFaultByInvoiceId(invoice.getInvoiceId());
			for (Fault fault : faultList) {
				Fault faultCopy = new Fault();
				BeanUtils.copyProperties(fault, faultCopy);
				faultCopy.setFaultCustomerId(invoice.getInvoiceCustomerId());
				faultCopy.setFaultDate(invoice.getInvoiceDate());
				faultDAO.edit(faultCopy);
			}
		}
	}

	@Override
	public Invoice getInvoiceWtDetails(long invoiceId) throws Exception {
		Invoice invoice = invoiceDAO.getRecordById(invoiceId);
		
		if(InvoiceType.SALES.getValue().equalsIgnoreCase(invoice.getInvoiceType())){
			List<Sales> salesList = salesDAO.listSalesByInvoiceId(invoiceId);
			invoice.setRecords(salesList);
		}else if(InvoiceType.PURCHASE.getValue().equalsIgnoreCase(invoice.getInvoiceType())){
			List<Purchase> purchaseList = purchaseDAO.listPurchaseByInvoiceId(invoiceId);
			invoice.setRecords(purchaseList);
		}else if(InvoiceType.FAULT.getValue().equalsIgnoreCase(invoice.getInvoiceType())){
			List<Fault> faultList = faultDAO.listFaultByInvoiceId(invoiceId);
			invoice.setRecords(faultList);
		}
		
		return invoice;
	}

}
