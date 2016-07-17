package net.trexis.asaas.web.service.model;

import java.util.Date;

public class RepositorySession extends BaseModel {

	private int id;
	private int repositoryid;
	private String key;
	private Date initdate = new Date();
	private Date lastdate = new Date();

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
	public Date getInitdate() {
		return initdate;
	}
	public void setInitdate(Date initdate) {
		this.initdate = initdate;
	}
	public Date getLastdate() {
		return lastdate;
	}
	public void setLastdate(Date lastdate) {
		this.lastdate = lastdate;
	}
}
