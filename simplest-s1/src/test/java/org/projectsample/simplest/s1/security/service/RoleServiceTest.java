/*
 * Copyright 2011-2012 The ProjectSample Organization
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projectsample.simplest.s1.security.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.dbutils.SessionFactory;
import org.projectsample.simplest.s1.exception.DbException;
import org.projectsample.simplest.s1.security.bean.Menu;
import org.projectsample.simplest.s1.security.bean.Role;
import org.projectsample.simplest.s1.security.data.Destroy;
import org.projectsample.simplest.s1.security.data.Initialize;

public class RoleServiceTest {

    private RoleService roleService = new RoleService(); 
    
    private Integer userRoleId = null;
    private Integer employeeRoleId = null;
    private Integer developerRoleId = null;
    private Integer salesmanRoleId = null;
    private Integer customerRoleId = null;
        
    @Before 
    public void setup() throws Exception {
        new Initialize().createTables();
        this.initData();
    }
    
    /*
     * Roles:<br>
     * User<br>
     * ----Employee<br>
     * --------Developer<br>
     * --------Salesman<br>
     * ----Customer<br>
     * 
     * <p>
     * Menus:<br>
     * Root(User)<br>
     * ----Menu1(User)<br>
     * --------Menu11(User)<br>
     * --------Menu12(Developer)<br>
     * ----Menu2(Customer)<br>
     * --------Menu21(Customer)<br>
     * ------------Menu221(Customer)<br>
     */
    private void initData() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            
            // Role
            String ROLE_MAX_ID_SQL = "select max(id) from role";
            // User Role
            String ROLE_INSERT_SQL = "insert into role (name, parent_id) values (?, ?)";
            session.execute(ROLE_INSERT_SQL, "User", null);
            userRoleId = session.getValue(ROLE_MAX_ID_SQL);
            // Employee Role
            session.execute(ROLE_INSERT_SQL, "Employee", userRoleId);
            employeeRoleId = session.getValue(ROLE_MAX_ID_SQL);
            // Developer Role
            session.execute(ROLE_INSERT_SQL, "Developer", employeeRoleId);
            developerRoleId = session.getValue(ROLE_MAX_ID_SQL);
            // Salesman Role
            session.execute(ROLE_INSERT_SQL, "Salesman", employeeRoleId);
            salesmanRoleId = session.getValue(ROLE_MAX_ID_SQL);
            // Customer Role
            session.execute(ROLE_INSERT_SQL, "Customer", userRoleId);
            customerRoleId = session.getValue(ROLE_MAX_ID_SQL);
            
            // Resource, Menu, Permission
            String RESOURCE_INSERT_SQL = "insert into resource (type, name, value) values ('MENU', ?, ?)";
            String RESOURCE_MAX_ID_SQL = "select max(id) from resource";
            String MENU_INSERT_SQL = "insert into menu (id, parent_id, \"order\", is_leaf) values (?, ?, ?, ?)";
            String PERMISSION_INSERT_SQL = "insert into permission (name, resource_id, operation) values (?, ?, 'DISPLAY')";
            String PERMISSION_MAX_ID_SQL = "select max(id) from permission";
            // Menu Root
            session.execute(RESOURCE_INSERT_SQL, "Root", null);
            Integer rootId = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, rootId, null, 1, false);
            session.execute(PERMISSION_INSERT_SQL, "Root Permission", rootId);
            Integer rootPId = session.getValue(PERMISSION_MAX_ID_SQL);
            // Menu2, it's not a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu2", null);
            Integer menu2Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu2Id, rootId, 2, false);
            session.execute(PERMISSION_INSERT_SQL, "Menu2 Permission", menu2Id);
            Integer menu2PId = session.getValue(PERMISSION_MAX_ID_SQL);
            // Menu1, it's not a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu1", null);
            Integer menu1Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu1Id, rootId, 1, false);
            session.execute(PERMISSION_INSERT_SQL, "Menu1 Permission", menu1Id);
            Integer menu1PId = session.getValue(PERMISSION_MAX_ID_SQL);
            // Menu21, it's not a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu21", null);
            Integer menu21Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu21Id, menu2Id, 1, false);
            session.execute(PERMISSION_INSERT_SQL, "Menu21 Permission", menu21Id);
            Integer menu21PId = session.getValue(PERMISSION_MAX_ID_SQL);
            // Menu11, it's a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu11", "/js/menu11.js");
            Integer menu11Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu11Id, menu1Id, 1, true);
            session.execute(PERMISSION_INSERT_SQL, "Menu11 Permission", menu11Id);
            Integer menu11PId = session.getValue(PERMISSION_MAX_ID_SQL);
            // Menu12, it's a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu12", "/js/menu12.js");
            Integer menu12Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu12Id, menu1Id, 2, true);
            session.execute(PERMISSION_INSERT_SQL, "Menu12 Permission", menu12Id);
            Integer menu12PId = session.getValue(PERMISSION_MAX_ID_SQL);
            // Menu211, it's a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu211", "/js/menu211.js");
            Integer menu211Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu211Id, menu21Id, 1, true);            
            session.execute(PERMISSION_INSERT_SQL, "Menu211 Permission", menu211Id);
            Integer menu211PId = session.getValue(PERMISSION_MAX_ID_SQL);
            
            // Role Permission
            String ROLE_PERMISSION_INSERT_SQL = "insert into role_permission (role_id, permission_id) values (?, ?)";
            // User Role
            session.execute(ROLE_PERMISSION_INSERT_SQL, userRoleId, rootPId);
            session.execute(ROLE_PERMISSION_INSERT_SQL, userRoleId, menu1PId);
            session.execute(ROLE_PERMISSION_INSERT_SQL, userRoleId, menu11PId);
            // Developer Role
            session.execute(ROLE_PERMISSION_INSERT_SQL, developerRoleId, menu12PId);
            // Customer Role
            session.execute(ROLE_PERMISSION_INSERT_SQL, customerRoleId, menu2PId);
            session.execute(ROLE_PERMISSION_INSERT_SQL, customerRoleId, menu21PId);
            session.execute(ROLE_PERMISSION_INSERT_SQL, customerRoleId, menu211PId);
            
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    @Test 
    public void testParent() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            
            Set<Role> roles = new HashSet<Role>();
            roleService.parent(session, userRoleId, roles);
            Assert.assertTrue(roles.size() == 0);
            
            roles = new HashSet<Role>();
            roleService.parent(session, employeeRoleId, roles);
            Assert.assertTrue(roles.size() == 1);
            for (Role role : roles) {
                String name = role.getName();
                Assert.assertTrue(name.equals("User"));
            }
            
            roles = new HashSet<Role>();
            roleService.parent(session, developerRoleId, roles);
            Assert.assertTrue(roles.size() == 2);
            for (Role role : roles) {
                String name = role.getName();
                Assert.assertTrue(name.equals("User") || name.equals("Employee"));
            }

            roles = new HashSet<Role>();
            roleService.parent(session, customerRoleId, roles);
            Assert.assertTrue(roles.size() == 1);
            for (Role role : roles) {
                String name = role.getName();
                Assert.assertTrue(name.equals("User"));
            }
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
    @Test 
    public void testGetMenus() {
        Set<Role> roles = null;
        List<Menu> menus = null;
        Menu root = null;
        Menu menu1 = null;
        Menu menu11 = null;
        Menu menu12 = null;
        Menu menu2 = null;
        Menu menu21 = null;
        Menu menu211 = null;
        
        Role userRole = new Role();
        userRole.setId(userRoleId);
        Role employeeRole = new Role();
        employeeRole.setId(employeeRoleId);
        Role developerRole = new Role();
        developerRole.setId(developerRoleId);
        Role salesmanRole = new Role();
        salesmanRole.setId(salesmanRoleId);
        Role customerRole = new Role();
        customerRole.setId(customerRoleId);
 
        // User
        roles = new HashSet<Role>();
        roles.add(userRole);
        menus = roleService.getMenus(roles);
        Assert.assertTrue(menus.size() == 3);
        root = menus.get(0);
        Assert.assertTrue(root.getId() != null);
        Assert.assertTrue(root.getType().equals("MENU"));
        Assert.assertTrue(root.getName().equals("Root"));
        Assert.assertTrue(root.getValue() == null);
        Assert.assertTrue(root.getParentId() == null);
        Assert.assertTrue(root.getOrder() == 1);
        Assert.assertTrue(root.getIsLeaf() == false);
        Assert.assertTrue(root.getLevel() == 0);
        menu1 = menus.get(1);
        Assert.assertTrue(menu1.getId() != null);
        Assert.assertTrue(menu1.getType().equals("MENU"));
        Assert.assertTrue(menu1.getName().equals("Menu1"));
        Assert.assertTrue(menu1.getValue() == null);
        Assert.assertTrue(menu1.getParentId() == root.getId());
        Assert.assertTrue(menu1.getOrder() == 1);
        Assert.assertTrue(menu1.getIsLeaf() == false);
        Assert.assertTrue(menu1.getLevel() == 1);
        menu11 = menus.get(2);
        Assert.assertTrue(menu11.getId() != null);
        Assert.assertTrue(menu11.getType().equals("MENU"));
        Assert.assertTrue(menu11.getName().equals("Menu11"));
        Assert.assertTrue(menu11.getValue().equals("/js/menu11.js"));
        Assert.assertTrue(menu11.getParentId() == menu1.getId());
        Assert.assertTrue(menu11.getOrder() == 1);
        Assert.assertTrue(menu11.getIsLeaf() == true);
        Assert.assertTrue(menu11.getLevel() == 2);

        // Salesman
        roles = new HashSet<Role>();
        roles.add(salesmanRole);
        menus = roleService.getMenus(roles);
        Assert.assertTrue(menus.size() == 0);
        
        // Developer
        roles = new HashSet<Role>();
        roles.add(developerRole);
        menus = roleService.getMenus(roles);
        Assert.assertTrue(menus.size() == 1);
        menu12 = menus.get(0);
        Assert.assertTrue(menu12.getId() != null);
        Assert.assertTrue(menu12.getType().equals("MENU"));
        Assert.assertTrue(menu12.getName().equals("Menu12"));
        Assert.assertTrue(menu12.getValue().equals("/js/menu12.js"));
        Assert.assertTrue(menu12.getParentId() == menu1.getId());
        Assert.assertTrue(menu12.getOrder() == 2);
        Assert.assertTrue(menu12.getIsLeaf() == true);
        Assert.assertTrue(menu12.getLevel() == 2);

        // User and Customer
        roles = new HashSet<Role>();
        roles.add(userRole);
        roles.add(customerRole);
        menus = roleService.getMenus(roles);
        Assert.assertTrue(menus.size() == 6);
        root = menus.get(0);
        Assert.assertTrue(root.getId() != null);
        Assert.assertTrue(root.getType().equals("MENU"));
        Assert.assertTrue(root.getName().equals("Root"));
        Assert.assertTrue(root.getValue() == null);
        Assert.assertTrue(root.getParentId() == null);
        Assert.assertTrue(root.getOrder() == 1);
        Assert.assertTrue(root.getIsLeaf() == false);
        Assert.assertTrue(root.getLevel() == 0);
        menu1 = menus.get(1);
        Assert.assertTrue(menu1.getId() != null);
        Assert.assertTrue(menu1.getType().equals("MENU"));
        Assert.assertTrue(menu1.getName().equals("Menu1"));
        Assert.assertTrue(menu1.getValue() == null);
        Assert.assertTrue(menu1.getParentId() == root.getId());
        Assert.assertTrue(menu1.getOrder() == 1);
        Assert.assertTrue(menu1.getIsLeaf() == false);
        Assert.assertTrue(menu1.getLevel() == 1);
        menu11 = menus.get(2);
        Assert.assertTrue(menu11.getId() != null);
        Assert.assertTrue(menu11.getType().equals("MENU"));
        Assert.assertTrue(menu11.getName().equals("Menu11"));
        Assert.assertTrue(menu11.getValue().equals("/js/menu11.js"));
        Assert.assertTrue(menu11.getParentId() == menu1.getId());
        Assert.assertTrue(menu11.getOrder() == 1);
        Assert.assertTrue(menu11.getIsLeaf() == true);
        Assert.assertTrue(menu11.getLevel() == 2);
        menu2 = menus.get(3);
        Assert.assertTrue(menu2.getId() != null);
        Assert.assertTrue(menu2.getType().equals("MENU"));
        Assert.assertTrue(menu2.getName().equals("Menu2"));
        Assert.assertTrue(menu2.getValue() == null);
        Assert.assertTrue(menu2.getParentId() == root.getId());
        Assert.assertTrue(menu2.getOrder() == 2);
        Assert.assertTrue(menu2.getIsLeaf() == false);
        Assert.assertTrue(menu2.getLevel() == 1);
        menu21 = menus.get(4);
        Assert.assertTrue(menu21.getId() != null);
        Assert.assertTrue(menu21.getType().equals("MENU"));
        Assert.assertTrue(menu21.getName().equals("Menu21"));
        Assert.assertTrue(menu21.getValue() == null);
        Assert.assertTrue(menu21.getParentId() == menu2.getId());
        Assert.assertTrue(menu21.getOrder() == 1);
        Assert.assertTrue(menu21.getIsLeaf() == false);
        Assert.assertTrue(menu21.getLevel() == 2);
        menu211 = menus.get(5);
        Assert.assertTrue(menu211.getId() != null);
        Assert.assertTrue(menu211.getType().equals("MENU"));
        Assert.assertTrue(menu211.getName().equals("Menu211"));
        Assert.assertTrue(menu211.getValue().equals("/js/menu211.js"));
        Assert.assertTrue(menu211.getParentId() == menu21.getId());
        Assert.assertTrue(menu211.getOrder() == 1);
        Assert.assertTrue(menu211.getIsLeaf() == true);
        Assert.assertTrue(menu211.getLevel() == 3);
        
        // User, Employee, Developer, Salesman and Customer
        Set<Role> roles1 = new HashSet<Role>();
        roles1.add(userRole);
        roles1.add(employeeRole);
        roles1.add(developerRole);
        roles1.add(salesmanRole);
        roles1.add(customerRole);
        menus = roleService.getMenus(roles1);
        Assert.assertTrue(menus.size() == 7);
    }
    
    @After 
    public void tearDown() {
        this.deleteData();
        new Destroy().dropTables();
    }
    
    private void deleteData() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            List<String> sqls = new ArrayList<String>();
            sqls.add("delete from role_permission");
            sqls.add("delete from permission");
            sqls.add("delete from role");
            sqls.add("delete from menu");
            sqls.add("delete from resource");
            session.batch(sqls.toArray(new String[0]));
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

}