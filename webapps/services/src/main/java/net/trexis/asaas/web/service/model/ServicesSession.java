package net.trexis.asaas.web.service.model;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import net.trexis.asaas.web.commons.Utilities;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ServicesSession extends BaseModel {
	
	private transient HttpSession session;

	public ServicesSession(HttpSession session){
		this.session = session;
	}
	
	public User getUser(){
		User user = null;
		if(SecurityContextHolder.getContext()!=null){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth!=null){
				user = (User)auth.getPrincipal();
			}
		}
		return user;
	}
	
	@Override
	public String toJson(){
		Gson gson = new Gson();
		JsonObject jsonobject = new JsonObject();
		jsonobject.addProperty("lastAccessTime", Utilities.dateTimeToISO(this.session.getLastAccessedTime()));
		jsonobject.addProperty("creationTime", Utilities.dateTimeToISO(this.session.getCreationTime()));
		jsonobject.addProperty("expires", Utilities.dateTimeToISO(this.session.getLastAccessedTime() + this.session.getMaxInactiveInterval()*1000));

		JsonElement userJson = new JsonObject();
		User user = getUser();
		if(user!=null)	userJson = gson.toJsonTree(user);
		jsonobject.add("user", userJson);
		return gson.toJson(jsonobject);
	}
}
