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
import com.curious365.ifa.service.PurchaseService;

@RestController
public class PurchaseRestController {

	private Log log = LogFactory.getLog(PurchaseRestController.class);
	
	@Autowired
	private PurchaseService purchaseService;

	public PurchaseService getPurchaseService() {
		return purchaseService;
	}

	public void setPurchaseService(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}
	
	@RequestMapping(value = "/removePurchase", method=RequestMethod.GET)
	public boolean removeSale(@RequestParam long purchaseRecordId){
		log.debug("entering.."+ purchaseRecordId);
		return purchaseService.remove(purchaseRecordId);
	}
	
	@RequestMapping(value = "/listActivePurchase")
	public Map<String,Object> listActivePurchase(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		Map<String,Object> purchaseMap = new HashMap<String,Object>();
		List<Content> purchaseList = new ArrayList<Content>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			purchaseList = purchaseService.listAllActivePurchase(strtRow, endRow);
		} catch (Exception e) {
			e.printStackTrace();
		};
		purchaseMap.put("data", purchaseList);
		purchaseMap.put("draw", datatable.getDraw());
		purchaseMap.put("recordsTotal", datatable.getRowCount());
		purchaseMap.put("recordsFiltered", datatable.getRowCount());
		return purchaseMap;
	}
	
	
	
}
