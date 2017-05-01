package com.curious365.ifa.service;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Sales;

public interface SalesService {
	public boolean create(Sales record);
	public boolean edit(Sales record);
	public boolean remove(long salesId);
	public Sales getRecordById(long salesId);
	public List<Content> listAllActiveSales(int strtRow,int endRow);
	
}
