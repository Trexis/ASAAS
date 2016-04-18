package net.trexis.asaas.web.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import net.trexis.asaas.web.security.User;

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
        User user = jdbcTemplate.queryForObject(sql, rowMapper());
		return user;
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
