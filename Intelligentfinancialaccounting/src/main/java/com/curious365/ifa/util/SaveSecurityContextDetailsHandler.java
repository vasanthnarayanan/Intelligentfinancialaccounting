package com.curious365.ifa.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class SaveSecurityContextDetailsHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setAttribute("authenticated", authentication.isAuthenticated());
		session.setAttribute("username", authentication.getName());
		session.setAttribute("role", authentication.getAuthorities().toString());
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
