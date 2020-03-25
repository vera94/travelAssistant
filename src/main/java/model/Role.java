package model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	USER, BUSINESS_USER, ADMIN;

	@Override
	public String getAuthority() {
		return this.toString();
	}
	
	
	
}
