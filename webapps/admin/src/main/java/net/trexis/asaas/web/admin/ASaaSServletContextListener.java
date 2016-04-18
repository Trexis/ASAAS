package net.trexis.asaas.web.admin;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.trexis.asaas.web.configuration.ASaaSProperties;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class ASaaSServletContextListener implements ServletContextListener 
{
	@Autowired
	ASaaSProperties asaasProperties;
	
	public void contextInitialized(ServletContextEvent contextEvent) 
	{ 
		try{
			if(asaasProperties==null) asaasProperties = new ASaaSProperties();
			String logbackConfigFile = asaasProperties.getString("logback.filepath"); 
			URL configFileURL;
			if (logbackConfigFile != null) 
			{ 
				configFileURL = new File(logbackConfigFile).toURI().toURL(); 
				if (configFileURL != null)
				{
					JoranConfigurator configurator = new JoranConfigurator(); 
					LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory(); 
					loggerContext.reset(); 
					configurator.setContext(loggerContext); 
					try { configurator.doConfigure(configFileURL); } 
					catch (JoranException ex) { throw new RuntimeException(ex); } 
				}
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
		
	} 
}