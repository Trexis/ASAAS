package net.trexis.asaas.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.trexis.asaas.web.commons.HttpRequestMethod;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;

public class HttpClient extends HttpClientBuilder {

	CloseableHttpClient httpclient;
	String username = null;
	String password = null;
	boolean authenticated = false;

	
	public HttpClient() {
		super();
		this.httpclient = this.create().build();
	}


	/*
	 * Proxy
	 */

	public HttpClient(String proxyUrl, int proxyPort, String proxyUsername, String proxyPassword){
		super();
		Credentials proxycreds = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials( new AuthScope(proxyUrl,proxyPort), proxycreds );
		
		this.create();
		
		this.useSystemProperties();
		this.setProxy(new HttpHost(proxyUrl, proxyPort));
		this.setDefaultCredentialsProvider(credsProvider);
		this.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

		this.httpclient = this.build();
	}
	

	/*
	 * Credentials
	 */
	
	//This does normal basic authentication
	public void setCredentials(String userName, String password){
		this.username = userName;
		this.password = password;
		Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials( new AuthScope(AuthScope.ANY), defaultcreds );
		this.setDefaultCredentialsProvider(credsProvider);

		this.httpclient = this.create().build();
	
	}
	
	/*
	 * HTTP Request
	 */
	public String httpStringRequest(String URL) throws Exception{
		return httpStringRequest(URL, null);
	}
	
	public String httpStringRequest(String URL, Header[] headers) throws Exception{
		HttpUriRequest request = getHttpUriRequest(HttpRequestMethod.GET, URL, headers);
		return httpStringRequest(request);
	}

	public String httpStringRequest(HttpRequestMethod method, String URL, Header[] headers) throws Exception{
		HttpUriRequest request = getHttpUriRequest(method, URL, headers);
		return httpStringRequest(request);
	}
	
	public String httpStringPostRequest(String URL, Header[] headers, String postBody) throws Exception{
		HttpPost request = (HttpPost)getHttpUriRequest(HttpRequestMethod.POST, URL, headers);
		StringEntity entity = new StringEntity(postBody, "UTF-8");
		request.setEntity(entity);
		return httpStringRequest(request);
	}

	private HttpUriRequest getHttpUriRequest(HttpRequestMethod method, String URL, Header[] headers){
		//Initialize request.  Default is GET
		HttpUriRequest request =  new HttpGet(URL);
		if(method.equals(HttpRequestMethod.POST)) request = new HttpPost(URL);
		
		//Set headers
		if(headers!=null){
	        for(Header header: headers){
		        request.addHeader(header);
	        }
        }
	
		return request;
	}
	
	private String httpStringRequest(HttpUriRequest request) throws Exception{
		HttpResponse response = this.httpclient.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		
		String responseString = "";
		if(statusCode != HttpStatus.SC_NO_CONTENT) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString += line;
			}
		}
		
		if ((statusCode != HttpStatus.SC_OK) && (statusCode!=201)) {
			throw new Exception(request.getMethod() + " to url " + request.getURI() + " failed: " + response.toString() + " [" + responseString + "]");
		}
		
		return responseString;		
	}
}
