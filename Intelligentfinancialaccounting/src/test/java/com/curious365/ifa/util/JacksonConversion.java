package com.curious365.ifa.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonConversion {
	public static String toJSON(Object object) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}
