package net.trexis.asaas.web.service.model;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.gson.Gson;

public class User extends BaseModel implements Principal  {

	private int id;
	private String username;
	private String password;
	private SimpleGrantedAuthority authority;
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

	public SimpleGrantedAuthority getAuthority() {
		return authority;
	}

	public void setAuthority(SimpleGrantedAuthority authority) {
		this.authority = authority;
	}
	
	public boolean isAdmin(){
		return this.authority.getAuthority().equals("ROLE_ADMIN");
	}

	public String getName() {
		return username;
	}

	
}
