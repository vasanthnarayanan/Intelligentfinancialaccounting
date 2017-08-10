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
import com.curious365.ifa.dto.Datatable;
import com.curious365.ifa.dto.TaxInvoice;
import com.curious365.ifa.service.TaxInvoiceService;

@RestController
public class TaxInvoiceRestController {
	private Log log = LogFactory.getLog(TaxInvoiceRestController.class);
	
	@Autowired
	private TaxInvoiceService taxInvoiceService;
	
	@RequestMapping(value = "/listTaxInvoicesForMonth")
	public Map<String,Object> listTaxInvoicesForMonth(@ModelAttribute Datatable datatable,@RequestParam("monthOfYear") String monthOfYear){
		log.debug("entering..");
		Map<String,Object> taxInvoicesMap = new HashMap<String,Object>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			List<TaxInvoice> taxInvoiceList = new ArrayList<TaxInvoice>();
			/**
			 * Get current user information 
			 */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
				taxInvoiceList = taxInvoiceService.listTaxInvoicesInclPrivilegedForMonth(monthOfYear);
			}else{
				taxInvoiceList = taxInvoiceService.listTaxInvoicesForMonth(monthOfYear);
			}
			taxInvoicesMap.put("data", taxInvoiceList);
		} catch (Exception e) {
			taxInvoicesMap.put("data", new ArrayList());
		};
			taxInvoicesMap.put("draw", datatable.getDraw());
			taxInvoicesMap.put("recordsTotal", datatable.getRowCount());
			taxInvoicesMap.put("recordsFiltered", datatable.getRowCount());	
		return taxInvoicesMap;
	}

}
