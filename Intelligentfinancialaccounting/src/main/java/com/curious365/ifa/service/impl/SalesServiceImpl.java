package com.curious365.ifa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dao.SalesDAO;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.service.SalesService;

@Service
public class SalesServiceImpl implements SalesService {
	
	@Autowired
	private SalesDAO salesDAO;

	public SalesDAO getSalesDAO() {
		return salesDAO;
	}

	public void setSalesDAO(SalesDAO salesDAO) {
		this.salesDAO = salesDAO;
	}

	@Override
	public boolean create(Sales record) {
		return salesDAO.create(record);
	}

	@Override
	public boolean edit(Sales record) {
		return salesDAO.edit(record);
	}

	@Override
	public boolean remove(long salesId) {
		return salesDAO.softDelete(salesId);
	}

	@Override
	public Sales getRecordById(long salesId) {
		return salesDAO.getRecordById(salesId);
	}
	
	@Override
	public List<Content> listAllActiveSales(int strtRow,int endRow) {
		return salesDAO.listAllSales(strtRow,endRow,Constants.ACTIVE);
	}

}
