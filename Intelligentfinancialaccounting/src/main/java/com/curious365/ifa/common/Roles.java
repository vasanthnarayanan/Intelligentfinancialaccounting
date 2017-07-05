package com.curious365.ifa.common;

public enum Roles {
	ROLE_ADMIN("ROLE_ADMIN"),ROLE_MODERATOR("ROLE_MODERATOR"),ROLE_USER("ROLE_USER");
	
	private String value;
	
	Roles(String role){
		this.value = role;
	}
	public String getValue() {
		return value;
	}
}
