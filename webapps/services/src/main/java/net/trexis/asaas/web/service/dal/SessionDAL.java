package net.trexis.asaas.web.service.dal;

import java.sql.Connection;
import java.sql.Date;
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
import net.trexis.asaas.web.commons.Utilities;
import net.trexis.asaas.web.service.model.Repository;
import net.trexis.asaas.web.service.model.RepositorySession;
import net.trexis.asaas.web.service.model.User;

public class SessionDAL extends BaseDAL {

	public List<RepositorySession> list() throws Exception {
		return list(true);
	}
	public List<RepositorySession> list(boolean includeProperties) throws Exception {
        String sql = "SELECT * from sessions";
        return list(sql,includeProperties);
	}	

	public List<RepositorySession> list(int userId) throws Exception {
		return list(userId, true);
	}
	public List<RepositorySession> list(int userId, boolean includeProperties) throws Exception {
        String sql = "SELECT * from sessions s, repositories r where s.repositoryid = r.id and r.userid=" + userId;
        return list(sql,includeProperties);
	}	

	public List<RepositorySession> listByRepository(int repositoryId) throws Exception {
		return listByRepository(repositoryId, true);
	}
	public List<RepositorySession> listByRepository(int repositoryId, boolean includeProperties) throws Exception {
        String sql = "SELECT * from sessions s, repositories r where s.repositoryid = r.id and r.id=" + repositoryId;
        return list(sql,includeProperties);
	}	

	
	public List<RepositorySession> list(String sql, boolean includeProperties) throws Exception {
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
	
	public void delete(RepositorySession item) throws Exception {
		deleteAllProperties(item.getId(), ItemType.Session);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		String deletesql = "delete from sessions where id=" + item.getId();
		jdbcTemplate.execute(deletesql);
	}
	
	public void update(RepositorySession item) throws Exception{
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		if(item.getId()==-1){
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String sql = "insert into sessions (repositoryid, sessionkey, initdate, lastdate) values (?, ?, ?, ?);";
			PreparedStatementCreator psc =  new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
		            ps.setInt(1, item.getRepositoryid());
		            ps.setString(2, item.getKey());
		            ps.setDate(3, Utilities.utilDateToSQLDate(item.getInitdate()));
		            ps.setDate(4, Utilities.utilDateToSQLDate(item.getLastdate()));
		            return ps;
		        }
		    };
			jdbcTemplate.update(psc, keyHolder);
			item.setId(keyHolder.getKey().intValue());
		} else {
			//We don't update the session itself, just the last view date
			String SQL = "update repositories set lastdate=?, key=? where id=?;";
			jdbcTemplate.update( SQL, item.getLastdate(), item.getKey(), item.getId());
		}
		this.updateProperties(item.getId(), ItemType.Session, item.getProperties());
	}

	public RepositorySession getSession(int sessionId) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from repositories where id=" + sessionId + ";";
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
            	session.setKey(rs.getString("sessionkey"));
            	session.setRepositoryid(rs.getInt("repositoryid"));
            	session.setInitdate(rs.getDate("initdate"));
            	session.setLastdate(rs.getDate("lastdate"));
                return session;
            }
             
        };
	}
}
