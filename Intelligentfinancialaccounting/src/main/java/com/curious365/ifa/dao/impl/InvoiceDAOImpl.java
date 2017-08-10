package com.curious365.ifa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.InvoiceType;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.InvoiceDAO;
import com.curious365.ifa.dto.Invoice;

@Repository
public class InvoiceDAOImpl implements InvoiceDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean create(Invoice invoice) {
		int flag = jdbcTemplate.update(
				QueryConstants.CREATE_INVOICE,
				new Object[] { invoice.getInvoiceDate(),
						invoice.getInvoiceType(),
						invoice.getInvoiceCustomerId(),
						invoice.getInvoiceTransportId(),
						invoice.getOrderId(),
						invoice.getRemarks(),
						invoice.getTaxInvoiceId(),
						Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean edit(Invoice invoice) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_INVOICE,
				new Object[] { invoice.getInvoiceDate(),
						invoice.getInvoiceType(),
						invoice.getInvoiceCustomerId(),
						invoice.getInvoiceTransportId(),
						invoice.getOrderId(),
						invoice.getRemarks(),
						invoice.getInvoiceId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean softDelete(long invoiceId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_INVOICE,
				new Object[] { Constants.INACTIVE, invoiceId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public Invoice getRecordById(long invoiceId) {
		Invoice invoice = null;
		try{
			invoice = jdbcTemplate.queryForObject(QueryConstants.GET_INVOICE_BY_ID,  new BeanPropertyRowMapper<Invoice>(Invoice.class),new Object[]{Constants.ACTIVE,invoiceId});
		}catch(Exception e){
			
		}
		return invoice;
	}

	@Override
	public long getCurrentInvoiceId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_INVOICE_CURR_SEQ, Long.class);
	}

	@Override
	public List<Invoice> listInvoiceLike(String query,String invoiceType) {
		List<Invoice> invoices = new ArrayList<Invoice>();
		try{
			invoices = jdbcTemplate.query(QueryConstants.LIST_INVOICE_LIKE, new BeanPropertyRowMapper<Invoice>(Invoice.class),new Object[]{Constants.ACTIVE,invoiceType,query});			
		}catch(Exception e){
			
		}
		return invoices;
	}

	@Override
	public List<Invoice> listInvoiceInclPrivelegedLike(String query,String invoiceType) {
		List<Invoice> invoices = new ArrayList<Invoice>();
		try{
			if(InvoiceType.SALES.getValue().equalsIgnoreCase(invoiceType)){
				invoices = jdbcTemplate.query(QueryConstants.LIST_INVOICE_INCL_PRIV_LIKE, new BeanPropertyRowMapper<Invoice>(Invoice.class),new Object[]{Constants.ACTIVE,InvoiceType.SALES.getValue(),query});
			}else if(InvoiceType.PURCHASE.getValue().equalsIgnoreCase(invoiceType)){
				invoices = jdbcTemplate.query(QueryConstants.LIST_INVOICE_INCL_PRIV_LIKE, new BeanPropertyRowMapper<Invoice>(Invoice.class),new Object[]{Constants.ACTIVE,InvoiceType.PURCHASE.getValue(),query});
			}else if(InvoiceType.FAULT.getValue().equalsIgnoreCase(invoiceType)){
				invoices = jdbcTemplate.query(QueryConstants.LIST_INVOICE_INCL_PRIV_LIKE, new BeanPropertyRowMapper<Invoice>(Invoice.class),new Object[]{Constants.ACTIVE,InvoiceType.FAULT.getValue(),query});
			}
						
		}catch(Exception e){
			
		}
		return invoices;
	}

	@Override
	public List<Invoice> listInvoiceByMonth(String monthOfYear) {
		List<Invoice> invoices = new ArrayList<Invoice>();
		try{
			invoices = jdbcTemplate.query(QueryConstants.LIST_INVOICE_BY_MONTH, new BeanPropertyRowMapper<Invoice>(Invoice.class),new Object[]{Constants.ACTIVE,monthOfYear});			
		}catch(Exception e){
			
		}
		return invoices;
	}

	@Override
	public List<Invoice> listInvoiceInclPrivelegedByMonth(String monthOfYear) {
		List<Invoice> invoices = new ArrayList<Invoice>();
		try{
			invoices = jdbcTemplate.query(QueryConstants.LIST_INVOICE_INCL_PRIV_BY_MONTH, new BeanPropertyRowMapper<Invoice>(Invoice.class),new Object[]{Constants.ACTIVE,monthOfYear});			
		}catch(Exception e){
			
		}
		return invoices;
	}

	@Override
	public boolean updateInvoiceWtTaxInvoice(Invoice invoice) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_INVOICE_WT_TAX_INVOICE,
				new Object[] {
						invoice.getTaxInvoiceId(),
						invoice.getInvoiceId()
						});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}
}
