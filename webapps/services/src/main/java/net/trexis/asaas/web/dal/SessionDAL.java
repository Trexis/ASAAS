package net.trexis.asaas.web.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import net.trexis.asaas.web.commons.ItemType;
import net.trexis.asaas.web.model.Repository;
import net.trexis.asaas.web.model.RepositorySession;
import net.trexis.asaas.web.model.User;

public class SessionDAL extends BaseDAL {

	public List<RepositorySession> list() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from sessions";
        List<RepositorySession> listItems = jdbcTemplate.query(sql, rowMapper());
        return listItems;
    }
	
	public RepositorySession getSessions(int sessionId) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from sessions where id=" + sessionId + ";";
        RepositorySession item = jdbcTemplate.queryForObject(sql, rowMapper());
        item.setProperties(this.listProperties(item.getId(), ItemType.Session));
		return item;
	}
	
	private RowMapper<RepositorySession> rowMapper(){
		return new RowMapper<RepositorySession>() {
 
            public RepositorySession mapRow(ResultSet rs, int rowNumber) throws SQLException {
            	RepositorySession session = new RepositorySession();
            	session.setId(rs.getInt("id"));
            	session.setRepositoryid(rs.getInt("repositoryid"));
                return session;
            }
             
        };
	}
}
