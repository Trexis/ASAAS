package net.trexis.asaas.web.admin.security;

import java.util.ArrayList;
import java.util.List;

import net.trexis.asaas.web.admin.proxy.ServicesProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ServicesAuthenticationProvider implements AuthenticationProvider {

	static final Logger logger = LoggerFactory.getLogger(ServicesAuthenticationProvider.class);
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authentication;
		try{
			ServicesProxy servicesProxy = new ServicesProxy();
			servicesProxy.setCredentials(authentication.getName(), (String)authentication.getCredentials());

			Gson gson = new Gson();
			JsonObject jsonobject = gson.fromJson(servicesProxy.restGet("/user"), JsonObject.class);
			JsonObject authority = (JsonObject)jsonobject.get("authority");

			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.get("role").getAsString()));
			auth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), auth.getCredentials(), grantedAuthorities);
		
		} catch(Exception ex){
			auth = null;
			logger.error("Unable to authenticate user " + authentication.getName(), ex);
			throw new AuthenticationServiceException("Unable to authenticate user " + authentication.getName(), ex);
		}
		return auth;
	}
	
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
