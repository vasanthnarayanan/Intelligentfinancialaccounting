package com.curious365.ifa.controller.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.common.Roles;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Datatable;
import com.curious365.ifa.dto.Purchase;
import com.curious365.ifa.exceptions.NoStockInHand;
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
	public void removePurchase(@RequestParam long purchaseRecordId){
		log.debug("entering.."+ purchaseRecordId);
		try{
			purchaseService.remove(purchaseRecordId);	
		}catch(Exception e){
			
		}
		
	}
	
	@RequestMapping(value = "/addPurchaseAsync", method=RequestMethod.POST)
	public String addPurchase(@ModelAttribute Purchase record){
		String message = "Unable to add sales! Please try with valid data";
		boolean hasError= false;
		log.debug("entering.."+ record);
		/* current date */
		Date date = new Date();
		SimpleDateFormat formatter= 
		new SimpleDateFormat("dd/MMM/yyyy");
		String dateNow = formatter.format(date.getTime());
		/* prev date */
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		String datePrev = formatter.format(date.getTime() - MILLIS_IN_DAY);
		if(record.getPurchaseDate().equalsIgnoreCase("today"))
		{
			record.setPurchaseDate(dateNow);
		}else if(record.getPurchaseDate().equalsIgnoreCase("yesterday"))
		{
			record.setPurchaseDate(datePrev);
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(record.getPurchaseDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			record.setPurchaseDate(formatter.format(newdate));
		}
		try{
			//[to-do] check cash checkbox and insert income directly
			purchaseService.create(record);	
		}catch (NoStockInHand e) {
			hasError= true;
			message = e.getMessage();
		} catch (Exception e) {
			hasError= true;
		}finally{
			if(!hasError){
				message = "Success";
			}
		}
		
		return message;
	}
	
	@RequestMapping(value = "/listActivePurchase")
	public Map<String,Object> listActivePurchase(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> purchaseMap = new HashMap<String,Object>();
		List<Content> purchaseList = new ArrayList<Content>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
				purchaseList = purchaseService.listAllActivePurchaseInclPriveleged(strtRow, endRow);
			}else{
				purchaseList = purchaseService.listAllActivePurchase(strtRow, endRow);
			}
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
