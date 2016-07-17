package net.trexis.asaas.web.service.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class BaseModel {

	private List<Property> properties = new ArrayList<Property>();

	public String toJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public Property getProperty(String propertyName){
		Property returnproperty = null;
		for(Property property:this.properties){
			if(property.getName().equals(propertyName)){
				returnproperty = property;
				break;
			}
		}
		return returnproperty;
	}
	
	public String getPropertyValue(String propertyName){
		String returnvalue = "";
		Property property = getProperty(propertyName);
		if(property!=null) returnvalue = property.getValue();
		return returnvalue;
	}
}
