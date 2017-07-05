package com.curious365.ifa.service;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Fault;
import com.curious365.ifa.dto.Invoice;

public interface FaultService {
	public void create(Fault record) throws Exception;
	public void create(List<Fault> record,Invoice invoice) throws Exception;
	public void edit(Fault record) throws Exception;
	public void remove(long faultId) throws Exception;
	public Fault getRecordById(long faultId);
	public long getActiveFaultRowCount();
	public long getActiveFaultRowCountInclPriveleged();
	public List<Content> listAllActiveFault(int strtRow,int endRow);
	public List<Content> listAllActiveFaultInclPriveleged(int strtRow,int endRow);
	
}
