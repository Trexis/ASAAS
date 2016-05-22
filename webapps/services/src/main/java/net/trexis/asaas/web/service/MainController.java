package net.trexis.asaas.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.trexis.asaas.web.commons.ResponseStatus;
import net.trexis.asaas.web.commons.Utilities;
import net.trexis.asaas.web.service.dal.RepositoryDAL;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class MainController {
	
	private String pagetitle = "treXis Any Site as a Service :: Rest Services";

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
		return session.toJson();
	}

	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public @ResponseBody String user(Authentication authentication) {
		User user = (User)authentication.getPrincipal();
		return user.toJson();
	}

	@RequestMapping(value = { "/repository" }, method = RequestMethod.GET)
	public @ResponseBody String repositoryGet(@RequestParam(value="id", required=false) Integer id, Authentication authentication) throws Exception {
		Gson gson = new Gson();
		User user = (User)authentication.getPrincipal();
		RepositoryDAL repodal = new RepositoryDAL();
		if(id==null){
			List<Repository> repositories = new ArrayList<Repository>();
			if(user.getAuthority().getAuthority().equals("ROLE_ADMIN")){
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
		Gson gson = new Gson();
		User user = (User)authentication.getPrincipal();
		RepositoryDAL repodal = new RepositoryDAL();
		Repository repo = gson.fromJson(body, Repository.class);
		repo.setUserid(user.getId());
		repodal.update(repo);
		return Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(repo));
	}
}