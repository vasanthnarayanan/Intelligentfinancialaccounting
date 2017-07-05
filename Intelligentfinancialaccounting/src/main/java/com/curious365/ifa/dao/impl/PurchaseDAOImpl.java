package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.RowMapper.PurchaseRowMapper;
import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.PurchaseDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Purchase;

@Repository
public class PurchaseDAOImpl implements PurchaseDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public boolean create(Purchase record) {
		int flag = jdbcTemplate.update(
				QueryConstants.ADD_PURCHASE,
				new Object[] { record.getPurchaseDate(),record.getPurchaseCustomerId(),
						record.getPurchaseItemId(), record.getPurchasePieces(),
						record.getPurchaseCost(),record.getPurchaseRemarks(),
						record.getPurchaseTax(),record.getPurchaseTaxRate(),
						record.getInvoiceId(),Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean edit(Purchase record) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_PURCHASE,
				new Object[] { record.getPurchaseDate(),record.getPurchaseCustomerId(),
						record.getPurchaseItemId(), record.getPurchasePieces(),
						record.getPurchaseCost(),record.getPurchaseRemarks(),
						record.getPurchaseTax(),record.getPurchaseTaxRate(),
						record.getInvoiceId(),record.getPurchaseRecordId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean softDelete(long purchaseId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_PURCHASE,
				new Object[] { Constants.INACTIVE, purchaseId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Purchase getRecordById(long purchaseId) {
		Purchase purchase = null;
		try{
			purchase = jdbcTemplate.queryForObject(QueryConstants.GET_PURCHASE,  new BeanPropertyRowMapper<Purchase>(Purchase.class),new Object[]{purchaseId,Constants.ACTIVE});
		}catch(Exception e){
			
		}
		return purchase;
	}


	@Override
	public List<Content> listAllPurchase(int strtRow, int endRow, int isActive) {
		List<Content> purchaseList = null;
		try{
			purchaseList = jdbcTemplate.query(QueryConstants.LIST_ALL_PURCHASE,new PurchaseRowMapper(),new Object[]{isActive,endRow,strtRow});
		}catch(Exception e){
			e.printStackTrace();
		}
		return purchaseList;
	}


	@Override
	public long getCurrentPurchaseId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_PURCHASE_CURR_SEQ, Long.class);
	}


	@Override
	public List<Content> listAllPurchaseInclPriveleged(int strtRow, int endRow,
			int isActive) {
		List<Content> purchaseList = null;
		try{
			purchaseList = jdbcTemplate.query(QueryConstants.LIST_ALL_PURCHASE_INCL_PRIV,new PurchaseRowMapper(),new Object[]{isActive,endRow,strtRow});
		}catch(Exception e){
			e.printStackTrace();
		}
		return purchaseList;
	}
	
	@Override
	public Long getActivePurchaseRowCount() throws Exception {
		return jdbcTemplate.queryForObject(QueryConstants.COUNT_ACTIVE_PURCHASE,Long.class,new Object[]{Constants.ACTIVE});
	}

	@Override
	public Long getActivePurchaseRowCountInclPriveleged() throws Exception {
		return jdbcTemplate.queryForObject(QueryConstants.COUNT_ACTIVE_PURCHASE_INCL_PRIV,Long.class,new Object[]{Constants.ACTIVE});
	}


	@Override
	public List<Purchase> listPurchaseByInvoiceId(long invoiceId) {
		List<Purchase> purchaseList = null;
		try{
			purchaseList = jdbcTemplate.query(QueryConstants.LIST_PURCHASE_BY_INVOICE_ID, new BeanPropertyRowMapper<Purchase>(Purchase.class),new Object[]{Constants.ACTIVE,invoiceId});
		}catch(Exception e){
			e.printStackTrace();
		}
		return purchaseList;
	}
}
