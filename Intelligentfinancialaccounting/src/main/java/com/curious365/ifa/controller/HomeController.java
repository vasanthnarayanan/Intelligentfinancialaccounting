package com.curious365.ifa.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	private Log log = LogFactory.getLog(HomeController.class);
	

	@RequestMapping("/home")
	public String welcome(){
		log.debug("welcome");
		return "welcome";
	}

}
