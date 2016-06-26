package net.trexis.asaas.web.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.trexis.asaas.web.admin.model.Component;
import net.trexis.asaas.web.admin.proxy.ServicesProxy;
import net.trexis.asaas.web.commons.ItemNotFoundException;
import net.trexis.asaas.web.commons.ResponseStatus;
import net.trexis.asaas.web.commons.Utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

	@RequestMapping(value = "/services/**",headers = {"content-type=application/json"}, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> serviceGet(HttpServletRequest request) {
		Gson gson = new Gson();
		try{
			String serviceurl = getServiceURLFromRequest(request);
			ServicesProxy servicesProxy = new ServicesProxy();
			JsonObject jsonobject = gson.fromJson(servicesProxy.restGet(serviceurl), JsonObject.class);
			if(jsonobject.get("status").getAsString().equals("success")){
				return new ResponseEntity<String>(jsonobject.toString(),HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(jsonobject.toString(),HttpStatus.FAILED_DEPENDENCY);
			}
		} catch(Exception ex){
			String jsonresponse = Utilities.responseWrapper(ResponseStatus.error, ex.getMessage(), gson.toJsonTree(ex));
			return new ResponseEntity<String>(jsonresponse,HttpStatus.FAILED_DEPENDENCY);
		}
		
	}

	@RequestMapping(value = "/serviceshtml/**",headers = {"content-type=text/html"}, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> serviceGetHtml(HttpServletRequest request) {
		Gson gson = new Gson();
		try{
			String serviceurl = getServiceURLFromRequest(request);
			ServicesProxy servicesProxy = new ServicesProxy();
			return new ResponseEntity<String>(servicesProxy.restGet(serviceurl),HttpStatus.OK);
		} catch(Exception ex){
			String jsonresponse = Utilities.responseWrapper(ResponseStatus.error, ex.getMessage(), gson.toJsonTree(ex));
			return new ResponseEntity<String>(jsonresponse,HttpStatus.FAILED_DEPENDENCY);
		}
		
	}
	
	@RequestMapping(value = "/services/**",headers = {"content-type=application/json"}, method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> serviceDelete(HttpServletRequest request) {
		Gson gson = new Gson();
		try{
			String serviceurl = getServiceURLFromRequest(request);
			ServicesProxy servicesProxy = new ServicesProxy();
			JsonObject jsonobject = gson.fromJson(servicesProxy.restDelete(serviceurl), JsonObject.class);
			if(jsonobject.get("status").getAsString().equals("success")){
				return new ResponseEntity<String>(jsonobject.toString(),HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(jsonobject.toString(),HttpStatus.FAILED_DEPENDENCY);
			}
		} catch(Exception ex){
			String jsonresponse = Utilities.responseWrapper(ResponseStatus.error, ex.getMessage(), gson.toJsonTree(ex));
			return new ResponseEntity<String>(jsonresponse,HttpStatus.FAILED_DEPENDENCY);
		}
		
	}
	@RequestMapping(value = "/services/**",headers = {"content-type=application/json"}, method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> servicePost(@RequestBody String body, HttpServletRequest request) {
		Gson gson = new Gson();
		try{
			String serviceurl = getServiceURLFromRequest(request);
			ServicesProxy servicesProxy = new ServicesProxy();
			JsonObject jsonobject = gson.fromJson(servicesProxy.restPost(serviceurl, body), JsonObject.class);
			if(jsonobject.get("status").getAsString().equals("success")){
				return new ResponseEntity<String>(jsonobject.toString(),HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(jsonobject.toString(),HttpStatus.FAILED_DEPENDENCY);
			}
		} catch(Exception ex){
			String jsonresponse = Utilities.responseWrapper(ResponseStatus.error, ex.getMessage(), gson.toJsonTree(ex));
			return new ResponseEntity<String>(jsonresponse,HttpStatus.FAILED_DEPENDENCY);
		}
		
	}

	private MultiValueMap<String,String[]> convertParameterMap(Map<String,String[]> requestMap){
		MultiValueMap<String,String[]> response = new LinkedMultiValueMap<String, String[]>();
		for(String key: requestMap.keySet()){
			response.add(key, requestMap.get(key));
		}
		return response;
	}
	
	private String getServiceURLFromRequest(HttpServletRequest request){	
		String requestURL = request.getRequestURI();
		String queryString = request.getQueryString();
		String response = "";
		if (queryString == null) {
			response = requestURL.toString();
		} else {
			response = requestURL += "?" + queryString;
		}		
		response = response.substring(request.getContextPath().length() + "services/".length());
		return response;
	}

}