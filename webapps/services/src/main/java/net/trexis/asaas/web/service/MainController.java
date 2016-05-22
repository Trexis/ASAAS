package net.trexis.asaas.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.trexis.asaas.web.commons.ResponseStatus;
import net.trexis.asaas.web.commons.Utilities;
import net.trexis.asaas.web.service.dal.RepositoryDAL;
import net.trexis.asaas.web.service.dal.UserDAL;
import net.trexis.asaas.web.service.model.Repository;
import net.trexis.asaas.web.service.model.ServicesSession;
import net.trexis.asaas.web.service.model.User;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class MainController {
	
	private String pagetitle = "treXis Any Site as a Service :: Rest Services";
	private Gson gson = new Gson();

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", pagetitle);
		model.setViewName("index");
		return model;

	}

	@RequestMapping(value = { "/session" }, method = RequestMethod.GET)
	public @ResponseBody String session(HttpServletRequest httpServletRequest) {
		ServicesSession session = new ServicesSession(httpServletRequest.getSession(false));
		JsonElement sessionjson = gson.fromJson(session.toJson(), JsonElement.class);
		return Utilities.responseWrapper(ResponseStatus.success, sessionjson);
	}

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
			return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(repo));
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
	public @ResponseBody String repositoryDelete(@RequestParam(value="id", required=true) Integer id, Authentication authentication) throws Exception {
		User user = (User)authentication.getPrincipal();
		RepositoryDAL repodal = new RepositoryDAL();
		Repository repo = repodal.getRepository(id);
		repodal.delete(repo);
		return Utilities.responseWrapper(ResponseStatus.success, null);
	}
}