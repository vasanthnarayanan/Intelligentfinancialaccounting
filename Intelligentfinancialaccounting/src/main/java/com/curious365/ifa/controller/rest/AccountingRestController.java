package com.curious365.ifa.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.dto.Datatable;
import com.curious365.ifa.service.AccountingService;

@RestController
public class AccountingRestController {
	private Log log = LogFactory.getLog(AccountingRestController.class);
	
	@Autowired
	private AccountingService accountingService;

	public AccountingService getAccountingService() {
		return accountingService;
	}

	public void setAccountingService(AccountingService accountingService) {
		this.accountingService = accountingService;
	}
	
	@RequestMapping(value = "/listActiveAccounts")
	public Map<String,Object> listActiveAccounts(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		Map<String,Object> accountMap = new HashMap<String,Object>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			accountMap = accountingService.getCustomerAccountDetail(datatable.getCustomerId(),datatable.getInitialBalance(),strtRow, endRow,datatable.getLength());
		} catch (Exception e) {
			accountMap.put("data", new ArrayList());
		};
			accountMap.put("draw", datatable.getDraw());
			accountMap.put("recordsTotal", datatable.getRowCount());
			accountMap.put("recordsFiltered", datatable.getRowCount());	
		return accountMap;
	}

	
}
