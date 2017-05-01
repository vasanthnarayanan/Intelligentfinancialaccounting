package com.curious365.ifa.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Datatable;
import com.curious365.ifa.service.SalesService;

@RestController
public class SalesRestController {

	private Log log = LogFactory.getLog(SalesRestController.class);
	
	@Autowired
	private SalesService salesService;

	public SalesService getSalesService() {
		return salesService;
	}

	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}
		
	@RequestMapping(value = "/removeSales", method=RequestMethod.GET)
	public boolean removeSale(@RequestParam long salesRecordId){
		log.debug("entering.."+ salesRecordId);
		return salesService.remove(salesRecordId);
	}
	
	@RequestMapping(value = "/listActiveSales")
	public Map<String,Object> listActiveSales(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		Map<String,Object> salesMap = new HashMap<String,Object>();
		List<Content> salesList = new ArrayList<Content>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			salesList = salesService.listAllActiveSales(strtRow, endRow);
		} catch (Exception e) {
			e.printStackTrace();
		};
		salesMap.put("data", salesList);
		salesMap.put("draw", datatable.getDraw());
		salesMap.put("recordsTotal", datatable.getRowCount());
		salesMap.put("recordsFiltered", datatable.getRowCount());
		return salesMap;
	}
	
	
}
