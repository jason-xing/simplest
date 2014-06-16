package org.projectsample.simplest.s1.security.dao;

import java.sql.SQLException;
import java.util.List;

import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.s1.security.bean.Menu;

/**
 * Menu DAO.
 * 
 * @author Jason Xing
 */
public class MenuDao {
	
    private Session session;
    private String sql;
    
    public MenuDao(Session session) {
        this.session = session;
    }
    
    public Integer insert(Menu menu) throws SQLException {
        sql = "insert into menu (id, parent_id, \"order\", is_leaf) \n" +
              "values (?, ?, ?, ?)                                  \n";
        session.execute(sql, menu.getId(), menu.getParentId(), menu.getOrder(), menu.getIsLeaf());
        return menu.getId();
    }
    
    public Menu get(Integer id) throws SQLException {
        sql = "select                       \n" +
              "    r.id,                    \n" +
              "    r.type,                  \n" +
              "    r.name,                  \n" +
              "    r.value,                 \n" +
              "    m.parent_id as parentid, \n" +
              "    m.\"order\",             \n" +
              "    m.is_leaf as isleaf      \n" +
              "from                         \n" +
              "    resource r,              \n" +
              "    menu m                   \n" +
              "where                        \n" +
              "    r.id = m.id              \n" +
              "    and m.id = ?             \n";
        return session.get(Menu.class, sql, id);
    }

    public Menu getRoot() throws SQLException {
        sql = "select                       \n" +
              "    r.id,                    \n" +
              "    r.type,                  \n" +
              "    r.name,                  \n" +
              "    r.value,                 \n" +
              "    m.parent_id as parentid, \n" +
              "    m.\"order\",             \n" +
              "    m.is_leaf as isleaf      \n" +
              "from                         \n" +
              "    resource r,              \n" +
              "    menu m                   \n" +
              "where                        \n" +
              "    r.id = m.id              \n" +
    	      "    and m.parent_id is null  \n";
        return session.get(Menu.class, sql);
    }
    
    public List<Menu> getChildren(Integer id) throws SQLException {
        sql = "select                       \n" +
              "    r.id,                    \n" +
              "    r.type,                  \n" +
              "    r.name,                  \n" +
              "    r.value,                 \n" +
              "    m.parent_id as parentid, \n" +
              "    m.\"order\",             \n" +
              "    m.is_leaf as isleaf      \n" +
              "from                         \n" +
              "    resource r,              \n" +
              "    menu m                   \n" +
              "where                        \n" +
              "    r.id = m.id              \n" +
              "    and m.parent_id = ?      \n" +
              "order by                     \n" +
              "    m.\"order\"              \n";
        return session.query(Menu.class, sql, id);
    }
    
}
