package com.curious365.ifa.dao.impl;

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
				QueryConstants.CREATE_INVOICE,
				new Object[] { invoice.getInvoiceId(),
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
	public Long getLastTaxInvoiceByMonth(Invoice invoice) {
		return jdbcTemplate.queryForObject(QueryConstants.GET_LAST_INVOICE_ID_BY_MONTH,Long.class,new Object[]{invoice.getInvoiceDate(),Constants.ACTIVE});
	}


	@Override
	public Long getCurrentTaxInvoiceSeq() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_TAX_INVOICE_CURR_SEQ, Long.class);
	}
	
	@Override
	public TaxInvoice getTaxInvoiceById(long taxInvoiceId) {
		TaxInvoice taxInvoice = null;
		try{
			taxInvoice = jdbcTemplate.queryForObject(QueryConstants.GET_TAX_INVOICE_BY_ID,  new BeanPropertyRowMapper<TaxInvoice>(TaxInvoice.class),new Object[]{taxInvoiceId,Constants.ACTIVE});
		}catch(Exception e){
			
		}
		return taxInvoice;
	}

	@Override
	public Long getLastTaxInvoice() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_LAST_INVOICE_ID,Long.class,new Object[]{Constants.ACTIVE});
	}
	
	
}
