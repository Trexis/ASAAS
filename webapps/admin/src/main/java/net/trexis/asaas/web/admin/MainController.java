package net.trexis.asaas.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.trexis.asaas.web.admin.model.Component;
import net.trexis.asaas.web.commons.ItemNotFoundException;
import net.trexis.asaas.web.commons.ResponseStatus;
import net.trexis.asaas.web.commons.Utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class MainController {
	
	private String pagetitle = "treXis Any Site as a Service";

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", pagetitle);
		model.setViewName("index");
		return model;

	}

	@RequestMapping(value = { "/login**" }, method = RequestMethod.GET)
	public ModelAndView loginPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", pagetitle + " :: Login");
		model.setViewName("login");
		return model;

	}
	@RequestMapping(value = { "/subscribe**" }, method = RequestMethod.GET)
	public ModelAndView subscribePage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", pagetitle + " :: Subscribe");
		model.setViewName("subscribe");
		return model;

	}
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
	
	
	@RequestMapping(value = "/dashboard**", method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", pagetitle + " :: Dashboard");
		model.setViewName("dashboard");

		return model;

	}

	@RequestMapping(value = "/component/{componentName}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> component(@PathVariable String componentName) {
		Gson gson = new Gson();
		try{
			Component component = new Component(componentName);
			String jsonresponse = Utilities.responseWrapper(ResponseStatus.success, gson.toJsonTree(component));
			return new ResponseEntity<String>(jsonresponse,HttpStatus.OK);
		} catch(Exception ex){
			String jsonresponse = Utilities.responseWrapper(ResponseStatus.error, ex.getMessage(), gson.toJsonTree(ex));
			return new ResponseEntity<String>(jsonresponse,HttpStatus.EXPECTATION_FAILED);
		}
	}
	
}