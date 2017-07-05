package com.curious365.ifa.controller.rest;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.common.Roles;
import com.curious365.ifa.service.InvoiceService;

@RestController
public class InvoiceRestController {
	private Log log = LogFactory.getLog(AccountingRestController.class);
	
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
}
