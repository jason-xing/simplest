package org.projectsample.simplest.s1.security.dao;

import java.sql.SQLException;
import java.util.List;

import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.s1.security.bean.Role;
import org.projectsample.simplest.s1.security.bean.User;

/**
 * User DAO.
 * 
 * @author Jason Xing
 */
public class UserDao {
	
    private Session session;
    private String sql;
    
    public UserDao(Session session) {
        this.session = session;
    }
    
    public Integer insert(User user) throws SQLException {
        sql = "insert into \"user\" (username, password_enc, email) \n" +
        	  "values (?, ?, ?)                                     \n";
        session.execute(sql, user.getUsername(), user.getPasswordEnc(), user.getEmail());
        // Postgresql supports the followings.
        // sql = "select currval('user_id_seq')";
        // return ((Long)session.getValue(sql)).intValue();
        // Because username is null and unique, derby supports the followings.
        sql = "select id from \"user\" where username = ?";
        return session.getValue(sql, user.getUsername());
    }

    public void delete(Integer id) throws SQLException {
        sql = "delete from \"user\" where id = ?";
        session.execute(sql, id);
    }

    public void update(User user) throws SQLException {
        sql = "update                \n" +
              "    \"user\"          \n" +
              "set                   \n" +
              "    username = ?,     \n" +
              "    password_enc = ?, \n" +
              "    email = ?         \n" +
              "where                 \n" +
              "    id = ?            \n";
        session.execute(sql, user.getUsername(), user.getPasswordEnc(), user.getEmail(), user.getId());
    }

    public User get(Integer id) throws SQLException {
        sql = "select                           \n" +
              "    id,                          \n" +
              "    username,                    \n" +
              "    password_enc as passwordEnc, \n" +
              "    email                        \n" +
              "from                             \n" +
              "    \"user\"                     \n" +
              "where                            \n" +
              "    id = ?                       \n";
        return session.get(User.class, sql, id);
    }
    
    public List<User> get() throws SQLException {
        sql = "select                           \n" +
              "    id,                          \n" +
              "    username,                    \n" +
              "    password_enc as passwordEnc, \n" +
              "    email                        \n" +
              "from                             \n" +
              "    \"user\"                     \n";
        return session.query(User.class, sql);
    }
    
    public User getByUsername(String username) throws SQLException {
        sql = "select                           \n" +
              "    id,                          \n" +
              "    username,                    \n" +
              "    password_enc as passwordEnc, \n" +
              "    email                        \n" +
              "from                             \n" +
              "    \"user\"                     \n" +
              "where                            \n" +
              "    username = ?                 \n";
        return session.get(User.class, sql, username);
    }
    
    public User getByEmail(String email) throws SQLException {
        sql = "select                           \n" +
              "    id,                          \n" +
              "    username,                    \n" +
              "    password_enc as passwordEnc, \n" +
              "    email                        \n" +
              "from                             \n" +
              "    \"user\"                     \n" +
              "where                            \n" +
              "    email = ?                    \n";
        return session.get(User.class, sql, email);
    }
    
    public List<Role> getRoles(Integer id) throws SQLException {
        sql = "select                      \n" +
              "    r.id,                   \n" +
              "    r.name,                 \n" +
              "    r.parent_id as parentId \n" +
              "from                        \n" +
              "    user_role ur,           \n" +
              "    role r                  \n" +
              "where                       \n" +
              "    ur.role_id = r.id       \n" +
              "    and ur.user_id = ?      \n";
        return session.query(Role.class, sql, id);
    }
    
    public void bindRole(Integer id, Integer roleId) throws SQLException {
        sql = "insert into user_role (user_id, role_id) values (?, ?)";
        session.execute(sql, id, roleId);
    }

}
