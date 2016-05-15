package net.trexis.asaas.web.service.model;

import java.util.List;

public class RepositorySession extends BaseModel {

	private int id;
	private int repositoryid;
	private String key;
	private List<Property> properties;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRepositoryid() {
		return repositoryid;
	}
	public void setRepositoryid(int repositoryid) {
		this.repositoryid = repositoryid;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
}
