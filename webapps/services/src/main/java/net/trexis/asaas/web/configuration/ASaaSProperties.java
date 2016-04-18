package net.trexis.asaas.web.configuration;

import javax.naming.InitialContext;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ASaaSProperties extends PropertiesConfiguration {

	static final Logger logger = LoggerFactory.getLogger(ASaaSProperties.class);
	
	public ASaaSProperties(){
		try{
            InitialContext initialContext = new InitialContext();
            String fileLocation = (String) initialContext.lookup("java:comp/env/asaas/config");
            this.load(fileLocation);
        }catch (Exception e){
            logger.error("Unable to load asaas.properties file", e);
        }
	}
}
