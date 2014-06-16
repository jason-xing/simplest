package org.projectsample.simplest.s1.security.dao;

import java.sql.SQLException;

import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.s1.security.bean.Permission;

/**
 * Permission DAO.
 * 
 * @author Jason Xing
 */
public class PermissionDao {
	
    private Session session;
    private String sql;
    
    public PermissionDao(Session session) {
        this.session = session;
    }
    
    public Integer insert(Permission permission) throws SQLException {
        sql = "insert into permission (name, resource_id, operation) \n" +
              "values (?, ?, ?)                                      \n";
        session.execute(sql, permission.getName(), permission.getResourceId(), permission.getOperation());
        sql = "select id from permission where resource_id = ? and operation = ?";
        return session.getValue(sql, permission.getResourceId(), permission.getOperation());
    }
    
}
