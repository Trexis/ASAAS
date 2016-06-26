package net.trexis.asaas.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import net.trexis.asaas.service.model.ProxyResponse;
import net.trexis.asaas.web.commons.HttpRequestMethod;

public class Proxy {

	//private HttpClient client;
	private Jsoup jsoup;
	
	private String baseurl = "";
	private String homeurl = "";
	private String loginurl = "";
	
	public Proxy(String baseUrl, String homeUrl, String loginUrl){
		this.baseurl = baseUrl;
		this.homeurl = homeUrl;
		this.loginurl = loginUrl;
	}
	
	public ProxyResponse goHome() throws Exception{
		return proxyExecute(HttpRequestMethod.GET.toString(), this.homeurl);
	}
	
	public ProxyResponse proxyExecute(String method, String url) throws Exception{
		String absoluteurl = makeAbsUrl(url);
		ProxyResponse response = new ProxyResponse(HttpRequestMethod.valueOf(method), baseurl, absoluteurl);
		Document document = Jsoup.connect(absoluteurl).method(Method.valueOf(method)).execute().parse();
		String html = document.html();
		response.setRawresponse(html);

		resolveUrls(document, "a[href]", "href");
		resolveUrls(document, "[src]", "src");
		resolveUrls(document, "link[href]", "href");
		html = document.html();
		html = cleanHtml(html);
		
		response.setCleanresponse(html);
		
		return response;
	}


	private String cleanHtml(String html) throws XPatherException{
		String cleanhtml = "";
		HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = new CleanerProperties();

		props.setAllowHtmlInsideAttributes(true);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitXmlDeclaration(true);
        
        PrettyHtmlSerializer serializer = new PrettyHtmlSerializer(props);
        
        TagNode node = cleaner.clean(html);
        Object[] content_nodes = node.evaluateXPath("");
        if (content_nodes.length > 0) {
        	for(int i=0; i<content_nodes.length; i++){
	            TagNode content_node = (TagNode) content_nodes[i];
	            cleanhtml += serializer.getAsString(content_node, "UTF-8", true);
        	}
        }
		
		return cleanhtml;
	}
	
	private void resolveUrls(Document document, String select, String attr) throws URISyntaxException{
		Elements links = document.select(select);
		for(Element element: links){
			URI uri = new URI(element.attr(attr));
			if(!uri.isAbsolute()){
				element.attr(attr, this.baseurl + uri);
			}
		}
		
	}

	private String makeAbsUrl(String url) throws URISyntaxException{
		URI uri = new URI(url);
		if(!uri.isAbsolute()){
			if(!this.homeurl.endsWith("/")) {
				uri = new URI(this.homeurl + "/" + url);
			} else {
				uri = new URI(this.homeurl + url);
			}
		}
		return uri.toString();
	}
	
	/*
	private String replaceLinks(String baseUrl, String content) throws URISyntaxException{
		String responsestring = replaceLinks(baseUrl, content, "(href|src|action|background)=\"[^\"]*\"", Character.toString('"')); 
		return replaceLinks(baseUrl, responsestring, "(href|src|action|background)='[^']*'", "'");
	}
	private String replaceLinks(String baseUrl, String content, String regex, String quote) throws URISyntaxException{
	    //absolute URI used for change all relative links
	    URI addressUri = new URI(baseUrl);
	    //finds all link atributes (href, src, etc.)
	    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	    Matcher m = pattern.matcher(content);
	    //determines if the link is allready absolute
	    Pattern absoluteLinkPattern = Pattern.compile("[a-z]+://.+");
	    //buffer for result saving
	    StringBuffer buffer = new StringBuffer();
	    //position from where should next interation take content to append to buffer
	    int lastEnd = 0;
	    while(m.find()){
	        //position of link in quotes
	        int startPos = content.indexOf(quote,m.start())+1;
	        int endPos = m.end()-1;
	        String link = content.substring(startPos,endPos);
	        Matcher absoluteMatcher = absoluteLinkPattern.matcher(link);
	        //is the link relative?
	        if(!absoluteMatcher.find())
	        {
	            //create relative URL
	            //URI tmpUri = addressUri.resolve(link);
	        	String newurl = baseUrl + link;
	            //append the string between links
	            buffer.append(content.substring(lastEnd,startPos-1));
	            //append new link
	            //buffer.append(tmpUri.toString());
	            buffer.append(newurl);
	            lastEnd =endPos+1;
	        }
	    }
	    //append the end of file
	    buffer.append(content.substring(lastEnd));
	    return buffer.toString();
	}
	*/
}
