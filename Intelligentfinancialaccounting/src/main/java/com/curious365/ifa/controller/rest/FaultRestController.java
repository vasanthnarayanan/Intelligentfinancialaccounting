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
import com.curious365.ifa.service.FaultService;

@RestController
public class FaultRestController {

	private Log log = LogFactory.getLog(FaultRestController.class);
	
	@Autowired
	private FaultService faultService;

	public FaultService getFaultService() {
		return faultService;
	}

	public void setFaultService(FaultService faultService) {
		this.faultService = faultService;
	}
	
	@RequestMapping(value = "/removeFault", method=RequestMethod.GET)
	public boolean removeSale(@RequestParam long faultRecordId){
		log.debug("entering.."+ faultRecordId);
		return faultService.remove(faultRecordId);
	}
	
	@RequestMapping(value = "/listActiveFault")
	public Map<String,Object> listActiveFault(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		Map<String,Object> faultMap = new HashMap<String,Object>();
		List<Content> faultList = new ArrayList<Content>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			faultList = faultService.listAllActiveFault(strtRow, endRow);
		} catch (Exception e) {
			e.printStackTrace();
		};
		faultMap.put("data", faultList);
		faultMap.put("draw", datatable.getDraw());
		faultMap.put("recordsTotal", datatable.getRowCount());
		faultMap.put("recordsFiltered", datatable.getRowCount());
		return faultMap;
	}
	
	
	
}
