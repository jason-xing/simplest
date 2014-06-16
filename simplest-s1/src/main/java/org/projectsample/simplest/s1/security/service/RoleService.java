package org.projectsample.simplest.s1.security.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.dbutils.SessionFactory;
import org.projectsample.simplest.s1.exception.DbException;
import org.projectsample.simplest.s1.security.bean.Menu;
import org.projectsample.simplest.s1.security.bean.Permission;
import org.projectsample.simplest.s1.security.bean.Resource;
import org.projectsample.simplest.s1.security.bean.Role;
import org.projectsample.simplest.s1.security.dao.ResourceDao;
import org.projectsample.simplest.s1.security.dao.RoleDao;

/**
 * Role Service.
 * 
 * @author Jason Xing
 */
public class RoleService {
        
    private ResourceService menuService = new ResourceService();
    
    /**
     * Get the role Administrator
     * 
     * @return the role Administrator
     */
    public Role getAdministrator() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            RoleDao roleDao = new RoleDao(session);
            return roleDao.get("Administrator");
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    /**
     * Add the parent of a role to the roles.
     * 
     * <p>
     * This is a recursive function.
     * 
     * @param session a DB session
     * @param roleId a role ID
     * @param roles the roles
     * 
     * @throws SQLException 
     */
    public void parent(Session session, Integer roleId, Set<Role> roles) throws SQLException {
        RoleDao roleDao = new RoleDao(session);
        Role parent = roleDao.getParent(roleId);
        if (parent != null) {
            roles.add(parent);
            parent(session, parent.getId(), roles);
        } else {
            return;
        }
    }

    /**
     * Get menus of the roles.
     * 
     * <p>
     * The menus are listed in order.
     * 
     * @param roles the roles
     * 
     * @return menus of the roles
     */
    public List<Menu> getMenus(Set<Role> roles) {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // All menus are listed in order.
            List<Menu> allMenus = menuService.getAllMenus(session);
            // Set type can remove the repetitive menu ID.
            Set<Integer> menuIdsOfRoles = new HashSet<Integer>();
            RoleDao roleDao = new RoleDao(session);
            for (Role role : roles) {
                List<Permission> permissions = roleDao.getPermissions(role.getId(), "MENU");
                for (Permission permission : permissions) {
                    menuIdsOfRoles.add(permission.getResourceId());
                }
            }
            List<Menu> menus = new ArrayList<Menu>();
            for (Menu menu : allMenus) {
                if (menuIdsOfRoles.contains(menu.getId())) {
                    menus.add(menu);
                }
            }
            return menus;
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    /**
     * Get the URLs of the roles.
     * 
     * @param roles the roles
     * 
     * @return the URLs of the roles
     */
    public Set<String> getUrls(Set<Role> roles) {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            RoleDao roleDao = new RoleDao(session);
            ResourceDao resourceDao = new ResourceDao(session);
            Set<String> urls = new HashSet<String>();
            for (Role role : roles) {
                List<Permission> permissions = roleDao.getPermissions(role.getId(), "URL");
                for (Permission permission : permissions) {
                    Resource resource = resourceDao.get(permission.getResourceId());
                    urls.add(resource.getValue());
                }
            }
            return urls;
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
    /**
     * Get the URLs of the role Anonymous.
     * 
     * @return the URLs of the role Anonymous
     */
    public Set<String> getUrlsOfAnonymous() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            RoleDao roleDao = new RoleDao(session);
            Role anonymous = roleDao.get("Anonymous");
            ResourceDao resourceDao = new ResourceDao(session);
            List<Permission> permissions = roleDao.getPermissions(anonymous.getId(), "URL");
            Set<String> urls = new HashSet<String>();
            for (Permission permission : permissions) {
                Resource resource = resourceDao.get(permission.getResourceId());
                urls.add(resource.getValue());
            }
            return urls;
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

}
