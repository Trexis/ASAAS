package net.trexis.asaas.web.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import net.trexis.asaas.web.commons.ItemType;
import net.trexis.asaas.web.model.Repository;
import net.trexis.asaas.web.model.User;

public class RepositoryDAL extends BaseDAL {

	public List<Repository> list() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from repositories";
        List<Repository> listItems = jdbcTemplate.query(sql, rowMapper());
        return listItems;
    }
	
	public Repository getRepository(int repositoryId) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT * from repositories where id=" + repositoryId + ";";
        Repository item = jdbcTemplate.queryForObject(sql, rowMapper());
        item.setProperties(this.listProperties(item.getId(), ItemType.Repository));
		return item;
	}
	
	private RowMapper<Repository> rowMapper(){
		return new RowMapper<Repository>() {
 
            public Repository mapRow(ResultSet rs, int rowNumber) throws SQLException {
            	Repository repository = new Repository();
            	repository.setId(rs.getInt("id"));
            	repository.setUserid(rs.getInt("userid"));
                return repository;
            }
             
        };
	}
}
