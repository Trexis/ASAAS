package net.trexis.asaas.web.service.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import net.trexis.asaas.web.commons.ItemNotFoundException;
import net.trexis.asaas.web.commons.ItemType;
import net.trexis.asaas.web.service.model.Repository;
import net.trexis.asaas.web.service.model.User;

public class UserDAL extends BaseDAL {
	
	public List<User> list() throws Exception{
        return list(true);
	}
	public List<User> list(boolean includeProperties) throws Exception{
        String sql = "SELECT * from users";
        return list(sql, includeProperties);
	}

	private List<User> list(String sql, boolean includeProperties) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        List<User> listItems = jdbcTemplate.query(sql, rowMapper());
        if(includeProperties){
        	for(User item: listItems){
        		item.setProperties(this.listProperties(item.getId(), ItemType.User));
        	}
        }
        return listItems;
    }
	
	public void delete(User item) throws Exception {
		deleteAllProperties(item.getId(), ItemType.User);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		String deletesql = "delete from users where id=" + item.getId();
		jdbcTemplate.execute(deletesql);
	}
	
	public void update(User item) throws Exception{
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		if(item.getId()==-1){
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String sql = "insert into users (username, password, authority, active) values (?, ?, ?, ?);";
			PreparedStatementCreator psc =  new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
		            ps.setString(1, item.getName());
		            ps.setString(2, item.getPassword());
		            ps.setString(3, item.getAuthority().getAuthority());
		            ps.setInt(4, item.getActive()?1:0);
		            return ps;
		        }
		    };
			jdbcTemplate.update(psc, keyHolder);
			item.setId(keyHolder.getKey().intValue());
		} else {
			//We don't update the userid, since we don't allow change in ownership
			String SQL = "update users set username=?, password=?, authority=?, active=? where id=?;";
			jdbcTemplate.update( SQL, item.getName(), item.getPassword(), item.getAuthority().getAuthority(), item.getActive()?1:0, item.getId());
		}
		this.updateProperties(item.getId(), ItemType.User, item.getProperties());
	}
	
	public User getUser(int userId) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from users where id=" + userId + ";";
        try{
        	User item = jdbcTemplate.queryForObject(sql, rowMapper());
	        item.setProperties(this.listProperties(item.getId(), ItemType.User));
			return item;
        } catch(EmptyResultDataAccessException ex){
        	throw new ItemNotFoundException(ex);
        }
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
