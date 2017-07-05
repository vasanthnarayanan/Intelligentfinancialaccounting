package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.InvoiceOrderDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.InvoiceOrder;

@Repository
public class InvoiceOrderDAOImpl implements InvoiceOrderDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean create(InvoiceOrder invoiceorder) {
		int flag = jdbcTemplate.update(
				QueryConstants.CREATE_INVOICE_ORDER,
				new Object[] { invoiceorder.getOrderDate(),
						invoiceorder.getOrderType(),
						invoiceorder.getOrderMode(),
						invoiceorder.getOrderStatus(),
						invoiceorder.getRemarks(),
						Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean edit(InvoiceOrder invoiceorder) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_INVOICE_ORDER,
				new Object[] { invoiceorder.getOrderDate(),
						invoiceorder.getOrderType(),
						invoiceorder.getOrderMode(),
						invoiceorder.getOrderStatus(),
						invoiceorder.getRemarks(),
						invoiceorder.getOrderId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean softDelete(long invoiceorderId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_INVOICE_ORDER,
				new Object[] { Constants.INACTIVE, invoiceorderId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public InvoiceOrder getRecordById(long invoiceorderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Content> listAllInvoiceOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCurrentInvoiceOrderId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_INVOICE_ORDER_CURR_SEQ, Long.class);
	}
}
