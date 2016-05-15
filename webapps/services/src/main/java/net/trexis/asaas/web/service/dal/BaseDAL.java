package net.trexis.asaas.web.service.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.trexis.asaas.web.commons.ItemType;
import net.trexis.asaas.web.service.model.Property;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
	
	public List<Property> listProperties(int itemId, ItemType itemType) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from properties where itemid=" + itemId + " and itemtype='" + itemType.toString() + "';";
        List<Property> listitems = jdbcTemplate.query(sql, propertyRowMapper());
        return listitems;
    }
	
	private RowMapper<Property> propertyRowMapper(){
		return new RowMapper<Property>() {
 
            public Property mapRow(ResultSet rs, int rowNumber) throws SQLException {
                Property property = new Property(rs.getString("name"), rs.getString("value"));
                return property;
            }
             
        };
	}
}
