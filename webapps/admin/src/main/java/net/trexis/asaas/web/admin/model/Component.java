package net.trexis.asaas.web.admin.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.trexis.asaas.web.commons.ItemNotFoundException;
import net.trexis.asaas.web.commons.Utilities;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Component extends BaseModel {

	private String name;
	private String html;
	private HashMap<String,String> dependencies;
	
	public Component(String name) throws ItemNotFoundException, IOException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String componentpath = request.getServletContext().getRealPath("/static/components/" + name);
		
    	File dir = new File(componentpath);

    	//Get the HTML file
    	File[] htmlfiles = dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".html"); }
    	});
    	
    	//Throw exception if unable to find a html file
    	if(htmlfiles.length==0) throw new ItemNotFoundException("No html files found for component");
    	this.html = Utilities.readFileContent(htmlfiles[0].getAbsolutePath());
    	
    	//Get all other files to load 
    	File[] nothtmlfiles = dir.listFiles(new FilenameFilter() { 
	         public boolean accept(File dir, String filename)
	              { return !filename.endsWith(".html"); }
    	});
    	for(File file: nothtmlfiles){
    		String filename = file.getName();
    		this.addDependency(file.getPath(), filename.substring(filename.lastIndexOf(".")));
    	}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public HashMap<String, String> getDependencies() {
		return dependencies;
	}
	public void setDependencies(HashMap<String, String> dependencies) {
		this.dependencies = dependencies;
	}
	
	public void addDependency(String fileName, String fileType){
		dependencies.put(fileName, fileType);
	}
	

}
