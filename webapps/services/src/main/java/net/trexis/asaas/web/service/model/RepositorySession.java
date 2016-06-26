package net.trexis.asaas.web.service.model;

public class RepositorySession extends BaseModel {

	private int id;
	private int repositoryid;
	private String key;

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
	
}
