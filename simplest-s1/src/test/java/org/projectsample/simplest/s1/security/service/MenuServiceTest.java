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
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.dbutils.SessionFactory;
import org.projectsample.simplest.s1.exception.DbException;
import org.projectsample.simplest.s1.security.bean.Menu;
import org.projectsample.simplest.s1.security.data.Destroy;
import org.projectsample.simplest.s1.security.data.Initialize;

public class MenuServiceTest {

    private ResourceService menuService = new ResourceService();

    @Before 
    public void setup() throws Exception {
        new Initialize().createTables();
        this.initData();
    }
    
    /*
     * Menus:<br>
     * Root<br>
     * ----Menu1<br>
     * --------Menu11<br>
     * --------Menu12<br>
     * ----Menu2<br>
     * --------Menu21<br>
     * ------------Menu221<br>
     */
    private void initData() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            String RESOURCE_INSERT_SQL = "insert into resource (type, name, value) values ('MENU', ?, ?)";
            String RESOURCE_MAX_ID_SQL = "select max(id) from resource";
            String MENU_INSERT_SQL = "insert into menu (id, parent_id, \"order\", is_leaf) values (?, ?, ?, ?)";
            // Menu Root
            session.execute(RESOURCE_INSERT_SQL, "Root", null);
            Integer rootId = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, rootId, null, 1, false);
            // Menu2, it's not a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu2", null);
            Integer menu2Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu2Id, rootId, 2, false);
            // Menu1, it's not a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu1", null);
            Integer menu1Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu1Id, rootId, 1, false);
            // Menu21, it's not a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu21", null);
            Integer menu21Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu21Id, menu2Id, 1, false);
            // Menu11, it's a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu11", "/js/menu11.js");
            Integer menu11Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu11Id, menu1Id, 1, true);
            // Menu12, it's a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu12", "/js/menu12.js");
            Integer menu12Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu12Id, menu1Id, 2, true);
            // Menu211, it's a leaf.
            session.execute(RESOURCE_INSERT_SQL, "Menu211", "/js/menu211.js");
            Integer menu211Id = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, menu211Id, menu21Id, 1, true);
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    private void deleteData() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            List<String> sqls = new ArrayList<String>();
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

    @Test 
    public void testGetAll() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            List<Menu> menus = menuService.getAllMenus(session);
            Assert.assertTrue(menus.size() == 7);
            Menu root = menus.get(0);
            Assert.assertTrue(root.getId() != null);
            Assert.assertTrue(root.getType().equals("MENU"));
            Assert.assertTrue(root.getName().equals("Root"));
            Assert.assertTrue(root.getValue() == null);
            Assert.assertTrue(root.getParentId() == null);
            Assert.assertTrue(root.getOrder() == 1);
            Assert.assertTrue(root.getIsLeaf() == false);
            Assert.assertTrue(root.getLevel() == 0);
            Menu menu1 = menus.get(1);
            Assert.assertTrue(menu1.getId() != null);
            Assert.assertTrue(menu1.getType().equals("MENU"));
            Assert.assertTrue(menu1.getName().equals("Menu1"));
            Assert.assertTrue(menu1.getValue() == null);
            Assert.assertTrue(menu1.getParentId() == root.getId());
            Assert.assertTrue(menu1.getOrder() == 1);
            Assert.assertTrue(menu1.getIsLeaf() == false);
            Assert.assertTrue(menu1.getLevel() == 1);
            Menu menu11 = menus.get(2);
            Assert.assertTrue(menu11.getId() != null);
            Assert.assertTrue(menu11.getType().equals("MENU"));
            Assert.assertTrue(menu11.getName().equals("Menu11"));
            Assert.assertTrue(menu11.getValue().equals("/js/menu11.js"));
            Assert.assertTrue(menu11.getParentId() == menu1.getId());
            Assert.assertTrue(menu11.getOrder() == 1);
            Assert.assertTrue(menu11.getIsLeaf() == true);
            Assert.assertTrue(menu11.getLevel() == 2);
            Menu menu12 = menus.get(3);
            Assert.assertTrue(menu12.getId() != null);
            Assert.assertTrue(menu12.getType().equals("MENU"));
            Assert.assertTrue(menu12.getName().equals("Menu12"));
            Assert.assertTrue(menu12.getValue().equals("/js/menu12.js"));
            Assert.assertTrue(menu12.getParentId() == menu1.getId());
            Assert.assertTrue(menu12.getOrder() == 2);
            Assert.assertTrue(menu12.getIsLeaf() == true);
            Assert.assertTrue(menu12.getLevel() == 2);
            Menu menu2 = menus.get(4);
            Assert.assertTrue(menu2.getId() != null);
            Assert.assertTrue(menu2.getType().equals("MENU"));
            Assert.assertTrue(menu2.getName().equals("Menu2"));
            Assert.assertTrue(menu2.getValue() == null);
            Assert.assertTrue(menu2.getParentId() == root.getId());
            Assert.assertTrue(menu2.getOrder() == 2);
            Assert.assertTrue(menu2.getIsLeaf() == false);
            Assert.assertTrue(menu2.getLevel() == 1);
            Menu menu21 = menus.get(5);
            Assert.assertTrue(menu21.getId() != null);
            Assert.assertTrue(menu21.getType().equals("MENU"));
            Assert.assertTrue(menu21.getName().equals("Menu21"));
            Assert.assertTrue(menu21.getValue() == null);
            Assert.assertTrue(menu21.getParentId() == menu2.getId());
            Assert.assertTrue(menu21.getOrder() == 1);
            Assert.assertTrue(menu21.getIsLeaf() == false);
            Assert.assertTrue(menu21.getLevel() == 2);
            Menu menu211 = menus.get(6);
            Assert.assertTrue(menu211.getId() != null);
            Assert.assertTrue(menu211.getType().equals("MENU"));
            Assert.assertTrue(menu211.getName().equals("Menu211"));
            Assert.assertTrue(menu211.getValue().equals("/js/menu211.js"));
            Assert.assertTrue(menu211.getParentId() == menu21.getId());
            Assert.assertTrue(menu211.getOrder() == 1);
            Assert.assertTrue(menu211.getIsLeaf() == true);
            Assert.assertTrue(menu211.getLevel() == 3);
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
    @After 
    public void tearDown() {
        this.deleteData();
        new Destroy().dropTables();
    }
}