package org.projectsample.simplest.s1.security.dao;

import java.sql.SQLException;
import java.util.List;

import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.s1.security.bean.Resource;

/**
 * Resource DAO.
 * 
 * @author Jason Xing
 */
public class ResourceDao {
	
    private Session session;
    private String sql;
    
    public ResourceDao(Session session) {
        this.session = session;
    }
    
    public Integer insert(Resource resource) throws SQLException {
        sql = "insert into resource (type, name, value) \n" +
              "values (?, ?, ?)                         \n";
        session.execute(sql, resource.getType(), resource.getName(), resource.getValue());
        sql = "select id from resource where type = ? and name = ?";
        return session.getValue(sql, resource.getType(), resource.getName());
    }
    
    public Resource get(Integer id) throws SQLException {
        sql = "select id, type, name, value from resource where id = ?";
        return session.get(Resource.class, sql, id);
    }
    
    public List<Resource> getByType(String type) throws SQLException {
        sql = "select id, type, name, value from resource where type = ?";
        return session.query(Resource.class, sql, type);
    }

}
