package com.curious365.ifa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dao.FaultDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Fault;
import com.curious365.ifa.service.FaultService;

@Service
public class FaultServiceImpl implements FaultService {
	
	@Autowired
	private FaultDAO faultDAO;

	public FaultDAO getFaultDAO() {
		return faultDAO;
	}

	public void setFaultDAO(FaultDAO faultDAO) {
		this.faultDAO = faultDAO;
	}

	@Override
	public boolean create(Fault record) {
		return faultDAO.create(record);
	}

	@Override
	public boolean edit(Fault record) {
		return faultDAO.edit(record);
	}

	@Override
	public boolean remove(long faultId) {
		return faultDAO.softDelete(faultId);
	}

	@Override
	public Fault getRecordById(long faultId) {
		return faultDAO.getRecordById(faultId);
	}

	@Override
	public List<Content> listAllActiveFault(int strtRow, int endRow) {
		return faultDAO.listAllFault(strtRow, endRow, Constants.ACTIVE);
	}

}
