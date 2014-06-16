package org.projectsample.simplest.s1.security.dao;

import java.sql.SQLException;
import java.util.List;

import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.s1.security.bean.Permission;
import org.projectsample.simplest.s1.security.bean.Role;

/**
 * Role DAO.
 * 
 * @author Jason Xing
 */
public class RoleDao {
	
    private Session session;
    private String sql;
    
    public RoleDao(Session session) {
        this.session = session;
    }
    
    public Role get(Integer id) throws SQLException {
        sql = "select id, name, parent_id as parentId from role where id = ?";
        return session.get(Role.class, sql, id);
    }
    
    public Role get(String name) throws SQLException {
        sql = "select id, name, parent_id as parentId from role where name = ?";
        return session.get(Role.class, sql, name);
    }
    
    public Role getParent(Integer id) throws SQLException {
        sql = "select                           \n" +
              "    parent.id,                   \n" +
              "    parent.name,                 \n" +
              "    parent.parent_id as parentId \n" +
              "from                             \n" +
              "    role r,                      \n" +
              "    role parent                  \n" +
              "where                            \n" +
              "    r.parent_id = parent.id      \n" +
              "    and r.id = ?                 \n";
        return session.get(Role.class, sql, id);
    }
    
    public List<Permission> getPermissions(Integer id, String resourceType) throws SQLException {
        sql = "select                           \n" +
              "    p.id,                        \n" +
              "    p.name,                      \n" +
              "    p.resource_id as resourceId, \n" +
              "    p.operation                  \n" +
              "from                             \n" +
              "    role_permission rp,          \n" +
              "    permission p,                \n" +
              "    resource rc                  \n" +
              "where                            \n" +
              "    rp.permission_id = p.id      \n" +
              "    and p.resource_id = rc.id    \n" +
              "    and rc.type = ?              \n" +
              "    and rp.role_id = ?           \n";
        return session.query(Permission.class, sql, resourceType, id);
    }

}
