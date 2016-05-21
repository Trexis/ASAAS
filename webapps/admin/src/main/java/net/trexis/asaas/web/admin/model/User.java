package net.trexis.asaas.web.admin.model;

import java.security.Principal;

import com.google.gson.JsonObject;

public class User extends BaseModel implements Principal {

	private String username;
	private String password;
	private JsonObject userdata; 
	
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String getName() {
		return username;
	}

	public void setName(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JsonObject getUserdata() {
		return userdata;
	}

	public void setUserdata(JsonObject userdata) {
		this.userdata = userdata;
	}
	
	
}
