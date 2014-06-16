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
import org.projectsample.simplest.s1.security.bean.Resource;
import org.projectsample.simplest.s1.security.dao.MenuDao;
import org.projectsample.simplest.s1.security.dao.ResourceDao;

/**
 * Resource Service.
 * 
 * @author Jason Xing
 */
public class ResourceService {

    /**
     * Add a menu.
     * 
     * @param menu a menu
     * 
     * @return the added menu ID
     */
    public Integer addMenu(Menu menu) {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            ResourceDao resourceDao = new ResourceDao(session);
            MenuDao menuDao = new MenuDao(session);
            Integer id = resourceDao.insert(menu);
            menu.setId(id);
            menuDao.insert(menu);
            session.commit();
            return id;
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    /**
     * Get children of a menu.
     * 
     * <p>
     * The children are listed in order.
     * 
     * @param menuId a menu ID
     * 
     * @return children of the menu
     */
     public List<Menu> getChildrenOfMenu(Integer menuId) {
         Session session = null;
         try {
             session = SessionFactory.openSession();
             return getChildrenOfMenu(session, menuId);
         } catch (SQLException e) {
             throw new DbException(e);
         } finally {
             session.close();
         }
    }

    /**
     * Get children of a menu.
     * 
     * <p>
     * The children are listed in order.
     * 
     * @param session a DB session
     * @param menuId a menu ID
     * 
     * @return children of the menu
     * 
     * @throws SQLException 
     */
     private List<Menu> getChildrenOfMenu(Session session, Integer menuId) throws SQLException {
         MenuDao menuDao = new MenuDao(session);
         return menuDao.getChildren(menuId);
    }

   /**
    * Get all menus.
    * 
    * <p>
    * All menus are listed in order.
    * 
    * @param session a DB session
    * 
    * @return all menus
    * 
    * @throws SQLException 
    */
    public List<Menu> getAllMenus(Session session) throws SQLException {
        List<Menu> menus = new ArrayList<Menu>();
        MenuDao menuDao = new MenuDao(session);
        Menu root = menuDao.getRoot();
        root.setLevel(0);
        menus.add(root);
        childrenOfMenu(session, root, menus);
        return menus;
    }

    /**
     * Get all menus.
     * 
     * <p>
     * The menus are listed in order.
     * 
     * @return all menus
     */
    public List<Menu> getAllMenus() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            return this.getAllMenus(session);
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

     /**
      * Add children of a menu to the menus.
      * 
      * <p>
      * This is a recursive function.
      * <p> 
      * The child property level will be set to (its parent's level plus one).
      * 
      * @param session a DB session
      * @param menu a menu
      * @param menus the menus
      * 
      * @throws SQLException 
      */
    private void childrenOfMenu(Session session, Menu menu, List<Menu> menus) throws SQLException {
        List<Menu> children = getChildrenOfMenu(menu.getId());
        if (children.size() > 0) {
            for (Menu child : children) {
                child.setLevel(menu.getLevel() + 1);
                menus.add(child);
                childrenOfMenu(session, child, menus);
            }
        } else {
            return;
        }
    }
    
    /**
     * Get all URLs.
     * 
     * @return all URLs
     */
    public Set<String> getAllUrls() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            ResourceDao resourceDao = new ResourceDao(session);
            Set<String> urls = new HashSet<String>();
            List<Resource> resources = resourceDao.getByType("URL");
            for (Resource resource : resources) {
                urls.add(resource.getValue());
            }
            return urls;
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    /**
     * Is a valid URL?.
     * 
     * @param url An URL
     * 
     * @return true if the URL is registered, otherwise false.
     */
    public boolean isValidUrl(String url) {
        Set<String> urls = this.getAllUrls();
        return urls.contains(url);
    }

}
