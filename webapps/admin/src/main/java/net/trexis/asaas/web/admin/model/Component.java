package net.trexis.asaas.web.admin.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.trexis.asaas.web.commons.ItemNotFoundException;
import net.trexis.asaas.web.commons.Utilities;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Component extends BaseModel {

	private String name;
	private String html;
	private HashMap<String,String> dependencies = new HashMap<String,String>();
	
	public Component(String name) throws ItemNotFoundException, IOException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String relativepath = "/statics/components/" + name;
		String componentpath = request.getServletContext().getRealPath(relativepath);
		
    	File dir = new File(componentpath);
    	if(!dir.exists()) throw new ItemNotFoundException("Components directory not found");
    	
    	//Get the HTML file
    	File[] htmlfiles = dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".html"); }
    	});
    	
    	//Throw exception if unable to find a html file
    	if(htmlfiles==null || htmlfiles.length==0) throw new ItemNotFoundException("No html files found for component");
    	this.html = Utilities.readFileContent(htmlfiles[0].getAbsolutePath());
    	
    	//Get all other files to load 
    	File[] nothtmlfiles = dir.listFiles(new FilenameFilter() { 
	         public boolean accept(File dir, String filename)
	              { return !filename.endsWith(".html"); }
    	});
    	for(File file: nothtmlfiles){
    		String filename = file.getName();
    		this.addDependency(relativepath + "/" + filename, filename.substring(filename.lastIndexOf(".")+1));
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
