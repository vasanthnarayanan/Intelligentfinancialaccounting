package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.RowMapper.FaultRowMapper;
import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.FaultDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Fault;

@Repository
public class FaultDAOImpl implements FaultDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public boolean create(Fault record) {
		int flag = jdbcTemplate.update(
				QueryConstants.ADD_FAULT,
				new Object[] { record.getFaultDate(),record.getFaultCustomerId(),
						record.getFaultItemId(), record.getFaultPieces(),
						record.getFaultCost(),record.getFaultRemarks(),Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean edit(Fault record) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_FAULT,
				new Object[] { record.getFaultDate(),record.getFaultCustomerId(),
						record.getFaultItemId(), record.getFaultPieces(),
						record.getFaultCost(),record.getFaultRemarks(),record.getFaultRecordId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean softDelete(long faultId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_FAULT,
				new Object[] { Constants.INACTIVE, faultId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Fault getRecordById(long faultId) {
		Fault fault = null;
		try{
			fault = jdbcTemplate.queryForObject(QueryConstants.GET_FAULT,  new BeanPropertyRowMapper<Fault>(Fault.class),new Object[]{faultId,Constants.ACTIVE});
		}catch(Exception e){
			
		}
		return fault;
	}


	@Override
	public List<Content> listAllFault(int strtRow, int endRow, int isActive) {
		List<Content> faultList = null;
		try{
			faultList = jdbcTemplate.query(QueryConstants.LIST_ALL_FAULT,new FaultRowMapper(),new Object[]{isActive,endRow,strtRow});
		}catch(Exception e){
			e.printStackTrace();
		}
		return faultList;
	}


	@Override
	public long getCurrentFaultId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_FAULT_CURR_SEQ, Long.class);
	}
}
