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
import com.curious365.ifa.dto.Fault;
import com.curious365.ifa.exceptions.NoStockInHand;
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
	public void removeFault(@RequestParam long faultRecordId){
		log.debug("entering.."+ faultRecordId);
		try {
			faultService.remove(faultRecordId);
		} catch (Exception e) {
		}
	}
	
	@RequestMapping(value = "/addFaultAsync", method=RequestMethod.POST)
	public String addFault(@ModelAttribute Fault record){
		String message = "Unable to add fault. Please try with valid data";
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
		if(record.getFaultDate().equalsIgnoreCase("today"))
		{
			record.setFaultDate(dateNow);
		}else if(record.getFaultDate().equalsIgnoreCase("yesterday"))
		{
			record.setFaultDate(datePrev);
		}else
		{
			DateFormat newformatter ; 
			Date newdate = null ; 
			newformatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				newdate = (Date)newformatter.parse(record.getFaultDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			record.setFaultDate(formatter.format(newdate));
		}
		try{
			//[to-do] check cash checkbox and insert income directly
			faultService.create(record);	
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
	
	@RequestMapping(value = "/listActiveFault")
	public Map<String,Object> listActiveFault(@ModelAttribute Datatable datatable){
		log.debug("entering..");
		/**
		 * Get current user information 
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String,Object> faultMap = new HashMap<String,Object>();
		List<Content> faultList = new ArrayList<Content>();
		int strtRow=datatable.getStart();
		int endRow=(datatable.getStart()+datatable.getLength());
		try {
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))){
				faultList = faultService.listAllActiveFaultInclPriveleged(strtRow, endRow);
			}else{
				faultList = faultService.listAllActiveFault(strtRow, endRow);
			}
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
