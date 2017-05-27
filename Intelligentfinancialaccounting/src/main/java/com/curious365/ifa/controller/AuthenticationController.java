package com.curious365.ifa.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthenticationController {
	
	private Log log = LogFactory.getLog(AuthenticationController.class);
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login( @RequestParam(value = "error", required = false) String error,
		    @RequestParam(value = "logout", required = false) String logout,
		    HttpServletRequest request){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
	    mav.addObject("logout",logout);
	    mav.addObject("error",error);
		mav.setViewName("login");
		return mav;
	}
	
}
