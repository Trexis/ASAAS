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
import net.trexis.asaas.web.service.model.RepositorySession;
import net.trexis.asaas.web.service.model.User;

public class SessionDAL extends BaseDAL {

	public List<RepositorySession> list() throws Exception {
        String sql = "SELECT * from sessions";
        return list(sql);
	}	

	public List<RepositorySession> list(int userId) throws Exception {
        String sql = "SELECT * from sessions s, repositories r where s.repositoryid = r.id and r.userid=" + userId;
        return list(sql);
	}	

	public List<RepositorySession> list(String sql) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        List<RepositorySession> listItems = jdbcTemplate.query(sql, rowMapper());
        return listItems;
    }
	
	public RepositorySession getSessions(int sessionId) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from sessions where id=" + sessionId + ";";
        try{
        	RepositorySession item = jdbcTemplate.queryForObject(sql, rowMapper());
	        item.setProperties(this.listProperties(item.getId(), ItemType.Session));
			return item;
        } catch(EmptyResultDataAccessException ex){
        	throw new ItemNotFoundException(ex);
        }
	}
	
	private RowMapper<RepositorySession> rowMapper(){
		return new RowMapper<RepositorySession>() {
 
            public RepositorySession mapRow(ResultSet rs, int rowNumber) throws SQLException {
            	RepositorySession session = new RepositorySession();
            	session.setId(rs.getInt("id"));
            	session.setKey(rs.getString("key"));
            	session.setRepositoryid(rs.getInt("repositoryid"));
                return session;
            }
             
        };
	}
}
