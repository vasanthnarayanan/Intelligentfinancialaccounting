package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Transport;

public interface TransportDAO {
	public boolean create(Transport transport);
	public boolean edit(Transport transport);
	public boolean softDelete(long transportId);
	public Transport getRecordById(long transportId);
	public List<Transport> listAllTransport();
	public long getCurrentTransportId();

}
