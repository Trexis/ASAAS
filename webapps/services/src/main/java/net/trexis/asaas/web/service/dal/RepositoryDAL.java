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

public class RepositoryDAL extends BaseDAL {

	public List<Repository> list() throws Exception{
        String sql = "SELECT * from repositories";
        return list(sql);
	}
	public List<Repository> list(int userId) throws Exception{
        String sql = "SELECT * from repositories where userid=" + userId;
		return list(sql);
	}
	private List<Repository> list(String sql) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        List<Repository> listItems = jdbcTemplate.query(sql, rowMapper());
        return listItems;
    }
	
	public void update(Repository item) throws Exception{
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		String SQL = "insert into repositories (userid, name) values (?, ?) ON DUPLICATE KEY UPDATE name=?";
		jdbcTemplate.update( SQL, item.getUserid(), item.getName(), item.getName());
	}
	
	public Repository getRepository(int repositoryId) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from repositories where id=" + repositoryId + ";";
        try{
        	Repository item = jdbcTemplate.queryForObject(sql, rowMapper());
	        item.setProperties(this.listProperties(item.getId(), ItemType.Repository));
			return item;
        } catch(EmptyResultDataAccessException ex){
        	throw new ItemNotFoundException(ex);
        }
	}
	
	private RowMapper<Repository> rowMapper(){
		return new RowMapper<Repository>() {
 
            public Repository mapRow(ResultSet rs, int rowNumber) throws SQLException {
            	Repository repository = new Repository();
            	repository.setId(rs.getInt("id"));
            	repository.setName(rs.getString("name"));
            	repository.setUserid(rs.getInt("userid"));
                return repository;
            }
             
        };
	}
}
