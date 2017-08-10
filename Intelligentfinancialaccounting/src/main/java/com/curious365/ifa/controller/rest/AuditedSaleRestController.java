package com.curious365.ifa.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.common.Roles;
import com.curious365.ifa.controller.AuditedSaleController;
import com.curious365.ifa.dto.AuditedSales;
import com.curious365.ifa.dto.Datatable;
import com.curious365.ifa.service.AuditedSalesService;

@RestController
public class AuditedSaleRestController {
	private Log log = LogFactory.getLog(AuditedSaleController.class);
	
	@Autowired
	private AuditedSalesService auditedSalesService;

	@RequestMapping(value = "/listAuditedSalesForMonth")
	public Map<String,Object> listAuditedSalesForMonth(@ModelAttribute Datatable datatable,@RequestParam("monthOfYear") String monthOfYear){
		log.debug("entering..");
		Map<String,Object> auditedSalesMap = new HashMap<String,Object>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			List<AuditedSales> auditedSalesList = new ArrayList<AuditedSales>();
			/**
			 * Get current user information 
			 */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
				auditedSalesList = auditedSalesService.listAuditedSalesInclPrivilegedForMonth(monthOfYear);
			}else{
				auditedSalesList = auditedSalesService.listAuditedSalesForMonth(monthOfYear);
			}
			auditedSalesMap.put("data", auditedSalesList);
		} catch (Exception e) {
			auditedSalesMap.put("data", new ArrayList());
		};
			auditedSalesMap.put("draw", datatable.getDraw());
			auditedSalesMap.put("recordsTotal", datatable.getRowCount());
			auditedSalesMap.put("recordsFiltered", datatable.getRowCount());	
		return auditedSalesMap;
	}
}
