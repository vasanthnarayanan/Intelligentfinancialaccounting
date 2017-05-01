package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.RowMapper.SalesRowMapper;
import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.SalesDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Sales;

@Repository
public class SalesDAOImpl implements SalesDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public boolean create(Sales record) {
		int flag = jdbcTemplate.update(
				QueryConstants.ADD_SALES,
				new Object[] { record.getSalesDate(),record.getSalesCustomerId(),
						record.getSalesItemId(), record.getSalesPieces(),
						record.getSalesCost(),record.getSalesRemarks(),Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean edit(Sales record) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_SALES,
				new Object[] { record.getSalesDate(),record.getSalesCustomerId(),
						record.getSalesItemId(), record.getSalesPieces(),
						record.getSalesCost(),record.getSalesRemarks(),record.getSalesRecordId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean softDelete(long salesId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_SALES,
				new Object[] { Constants.INACTIVE, salesId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Sales getRecordById(long salesId) {
		Sales sales = null;
		try{
			sales = jdbcTemplate.queryForObject(QueryConstants.GET_SALES,  new BeanPropertyRowMapper<Sales>(Sales.class),new Object[]{salesId,Constants.ACTIVE});
		}catch(Exception e){
			
		}
		return sales;
	}
	
	@Override
	public List<Content> listAllSales(int strtRow,int endRow,int isActive) {
		List<Content> salesList = null;
		try{
			salesList = jdbcTemplate.query(QueryConstants.LIST_ALL_SALES,new SalesRowMapper(),new Object[]{isActive,endRow,strtRow});
		}catch(Exception e){
			e.printStackTrace();
		}
		return salesList;
	}


	@Override
	public long getCurrentSalesId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_SALES_CURR_SEQ, Long.class);
	}
}
