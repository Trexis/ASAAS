package net.trexis.asaas.web.admin.proxy;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import net.trexis.asaas.web.admin.model.User;
import net.trexis.asaas.web.configuration.ASaaSProperties;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ServicesProxy extends RestTemplate {

	ASaaSProperties asaasProperties;
	
	private String username;
	private String password;
	
	public ServicesProxy(){
		super();
		//We load the proxy credentials incase we simply proxy as the already logged in user.  
		//Otherwise call authenticate to change these values.
		loadCredentialsFromContext();
		asaasProperties = new ASaaSProperties();
	}
	
	public String restPost(String relativeUri) throws MalformedURLException, URISyntaxException{
		return restPost(relativeUri, null);
	}
	public String restPost(String relativeUri, String body) throws MalformedURLException, URISyntaxException{
		String uri = getFullUri(relativeUri);
		this.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body, createHeaders(username, password));
		ResponseEntity<String> response = this.exchange(uri, HttpMethod.POST,  httpEntity, String.class);
		return response.getBody();
	}
	
	public String restGet(String relativeUri) throws MalformedURLException, URISyntaxException{
		String uri = getFullUri(relativeUri);
		ResponseEntity<String> response = this.exchange(uri, HttpMethod.GET,  new HttpEntity<String>(createHeaders(username, password)), String.class);
		return response.getBody();
	}

	public String restDelete(String relativeUri) throws MalformedURLException, URISyntaxException{
		String uri = getFullUri(relativeUri);
		ResponseEntity<String> response = this.exchange(uri, HttpMethod.DELETE,  new HttpEntity<String>(createHeaders(username, password)), String.class);
		return response.getBody();
	}
	
	public void setCredentials(String username, String password) throws MalformedURLException, URISyntaxException{
		this.username = username;
		this.password = password;
	}
	
	private HttpHeaders createHeaders(final String username, final String password ){
		   HttpHeaders headers = new HttpHeaders(){
		      {
		         String auth = username + ":" + password;
		         byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set("Authorization", authHeader);
		      }
		   };
		   headers.setContentType(MediaType.APPLICATION_JSON);
		   return headers;
		}
	
	private void loadCredentialsFromContext(){
		if(SecurityContextHolder.getContext()!=null){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth!=null){
				User user = (User)auth.getPrincipal();
				this.username = user.getName();
				this.password = user.getPassword();
			}
		}
	}
	
	private String getFullUri(String relativeURI) throws URISyntaxException, MalformedURLException{
		String servicesurl = asaasProperties.getString("services.proxy.url");
		URL baseUrl = new URL(servicesurl);
		URI uri = baseUrl.toURI();
	    String newPath = uri.getPath() + relativeURI;
	    URI newUri = uri.resolve(newPath);
	    return newUri.toURL().toString();
	}

}
