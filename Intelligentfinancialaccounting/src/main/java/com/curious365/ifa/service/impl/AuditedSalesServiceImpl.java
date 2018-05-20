package com.curious365.ifa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curious365.ifa.dao.AuditedSalesDAO;
import com.curious365.ifa.dto.AuditedSales;
import com.curious365.ifa.service.AuditedSalesService;

@Service
public class AuditedSalesServiceImpl implements AuditedSalesService {
	
	@Autowired
	private AuditedSalesDAO auditedSalesDAO;

	@Override
	public List<AuditedSales> listAuditedSalesInclPrivilegedForMonth(
			String monthOfYear) {
			StringBuffer sb = new StringBuffer("%-");
			sb.append(monthOfYear);
			return auditedSalesDAO.listAuditedSalesInclPrivelegedByMonth(sb.toString());
			}

	@Override
	public List<AuditedSales> listAuditedSalesForMonth(String monthOfYear) {
		StringBuffer sb = new StringBuffer("%-");
		sb.append(monthOfYear);
		return auditedSalesDAO.listAuditedSalesByMonth(sb.toString());		
	}

	@Override
	public void editAuditedSales(AuditedSales record) throws Exception {
		auditedSalesDAO.edit(record);
	}

	@Override
	public AuditedSales getRecordById(long recordid) {
		return auditedSalesDAO.getRecordById(recordid);
	}

	@Override
	public void remove(long recordid) throws Exception {
		auditedSalesDAO.softDelete(recordid);
	}

}
