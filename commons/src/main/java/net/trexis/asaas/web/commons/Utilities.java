package net.trexis.asaas.web.commons;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Utilities {

	public static String dateTimeToISO(Long dateLong){
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		df.setTimeZone(tz);
		return df.format(new Date(dateLong));
	}
	
	public static String responseWrapper(ResponseStatus status, JsonElement jsonData){
		return responseWrapper(status, null, jsonData);
	}
	public static String responseWrapper(ResponseStatus status, String message, JsonElement jsonData){
		Gson gson = new Gson();
		JsonObject response = new JsonObject();
		response.addProperty("status", status.toString());
		response.addProperty("message", message);
		response.add("data", (jsonData!=null)?jsonData:new JsonObject());
		
		return gson.toJson(response);
	}
	
	public static String readFileContent(String path) throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, "utf-8");
	}
	
	public static String getBaseUrlFromRequest(HttpServletRequest request) throws MalformedURLException{
		URL url = new URL(request.getScheme(), 
		        request.getServerName(), 
		        request.getServerPort(), 
		        request.getContextPath());
		return url.toString();
	}
}
