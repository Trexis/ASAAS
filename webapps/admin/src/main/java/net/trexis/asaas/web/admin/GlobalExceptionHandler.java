package net.trexis.asaas.web.admin;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@ControllerAdvice
public class GlobalExceptionHandler {
 
	static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	private String pagetitle = "treXis Any Site as a Service";
     
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception ex){
        logger.info("Exception Occured:: URL="+request.getRequestURL());
		ModelAndView model = new ModelAndView();
		model.addObject("title", pagetitle + " :: Exception");
		model.addObject("message", ex.getMessage());
		model.addObject("cause", ex);
		model.setViewName("exception");
		return model;
    }
    
}