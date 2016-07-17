package net.trexis.asaas.web.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.trexis.asaas.service.Proxy;
import net.trexis.asaas.service.model.ProxyResponse;
import net.trexis.asaas.web.commons.NotOwnerException;
import net.trexis.asaas.web.commons.ResponseStatus;
import net.trexis.asaas.web.commons.Utilities;
import net.trexis.asaas.web.configuration.ASaaSProperties;
import net.trexis.asaas.web.service.component.SessionKeyGenerator;
import net.trexis.asaas.web.service.dal.RepositoryDAL;
import net.trexis.asaas.web.service.dal.SessionDAL;
import net.trexis.asaas.web.service.dal.UserDAL;
import net.trexis.asaas.web.service.model.Repository;
import net.trexis.asaas.web.service.model.RepositorySession;
import net.trexis.asaas.web.service.model.ServicesSession;
import net.trexis.asaas.web.service.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

@Controller
public class MainController {
	
	@Autowired
	private SessionKeyGenerator keygenerator;

	
	private String pagetitle = "treXis Any Site as a Service :: Rest Services";
	private Gson gson = new Gson();

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", pagetitle);
		model.setViewName("index");
		return model;

	}

	/*
	 * This is used to determine if the system user is still active, logged in, and has a active session
	 */
	@RequestMapping(value = { "/session" }, method = RequestMethod.GET)
	public @ResponseBody String session(HttpServletRequest httpServletRequest) {
		ServicesSession session = new ServicesSession(httpServletRequest.getSession(false));
		JsonElement sessionjson = gson.fromJson(session.toJson(), JsonElement.class);
		return Utilities.responseWrapper(ResponseStatus.success, sessionjson);
	}

	/*
	 * System User management
	 */
	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public @ResponseBody String userGet(@RequestParam(value="id", required=false) Integer id, @RequestParam(value="all", required=false, defaultValue="false") Boolean all, Authentication authentication) throws Exception {
		User curruser = (User)authentication.getPrincipal();
		UserDAL userdal = new UserDAL();
		if(id==null){
			if(!curruser.isAdmin()) throw new Exception("Only administrators can perform this action");
			List<User> users = new ArrayList<User>();
			users = userdal.list();
			return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(users));
		} else {
			if(curruser.getId()!=id&&!curruser.isAdmin()) throw new Exception("Only administrators can perform this action");
			User user = userdal.getUser(id);
			return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(user));
		}
	}
	
	@RequestMapping(value = { "/user" },headers = {"content-type=application/json"}, method = RequestMethod.POST)
	public @ResponseBody String userPost(Authentication authentication,@RequestBody String body) throws Exception {
		User curruser = (User)authentication.getPrincipal();
		UserDAL userdal = new UserDAL();
		User user = gson.fromJson(body, User.class);
		if(user.getId()==-1){
			
		} else {
			if(curruser.getId()!=user.getId()&&!curruser.isAdmin()) throw new Exception("Only administrators can perform this action");
		}
		userdal.update(user);
		return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(user));
	}
	
	@RequestMapping(value = { "/user" }, method = RequestMethod.DELETE)
	public @ResponseBody String userDelete(@RequestParam(value="id", required=true) Integer id, Authentication authentication) throws Exception {
		User curruser = (User)authentication.getPrincipal();
		UserDAL userdal = new UserDAL();
		User user = userdal.getUser(id);
		if(curruser.getId()!=user.getId()&&!curruser.isAdmin()) throw new Exception("Only administrators can perform this action");
		userdal.delete(user);
		return Utilities.responseWrapper(ResponseStatus.success, null);
	}


	/*
	 * Repository Management
	 */
	@RequestMapping(value = { "/repository" }, method = RequestMethod.GET)
	public @ResponseBody String repositoryGet(@RequestParam(value="id", required=false) Integer id, @RequestParam(value="all", required=false, defaultValue="false") Boolean all, Authentication authentication) throws Exception {
		User user = (User)authentication.getPrincipal();
		RepositoryDAL repodal = new RepositoryDAL();
		if(id==null){
			List<Repository> repositories = new ArrayList<Repository>();
			if(all & user.getAuthority().getAuthority().equals("ROLE_ADMIN")){
				repositories = repodal.list();
			} else {
				repositories = repodal.list(user.getId());
			}
			return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(repositories));
		} else {
			Repository repo = repodal.getRepository(id);
			if(repo.getUserid()!=user.getId() & !user.getAuthority().getAuthority().equals("ROLE_ADMIN")){
				throw new NotOwnerException("You are not the owner of this repository.");
			} else {
				return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(repo));
			}
		}
	}
	
	@RequestMapping(value = { "/repository" },headers = {"content-type=application/json"}, method = RequestMethod.POST)
	public @ResponseBody String repositoryPost(Authentication authentication,@RequestBody String body) throws Exception {
		User user = (User)authentication.getPrincipal();
		RepositoryDAL repodal = new RepositoryDAL();
		Repository repo = gson.fromJson(body, Repository.class);
		repo.setUserid(user.getId());  //this only apply to new items, since we do not update userid in the dal
		repodal.update(repo);
		return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(repo));
	}
	
	@RequestMapping(value = { "/repository" }, method = RequestMethod.DELETE)
	public @ResponseBody String repositoryDelete(Authentication authentication,@RequestParam(value="id", required=true) Integer id) throws Exception {
		User user = (User)authentication.getPrincipal();
		RepositoryDAL repodal = new RepositoryDAL();
		Repository repo = repodal.getRepository(id);
		if(repo.getUserid()!=user.getId() & !user.getAuthority().getAuthority().equals("ROLE_ADMIN")){
			throw new NotOwnerException("You are not the owner of this repository.");
		} else {
			repodal.delete(repo);
			return Utilities.responseWrapper(ResponseStatus.success, null);
		}
	}
	
	/*
	 * Repository Session Management
	 */
	@RequestMapping(value = { "/repositorySession" }, method = RequestMethod.GET)
	public @ResponseBody String repositorySessionGet(@RequestParam(value="repositoryId", required=false) Integer repositoryId, @RequestParam(value="all", required=false, defaultValue="false") Boolean all, Authentication authentication) throws Exception {
		User user = (User)authentication.getPrincipal();
		SessionDAL sessiondal = new SessionDAL();
		List<RepositorySession> sessions = new ArrayList<RepositorySession>();
		if(repositoryId==null){
			if(all & user.getAuthority().getAuthority().equals("ROLE_ADMIN")){
				sessions = sessiondal.list();
			} else {
				sessions = sessiondal.list(user.getId());
			}
			return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(sessions));
		} else {
			RepositoryDAL repodal = new RepositoryDAL();
			Repository repo = repodal.getRepository(repositoryId);
			if(repo.getUserid()!=user.getId() & !user.getAuthority().getAuthority().equals("ROLE_ADMIN")){
				throw new NotOwnerException("You are not the owner of this repository.");
			} else {
				sessions = sessiondal.listByRepository(repositoryId);
				return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(sessions));
			}
		}
	}
	@RequestMapping(value = { "/repositorySession" }, method = RequestMethod.DELETE)
	public @ResponseBody String repositorySessionDelete(Authentication authentication,@RequestParam(value="repositoryId", required=true) Integer repositoryId, @RequestParam(value="id", required=true) Integer id) throws Exception {
		User user = (User)authentication.getPrincipal();
		RepositoryDAL repodal = new RepositoryDAL();
		Repository repo = repodal.getRepository(repositoryId);
		if(repo.getUserid()!=user.getId() & !user.getAuthority().getAuthority().equals("ROLE_ADMIN")){
			throw new NotOwnerException("You are not the owner of this repository.");
		} else {
			SessionDAL sessiondal = new SessionDAL();
			RepositorySession session = sessiondal.getSession(id);
			sessiondal.delete(session);
			return Utilities.responseWrapper(ResponseStatus.success, null);
		}
	}
	
	/*
	 * Service
	 */
	@RequestMapping(value = { "/service/{repositoryName}.json" })
	public @ResponseBody String serviceGetJson(@PathVariable String repositoryName, @RequestParam(value="sid", required=false) Integer sessionId, @RequestParam(value="skey", required=false) String sessionKey, @RequestParam(value="url", required=false) String url, HttpServletRequest httpServletRequest, Authentication authentication) throws Exception {
		User user = (User)authentication.getPrincipal();
		ProxyResponse response = getProxyResponse(repositoryName, sessionId, sessionKey, httpServletRequest, user, url);
		return Utilities.responseWrapper(ResponseStatus.success, gson.fromJson(response.toJson(), JsonElement.class));
	}
	@RequestMapping(value = { "/service/{repositoryName}" })
	public @ResponseBody String serviceGet(@PathVariable String repositoryName, @RequestParam(value="sid", required=false) Integer sessionId, @RequestParam(value="skey", required=false) String sessionKey, @RequestParam(value="url", required=false) String url, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws Exception {
		User user = (User)authentication.getPrincipal();
		ProxyResponse response = getProxyResponse(repositoryName, sessionId, sessionKey, httpServletRequest, user, url);
		httpServletResponse.setHeader("sessionKey", response.getSessionkey());
		return response.toString();
	}
	
	private ProxyResponse getProxyResponse(String repositoryName, Integer sessionId, String sessionKey, HttpServletRequest httpServletRequest, User user, String url) throws Exception{
		String repositoryname = repositoryName.replace("%20", " ");
		RepositoryDAL repodal = new RepositoryDAL();
		Repository repo = repodal.getRepository(repositoryname, user.getId()); //this throws exception if not found
		
		//obtain session
		SessionDAL sessiondal = new SessionDAL();
		RepositorySession session = new RepositorySession();
		if(keygenerator==null) throw new Exception("Configure a bean implementing the SessionKeyGenerator class");
		
		if(sessionId==null){
			session.setId(-1);
			session.setRepositoryid(repo.getId());
		} else {
			if(sessionKey==null||sessionKey.equals("")){
				throw new Exception("Session Key is required when using session id.");
			}
			session = sessiondal.getSession(sessionId);
			if(!keygenerator.validateKey(sessionKey, session)){
				throw new Exception("Invalid session key provided for given session id");
			}
		}
		
		//if login url, then sniff for login credentials from login page
		
		//maybe have customer provide a login template, like username/password/etc?
		
		//try login
		// if success, continue
		// if failed, prompt for session properties
		
		
		String baseurl = Utilities.getBaseUrlFromRequest(httpServletRequest) + "/service/" + repositoryname + "?url=";
		String homeurl = repo.getPropertyValue("homeurl");
		String loginurl = repo.getPropertyValue("loginurl");
		
		Proxy proxy = new Proxy(baseurl, homeurl, loginurl);
		ProxyResponse response = new ProxyResponse();
		if(url==null){
			response = proxy.goHome();
		} else {
			URI uri = new URI(url);
			if(uri.isAbsolute()){
				throw new Exception("The URL attribute should be relative to the home url of the repository");
			}
			response = proxy.proxyExecute(httpServletRequest.getMethod(), url);
		}

		
		//template apply here
		
		// api management apply here
		
		//cache here

		//we generate the key on every response, since it might change if the session login details changed since last login.
		//but the key should consistantly stay the same if the the login/session properties remain the same
		session.setKey(keygenerator.generate(session));
		session.setLastdate(new Date());
		sessiondal.update(session);
		response.setSessionkey(session.getKey());

		return response;
	}
	
}