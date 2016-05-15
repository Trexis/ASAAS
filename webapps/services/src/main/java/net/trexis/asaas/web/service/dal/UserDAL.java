package net.trexis.asaas.web.service.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import net.trexis.asaas.web.commons.ItemNotFoundException;
import net.trexis.asaas.web.commons.ItemType;
import net.trexis.asaas.web.service.model.Repository;
import net.trexis.asaas.web.service.model.User;

public class UserDAL extends BaseDAL {

	public List<User> list() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from users";
        List<User> listUser = jdbcTemplate.query(sql, rowMapper());
        return listUser;
    }
	
	public User getUser(String username) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from users where username='" + username + "';";
        try{
        	User item = jdbcTemplate.queryForObject(sql, rowMapper());
	        item.setProperties(this.listProperties(item.getId(), ItemType.User));
			return item;
        } catch(EmptyResultDataAccessException ex){
        	throw new ItemNotFoundException(ex);
        }
	}
	
	private RowMapper<User> rowMapper(){
		return new RowMapper<User>() {
 
            public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setActive(rs.getInt("active")==1);
                user.setAuthority(new SimpleGrantedAuthority(rs.getString("authority")));
                return user;
            }
             
        };
	}
}
