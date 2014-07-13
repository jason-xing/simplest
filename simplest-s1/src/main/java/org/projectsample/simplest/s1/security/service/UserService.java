package org.projectsample.simplest.s1.security.service;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.dbutils.SessionFactory;
import org.projectsample.simplest.s1.exception.DbException;
import org.projectsample.simplest.s1.security.bean.User;
import org.projectsample.simplest.s1.security.bean.Role;
import org.projectsample.simplest.s1.security.dao.RoleDao;
import org.projectsample.simplest.s1.security.dao.UserDao;
import org.projectsample.simplest.s1.security.exception.EmailRegisteredException;
import org.projectsample.simplest.s1.security.exception.PasswordNotCorrectException;
import org.projectsample.simplest.s1.security.exception.UsernameExistsException;
import org.projectsample.simplest.s1.security.exception.UsernameNotExistException;
import org.projectsample.simplest.s1.security.utils.MD5;

/**
 * User Service.
 * 
 * @author Jason Xing
 */
public class UserService {
    
    private RoleService roleService = new RoleService();

    /**
     * Add an user.
     * 
     * <p>
     * Precondition: <br>
     * the username is not empty; <br>
     * the password is not empty;
     * 
     * @param user an user
     * 
     * @return the added user ID
     * 
     * @throws UsernameExistsException if the username exists already 
     * @throws EmailRegisteredException if the email has been already registered
     */
    public Integer add(User user) throws UsernameExistsException, EmailRegisteredException {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            UserDao userDao = new UserDao(session);
            User userByUsername = userDao.getByUsername(user.getUsername());
            if (userByUsername != null) {
                throw new UsernameExistsException();
            }
            String email = user.getEmail();
            if (!"".equals(email)) {
                User userByEmail = userDao.getByEmail(email);
                if (userByEmail != null) {
                    throw new EmailRegisteredException();
                }
            }
            String passwordEnc = MD5.encodeString(user.getPassword(), null);
            user.setPasswordEnc(passwordEnc);
            Integer id = userDao.insert(user);
            RoleDao roleDao = new RoleDao(session);
            userDao.bindRole(id, roleDao.get("CommonRole").getId());
            session.commit();
            return id;
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    /**
     * Authenticate an user.
     * 
     * <p>
     * Precondition: <br>
     * the username is not empty; <br>
     * the password is not empty;
     * 
     * @param username the username of an user
     * @param password the password of an user
     * 
     * @return the authenticated user ID
     * 
     * @throws UsernameNotExistException if the username is not found
     * @throws PasswordNotCorrectException if the password is not correct
     */
    public User authenticate(String username, String password) 
            throws UsernameNotExistException, PasswordNotCorrectException {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            UserDao userDao = new UserDao(session);
            User user = userDao.getByUsername(username);
            if (user == null) {
                throw new UsernameNotExistException();
            }
            String passwordEnc = MD5.encodeString(password, null);
            if (!passwordEnc.equals(user.getPasswordEnc())) {
                throw new PasswordNotCorrectException();
            }
            return user;
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
    /**
     * Get roles of an user.
     * 
     * @param userId an user ID
     * 
     * @return roles of the user
     */
    public Set<Role> getRoles(Integer userId) {
        Set<Role> roles = new HashSet<Role>();
        Session session = null;
        try {
            session = SessionFactory.openSession();
            UserDao userDao = new UserDao(session);
            List<Role> roleList = userDao.getRoles(userId);
            for (Role role : roleList) {
                roles.add(role);
                roleService.parent(session, role.getId(), roles);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
        return roles;
    }

    /**
     * modify an user.
     * 
     * <p>
     * If the password is empty, it won't modify the password.<br>
     * Precondition: <br>
     * the username is not empty;
     * 
     * @param user an user
     * 
     * @throws EmailRegisteredException if the email has been already registered
     */
    public User modify(User user) throws EmailRegisteredException {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            UserDao userDao = new UserDao(session);
            // Fetch the user from database by username
            User userFromDb = userDao.getByUsername(user.getUsername());
            String emailFromDb = userFromDb.getEmail();
            String email = user.getEmail();
            // The email is modified.
            if (!emailFromDb.equals(email)) {
                if (!"".equals(email)) {
                    User userByEmail = userDao.getByEmail(email);
                    if (userByEmail != null) {
                        throw new EmailRegisteredException();
                    }
                }
            }
            userFromDb.setEmail(email);
            String password = user.getPassword();
            // If the password is not empty, it will modify the password, otherwise not.
            if (!password.equals("")) {
                userFromDb.setPasswordEnc(MD5.encodeString(password, null));
            }
            userDao.update(userFromDb);
            session.commit();
            return userFromDb;
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

}
