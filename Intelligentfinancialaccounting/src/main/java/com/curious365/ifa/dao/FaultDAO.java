package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Fault;

public interface FaultDAO {
	public boolean create(Fault record);
	public boolean edit(Fault record);
	public boolean softDelete(long faultId);
	public Fault getRecordById(long faultId);
	public List<Fault> listFaultByInvoiceId(long invoiceId);
	public Long getActiveFaultRowCount() throws Exception;
	public Long getActiveFaultRowCountInclPriveleged() throws Exception;
	public List<Content> listAllFault(int strtRow,int endRow,int isActive);
	public List<Content> listAllFaultInclPriveleged(int strtRow,int endRow,int isActive);
	public long getCurrentFaultId();
}
