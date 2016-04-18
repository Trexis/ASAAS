package net.trexis.asaas.web.security;

import java.security.Principal;

import org.springframework.security.core.GrantedAuthority;

public class User implements Principal {

	private int id;
	private String username;
	private String password;
	private GrantedAuthority authority;
	private Boolean active;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GrantedAuthority getAuthority() {
		return authority;
	}

	public void setAuthority(GrantedAuthority authority) {
		this.authority = authority;
	}

	public String getName() {
		return username;
	}

}
