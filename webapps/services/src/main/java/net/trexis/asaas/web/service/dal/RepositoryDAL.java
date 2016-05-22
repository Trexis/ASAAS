package net.trexis.asaas.web.service.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

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

public class RepositoryDAL extends BaseDAL {

	public List<Repository> list() throws Exception{
        return list(true);
	}
	public List<Repository> list(boolean includeProperties) throws Exception{
        String sql = "SELECT * from repositories";
        return list(sql, includeProperties);
	}
	public List<Repository> list(int userId) throws Exception{
		return list(userId, true);
	}
	public List<Repository> list(int userId, boolean includeProperties) throws Exception{
        String sql = "SELECT * from repositories where userid=" + userId;
		return list(sql, true);
	}
	private List<Repository> list(String sql, boolean includeProperties) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        List<Repository> listItems = jdbcTemplate.query(sql, rowMapper());
        if(includeProperties){
        	for(Repository item: listItems){
        		item.setProperties(this.listProperties(item.getId(), ItemType.Repository));
        	}
        }
        return listItems;
    }
	
	public void delete(Repository item) throws Exception {
		deleteAllProperties(item.getId(), ItemType.Repository);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		String deletesql = "delete from repositories where id=" + item.getId();
		jdbcTemplate.execute(deletesql);
	}
	
	public void update(Repository item) throws Exception{
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		if(item.getId()==-1){
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String sql = "insert into repositories (userid, name) values (?, ?);";
			PreparedStatementCreator psc =  new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
		            ps.setInt(1, item.getUserid());
		            ps.setString(2, item.getName());
		            return ps;
		        }
		    };
			jdbcTemplate.update(psc, keyHolder);
			item.setId(keyHolder.getKey().intValue());
		} else {
			//We don't update the userid, since we don't allow change in ownership
			String SQL = "update repositories set name= ? where id=?;";
			jdbcTemplate.update( SQL, item.getName(), item.getId());
		}
		this.updateProperties(item.getId(), ItemType.Repository, item.getProperties());
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
