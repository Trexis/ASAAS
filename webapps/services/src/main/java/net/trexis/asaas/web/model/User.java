package net.trexis.asaas.web.model;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.google.gson.Gson;

public class User extends BaseModel implements Principal  {

	private int id;
	private String username;
	private String password;
	private GrantedAuthority authority;
	private Boolean active;
	private List<Property> properties;
	
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

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	
}
