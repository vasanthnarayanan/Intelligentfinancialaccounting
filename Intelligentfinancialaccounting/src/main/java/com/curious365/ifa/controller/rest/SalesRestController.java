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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.common.Roles;
import com.curious365.ifa.dto.Content;
import com.curious365.ifa.dto.Datatable;
import com.curious365.ifa.dto.Sales;
import com.curious365.ifa.exceptions.NoStockInHand;
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
	public void removeSale(@RequestParam long salesRecordId){
		log.debug("entering.."+ salesRecordId);
		try {
			salesService.remove(salesRecordId);
		} catch (Exception e) {
			
		}
	}
	
	@RequestMapping(value = "/addSalesAsync", method=RequestMethod.POST)
	public String addSales(@ModelAttribute Sales record){
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
		if(record.getSalesDate().equalsIgnoreCase("today"))
		{
			record.setSalesDate(dateNow);
		}else if(record.getSalesDate().equalsIgnoreCase("yesterday"))
		{
			record.setSalesDate(datePrev);
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(record.getSalesDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			record.setSalesDate(formatter.format(newdate));
		}
		try{
			//[to-do] check cash checkbox and insert income directly
			salesService.create(record);	
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

	
	@RequestMapping(value = "/listActiveSales")
	public Map<String,Object> listActiveSales(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> salesMap = new HashMap<String,Object>();
		List<Content> salesList = new ArrayList<Content>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			if(StringUtils.isEmpty(datatable.getSearch().get("value"))){
				if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
					salesList = salesService.listAllActiveSalesInclPriveleged(strtRow, endRow);
				}else{
					salesList = salesService.listAllActiveSales(strtRow, endRow);
				}
				salesMap.put("recordsFiltered", datatable.getRowCount());
			}else{
				if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
					salesList = salesService.listAllActiveSalesInclPriveleged(strtRow, endRow);
				}else{
					salesList = salesService.listAllActiveSales(strtRow, endRow);
				}
				salesMap.put("recordsFiltered", datatable.getFiltered());
			}
		} catch (Exception e) {
			e.printStackTrace();
		};
		salesMap.put("data", salesList);
		salesMap.put("draw", datatable.getDraw());
		salesMap.put("recordsTotal", datatable.getRowCount());
		
		return salesMap;
	}
	
	
}
