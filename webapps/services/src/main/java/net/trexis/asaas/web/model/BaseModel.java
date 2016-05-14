package net.trexis.asaas.web.model;

import com.google.gson.Gson;

public class BaseModel {

	public String toJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
