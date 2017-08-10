package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.AuditedSalesDAO;
import com.curious365.ifa.dto.AuditedSales;
import com.curious365.ifa.dto.Sales;

@Repository
public class AuditedSalesDAOImpl implements AuditedSalesDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	@Override
	public boolean create(AuditedSales record) {
		int flag = jdbcTemplate.update(
				QueryConstants.ADD_AUDITED_SALES,
				new Object[] { record.getSalesRecordId(),record.getSalesDate(),record.getSalesCustomerId(),
						record.getSalesItemId(), record.getSalesPieces(),
						record.getSalesCost(),record.getSalesRemarks(),
						record.getSalesTax(),record.getSalesTaxRate(),
						record.getInvoiceId(),record.getTaxInvoiceId(),Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public List<AuditedSales> listAuditedSalesByMonthOfYear(String monthOfYear) {
		List<AuditedSales> auditedSalesList = null;
		try{
			auditedSalesList = jdbcTemplate.query(QueryConstants.LIST_AUDITED_SALES_BY_MONTH_YEAR, new BeanPropertyRowMapper<AuditedSales>(AuditedSales.class),new Object[]{Constants.ACTIVE,monthOfYear});
		}catch(Exception e){
			e.printStackTrace();
		}
		return auditedSalesList;
	}


	@Override
	public List<AuditedSales> listAuditedSalesByTaxInvoiceId(long taxInvoiceId) {
		List<AuditedSales> auditedSalesList = null;
		try{
			auditedSalesList = jdbcTemplate.query(QueryConstants.LIST_AUDITED_SALES_BY_TAX_INVOICE_ID, new BeanPropertyRowMapper<AuditedSales>(AuditedSales.class),new Object[]{Constants.ACTIVE,taxInvoiceId});
		}catch(Exception e){
			e.printStackTrace();
		}
		return auditedSalesList;
	}


	@Override
	public boolean edit(AuditedSales record) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_AUDITED_SALES,
				new Object[] { record.getSalesDate(),record.getSalesCustomerId(),
						record.getSalesItemId(), record.getSalesPieces(),
						record.getSalesCost(),record.getSalesRemarks(),
						record.getSalesTax(),record.getSalesTaxRate(),record.getAuditedSalesId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public List<AuditedSales> listAuditedSalesByMonth(String monthOfYear) {
		List<AuditedSales> auditedSalesList = null;
		try{
			auditedSalesList = jdbcTemplate.query(QueryConstants.LIST_AUDITED_SALES_BY_MONTH, new BeanPropertyRowMapper<AuditedSales>(AuditedSales.class),new Object[]{Constants.ACTIVE,monthOfYear});
		}catch(Exception e){
			e.printStackTrace();
		}
		return auditedSalesList;
	}


	@Override
	public List<AuditedSales> listAuditedSalesInclPrivelegedByMonth(
			String monthOfYear) {
		List<AuditedSales> auditedSalesList = null;
		try{
			auditedSalesList = jdbcTemplate.query(QueryConstants.LIST_AUDITED_SALES_INCL_PRIV_BY_MONTH, new BeanPropertyRowMapper<AuditedSales>(AuditedSales.class),new Object[]{Constants.ACTIVE,monthOfYear});
		}catch(Exception e){
			e.printStackTrace();
		}
		return auditedSalesList;
	}


	@Override
	public AuditedSales getRecordById(long recordid) {
		AuditedSales auditedSales = null;
		try{
			auditedSales = jdbcTemplate.queryForObject(QueryConstants.GET_AUDITED_SALES,  new BeanPropertyRowMapper<AuditedSales>(AuditedSales.class),new Object[]{recordid,Constants.ACTIVE});
		}catch(Exception e){
			
		}
		return auditedSales;
	}
	
}
