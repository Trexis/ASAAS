package net.trexis.asaas.web.service.component.impl;

import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import net.trexis.asaas.web.commons.Encryptor;
import net.trexis.asaas.web.commons.Utilities;
import net.trexis.asaas.web.configuration.ASaaSProperties;
import net.trexis.asaas.web.service.component.SessionKeyGenerator;
import net.trexis.asaas.web.service.model.Property;
import net.trexis.asaas.web.service.model.RepositorySession;
import net.trexis.asaas.web.service.model.User;

public class SessionKeyGeneratorImpl implements SessionKeyGenerator{

	
	@Autowired
	private ASaaSProperties properties;
	
	public SessionKeyGeneratorImpl(){
		if(properties==null) properties = new ASaaSProperties();
	}

	@Override
	public String generate(RepositorySession session) throws Exception {
		User user = getUserFromContext();
		String seckey = "feacbc02a3a697b0";
		String sessionkey = makeSessionKey(session);
		return Encryptor.encrypt(seckey, sessionkey);
	}

	@Override
	public boolean validateKey(String key, RepositorySession session) throws Exception {
		User user = getUserFromContext();
		String seckey = properties.getString("security.key");
		String seciv = user.getUsername();
		String sessionkey = makeSessionKey(session);
		String sessionkeyenc = Encryptor.encrypt(seckey, sessionkey);
		return sessionkeyenc.equals(key);
	}

	private String makeSessionKey(RepositorySession session){
		String sessionkey = Utilities.dateTimeToISO(session.getInitdate().getTime());
		for(Property property: session.getProperties()){
			sessionkey += property.getName()+property.getValue();
		}
		return sessionkey;
	}

	public User getUserFromContext() throws Exception{
		User user = null;
		if(SecurityContextHolder.getContext()!=null){
			if(SecurityContextHolder.getContext().getAuthentication()!=null){
				if(SecurityContextHolder.getContext().getAuthentication().getPrincipal()!=null){
					user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				}
			}
		}
		if(user==null) throw new Exception("Secured User required for encryption");
		return user;
	}
}
