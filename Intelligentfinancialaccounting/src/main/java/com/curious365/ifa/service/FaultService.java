package com.curious365.ifa.service;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Fault;

public interface FaultService {
	public boolean create(Fault record);
	public boolean edit(Fault record);
	public boolean remove(long faultId);
	public Fault getRecordById(long faultId);
	public List<Content> listAllActiveFault(int strtRow,int endRow);
	
}
