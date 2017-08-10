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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.common.Roles;
import com.curious365.ifa.dto.Datatable;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.service.InvoiceService;

@RestController
public class InvoiceRestController {
	private Log log = LogFactory.getLog(InvoiceRestController.class);
	
	@Autowired
	private InvoiceService invoiceService;
	
	@RequestMapping("/listInvoiceLike")
	public Map<String,Object> populateAutocomplete(@RequestParam("query") String query,@RequestParam("invoicetype") String invoicetype){
		log.debug("entering..");
		if(StringUtils.hasText(query)){
			query = query.toLowerCase();
		}
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
			return invoiceService.populateAutocompleteInclPrivileged(query,invoicetype);
		}else{
			return invoiceService.populateAutocomplete(query,invoicetype);
		}
		
	}
	
	@RequestMapping(value = "/listEstimatesForMonth")
	public Map<String,Object> listEstimatesForMonth(@ModelAttribute Datatable datatable,@RequestParam("monthOfYear") String monthOfYear){
		log.debug("entering..");
		Map<String,Object> estimatesMap = new HashMap<String,Object>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			List<Invoice> estimateList = new ArrayList<Invoice>();
			/**
			 * Get current user information 
			 */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
				estimateList = invoiceService.listEstimatesInclPrivilegedForMonth(monthOfYear);
			}else{
				estimateList = invoiceService.listEstimatesForMonth(monthOfYear);
			}
			estimatesMap.put("data", estimateList);
		} catch (Exception e) {
			estimatesMap.put("data", new ArrayList());
		};
			estimatesMap.put("draw", datatable.getDraw());
			estimatesMap.put("recordsTotal", datatable.getRowCount());
			estimatesMap.put("recordsFiltered", datatable.getRowCount());	
		return estimatesMap;
	}
}
