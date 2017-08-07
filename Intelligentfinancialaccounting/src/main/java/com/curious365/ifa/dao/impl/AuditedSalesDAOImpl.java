package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.AuditedSalesDAO;
import com.curious365.ifa.dto.AuditedSales;

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
	
}
