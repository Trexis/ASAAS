package net.trexis.asaas.web.security;

import java.util.ArrayList;
import java.util.List;

import net.trexis.asaas.web.configuration.ASaaSProperties;
import net.trexis.asaas.web.dal.UserDAL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class DatabaseAuthenticationProvider implements AuthenticationProvider {

	static final Logger logger = LoggerFactory.getLogger(DatabaseAuthenticationProvider.class);

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authentication;
		try{
			UserDAL userdal = new UserDAL();
			User user = userdal.getUser(auth.getName());
			if(auth.getCredentials().equals(user.getPassword())){
				List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
				grantedAuthorities.add(user.getAuthority());
				auth = new UsernamePasswordAuthenticationToken(user, auth.getCredentials(), grantedAuthorities);
			} else {
				throw new BadCredentialsException("Password does not match");
			}
		} catch (Exception ex){
			logger.error("Unable to authenticate user " + authentication.getName(), ex);
			throw new AuthenticationServiceException("Unable to authenticate user " + authentication.getName(), ex);
		}
		return auth;
	}

	public boolean supports(Class<?> authentication) {
		return true;
	}

}
