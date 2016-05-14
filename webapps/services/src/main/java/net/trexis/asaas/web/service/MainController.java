package net.trexis.asaas.web.service;

import javax.servlet.http.HttpServletRequest;

import net.trexis.asaas.web.model.ServicesSession;
import net.trexis.asaas.web.model.User;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

}