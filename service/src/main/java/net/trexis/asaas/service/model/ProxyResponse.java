package net.trexis.asaas.service.model;

import org.json.XML;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.trexis.asaas.web.commons.HttpRequestMethod;

public class ProxyResponse {

	private HttpRequestMethod method = HttpRequestMethod.GET;
	private String baseurl = "";
	private String url = "";
	private String rawresponse = "";
	private String cleanresponse = "";
	private String sessionkey = "";
	
	public ProxyResponse(){
		
	}
	
	public ProxyResponse(HttpRequestMethod method, String baseUrl, String url){
		this.method = method;
		this.baseurl = baseUrl;
		this.url = url;
	}
	
	public String getBaseurl() {
		return baseurl;
	}
	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRawresponse() {
		return rawresponse;
	}
	/*
	 * Setting raw response overwrite clean response
	 */
	public void setRawresponse(String rawresponse) {
		this.rawresponse = cleanresponse = rawresponse;
	}
	public String getCleanresponse() {
		return cleanresponse;
	}
	public void setCleanresponse(String cleanresponse) {
		this.cleanresponse = cleanresponse;
	}
	public HttpRequestMethod getMethod() {
		return method;
	}
	public void setMethod(HttpRequestMethod method) {
		this.method = method;
	}
	public String getSessionkey() {
		return sessionkey;
	}
	public void setSessionkey(String sessionkey) {
		this.sessionkey = sessionkey;
	}

	@Override
	public String toString(){
		return this.cleanresponse;
	}
	
	public String toJson(){
		return XML.toJSONObject(this.cleanresponse).toString();
	}
}
