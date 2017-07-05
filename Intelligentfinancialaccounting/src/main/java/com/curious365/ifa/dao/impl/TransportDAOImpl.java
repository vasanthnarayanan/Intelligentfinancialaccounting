package com.curious365.ifa.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.TransportDAO;
import com.curious365.ifa.dto.Transport;

@Repository
public class TransportDAOImpl implements TransportDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean create(Transport transport) {
		int flag = jdbcTemplate.update(
				QueryConstants.CREATE_TRANSPORT,
				new Object[] { transport.getTransportName(),
						transport.getTransportAddress(),
						transport.getTransportPhone(),
						transport.getVehicleNo(),
						Constants.ACTIVE });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean edit(Transport transport) {
		int flag = jdbcTemplate.update(
				QueryConstants.UPDATE_TRANSPORT,
				new Object[] { transport.getTransportName(),
						transport.getTransportAddress(),
						transport.getTransportPhone(),
						transport.getVehicleNo(),
						transport.getTransportId() });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean softDelete(long transportId) {
		int flag = jdbcTemplate.update(
				QueryConstants.SOFT_DELETE_TRANSPORT,
				new Object[] { Constants.INACTIVE, transportId });
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Transport getRecordById(long transportId) {
		return jdbcTemplate.queryForObject(QueryConstants.GET_TRANSPORT,Transport.class,new Object[]{transportId,Constants.ACTIVE});
	}

	@Override
	public List<Transport> listAllTransport() {
		return jdbcTemplate.query(QueryConstants.LIST_TRANSPORT, new BeanPropertyRowMapper<Transport>(Transport.class));
	}

	@Override
	public long getCurrentTransportId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_TRANSPORT_CURR_SEQ, Long.class);
	}
}
