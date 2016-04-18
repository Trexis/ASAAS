package net.trexis.asaas.web.dal;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jndi.JndiTemplate;

public class BaseDAL {

	public DataSource getDataSource() throws Exception{
		try{
			JndiTemplate jndiTemplate = new JndiTemplate();
			DataSource dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/asaasDS");		
			return dataSource;
		} catch(Exception ex){
			throw new Exception("Bad configuration of asaasDS JNDI in context.xml", ex);
		}
	}
}
