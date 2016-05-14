package net.trexis.asaas.web.model;

import java.util.List;

public class Repository extends BaseModel  {

	private int id;
	private int userid;
	private List<Property> properties;
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	
	
}
