package net.trexis.asaas.web.proxy;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import net.trexis.asaas.web.configuration.ASaaSProperties;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

public class ServicesProxy extends RestTemplate {

	@Autowired
	ASaaSProperties asaasProperties;
	
	private String username;
	private String password;
	
	public ServicesProxy(){
		super();
		asaasProperties = new ASaaSProperties();
	}
	
	public String restPost(String relativeUri) throws MalformedURLException, URISyntaxException{
		String uri = getFullUri(relativeUri);
		ResponseEntity<String> response = this.exchange(uri, HttpMethod.POST,  new HttpEntity<String>(createHeaders(username, password)), String.class);
		return response.getBody();
	}
	
	public String restGet(String relativeUri) throws MalformedURLException, URISyntaxException{
		String uri = getFullUri(relativeUri);
		ResponseEntity<String> response = this.exchange(uri, HttpMethod.GET,  new HttpEntity<String>(createHeaders(username, password)), String.class);
		return response.getBody();
	}
	
	public void authenticate(String username, String password) throws MalformedURLException, URISyntaxException{
		this.username = username;
		this.password = password;
		String response = this.restPost("/login");
	}
	
	private HttpHeaders createHeaders(final String username, final String password ){
		   return new HttpHeaders(){
		      {
		         String auth = username + ":" + password;
		         byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set("Authorization", authHeader);
		      }
		   };
		}
	
	/*private void loadProxyCredentials(){
		if(SecurityContextHolder.getContext()!=null){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth!=null){
				this.username = auth.getName();
				this.password = (String) auth.getCredentials();
			}
		}
	}*/
	
	private String getFullUri(String relativeURI) throws URISyntaxException, MalformedURLException{
		String servicesurl = asaasProperties.getString("services.proxy.url");
		URL baseUrl = new URL(servicesurl);
		URI uri = baseUrl.toURI();
	    String newPath = uri.getPath() + relativeURI;
	    URI newUri = uri.resolve(newPath);
	    return newUri.toURL().toString();
	}

}
