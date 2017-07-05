package com.curious365.ifa.dao;

import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

public class DummyTes {

	@Test
	public void test() {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("redirect:/showSalesSheet");
		uriBuilder.queryParam("error", "This is error");
		uriBuilder.queryParam("info", "Information");
		System.out.println(uriBuilder.build().encode().toUriString());
		
		long test = 5-10;
		System.out.println(-test);
		
	}

}
