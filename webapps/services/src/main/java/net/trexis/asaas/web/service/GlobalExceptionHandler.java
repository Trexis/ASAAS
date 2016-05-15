package net.trexis.asaas.web.service;


import javax.servlet.http.HttpServletRequest;

import net.trexis.asaas.web.commons.ResponseStatus;
import net.trexis.asaas.web.commons.Utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@ControllerAdvice
public class GlobalExceptionHandler {
 
	static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
     
    @ExceptionHandler(Exception.class)
    public @ResponseBody String handleException(HttpServletRequest request, Exception ex){
        logger.info("Exception Occured:: URL="+request.getRequestURL());
        Gson gson = new Gson();
        return Utilities.responseWrapper(ResponseStatus.error, ex.getMessage(), gson.toJsonTree(ex));
    }
    
    /*
    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occured")
    @ExceptionHandler(IOException.class)
    public void handleIOException(){
        logger.error("IOException handler executed");
        //returning 404 error code
    }
    */
}