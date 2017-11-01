package com.curious365.ifa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.TaxInvoiceDAO;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.TaxInvoice;

@Repository
public class TaxInvoiceDAOImpl implements TaxInvoiceDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public boolean instantCreate(Invoice invoice) {
		int flag = jdbcTemplate.update(
				QueryConstants.CREATE_INSTANT_TAX_INVOICE,
				new Object[] { invoice.getTaxInvoiceId(),
						invoice.getInvoiceDate(),
						invoice.getInvoiceType(),
						invoice.getInvoiceCustomerId(),
						invoice.getInvoiceTransportId(),
						invoice.getOrderId(),
						invoice.getRemarks(),
						Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Long getLastTaxInvoiceByDate(Invoice invoice) {
		return jdbcTemplate.queryForObject(QueryConstants.GET_LAST_INVOICE_ID_BY_DATE,Long.class,new Object[]{invoice.getInvoiceDate(),Constants.ACTIVE});
	}
	
	@Override
	public Long getFirstTaxInvoiceByDate(Invoice invoice) {
		return jdbcTemplate.queryForObject(QueryConstants.GET_FIRST_INVOICE_ID_BY_DATE,Long.class,new Object[]{invoice.getInvoiceDate(),Constants.ACTIVE});
	}


	@Override
	public Long getCurrentTaxInvoiceSeq() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_TAX_INVOICE_CURR_SEQ, Long.class);
	}
	
	@Override
	public TaxInvoice getTaxInvoiceById(long taxInvoiceId) {
		TaxInvoice taxInvoice = null;
		try{
			taxInvoice = jdbcTemplate.queryForObject(QueryConstants.GET_TAX_INVOICE_BY_ID,  new BeanPropertyRowMapper<TaxInvoice>(TaxInvoice.class),new Object[]{Constants.ACTIVE,taxInvoiceId});
		}catch(Exception e){
			
		}
		return taxInvoice;
	}

	@Override
	public Long getLastTaxInvoice() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_LAST_INVOICE_ID,Long.class,new Object[]{Constants.ACTIVE});
	}


	@Override
	public List<TaxInvoice> listInvoiceByMonth(String monthOfYear) {
		List<TaxInvoice> invoices = new ArrayList<TaxInvoice>();
		try{
			invoices = jdbcTemplate.query(QueryConstants.LIST_TAX_INVOICE_BY_MONTH, new BeanPropertyRowMapper<TaxInvoice>(TaxInvoice.class),new Object[]{Constants.ACTIVE,monthOfYear});			
		}catch(Exception e){
			
		}
		return invoices;
	}


	@Override
	public List<TaxInvoice> listInvoiceInclPrivelegedByMonth(String monthOfYear) {
		List<TaxInvoice> invoices = new ArrayList<TaxInvoice>();
		try{
			invoices = jdbcTemplate.query(QueryConstants.LIST_TAX_INVOICE_INCL_PRIV_BY_MONTH, new BeanPropertyRowMapper<TaxInvoice>(TaxInvoice.class),new Object[]{Constants.ACTIVE,monthOfYear});			
		}catch(Exception e){
			
		}
		return invoices;
	}


	@Override
	public Long countInvoiceByDate(Invoice invoice) {
		return jdbcTemplate.queryForObject(QueryConstants.COUNT_TAX_INVOICE_BY_DATE,Long.class,new Object[]{invoice.getInvoiceDate(),Constants.ACTIVE});
	}


	@Override
	public boolean edit(Invoice invoice) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_TAX_INVOICE,
				new Object[] { invoice.getInvoiceDate(),
						invoice.getInvoiceType(),
						invoice.getInvoiceCustomerId(),
						invoice.getInvoiceTransportId(),
						invoice.getOrderId(),
						invoice.getRemarks(),
						invoice.getTaxInvoiceId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean softDelete(long invoiceId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_TAX_INVOICE,
				new Object[] { Constants.INACTIVE, invoiceId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}
	
	
}
