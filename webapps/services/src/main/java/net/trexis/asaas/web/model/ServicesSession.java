package net.trexis.asaas.web.model;

import javax.servlet.http.HttpSession;

import net.trexis.asaas.web.commons.Utilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class ServicesSession extends BaseModel {
	
	private transient HttpSession session;

	public ServicesSession(HttpSession session){
		this.session = session;
	}
	
	@Override
	public String toJson(){
		JsonObject jsonobject = new JsonObject();
		jsonobject.addProperty("lastAccessTime", Utilities.dateTimeToISO(this.session.getLastAccessedTime()));
		jsonobject.addProperty("creationTime", Utilities.dateTimeToISO(this.session.getCreationTime()));
		jsonobject.addProperty("expires", Utilities.dateTimeToISO(this.session.getLastAccessedTime() + this.session.getMaxInactiveInterval()*1000));

		Gson gson = new Gson();
		return gson.toJson(jsonobject);
	}
}
