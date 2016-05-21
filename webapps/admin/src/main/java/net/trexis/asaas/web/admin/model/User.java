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

	public JsonObject getUserdataWithPassword() {
		return userdata;
	}
	//We remove the password, as this is written out to the javascript DOM in the dashboard
	public JsonObject getUserdata() {
		JsonObject tempuserdata = this.userdata;
		tempuserdata.remove("password");
		return tempuserdata;
	}

	public void setUserdata(JsonObject userdata) {
		this.userdata = userdata;
	}
	
	
}
