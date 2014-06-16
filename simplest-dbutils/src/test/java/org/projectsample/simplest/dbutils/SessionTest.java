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
package org.projectsample.simplest.dbutils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Session.class.
 * 
 * @author Jason Xing
 */
public class SessionTest {

    @Before
    public void setup() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // Create tables
            String sql = "create table person (id int, name varchar(32))";
            session.execute(sql);
            // Insert data
            sql = "insert into person (id, name) values (1, 'Jason Xing')";
            session.execute(sql);
            sql = "insert into person (id, name) values (2, 'Moon Liu')";
            session.execute(sql);
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
    @Test 
    public void testQuery() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // Test the method: query(String sql, Object... params)
            String sql = "select id, name from person where id = ?";
            List<Map<String, Object>> persons = session.query(sql, 1);
            Assert.assertTrue(1 == persons.size());
            persons = session.query(sql, -1);
            Assert.assertTrue(0 == persons.size());
            // Test the method: query(String sql)
            sql = "select id, name from person";
            persons = session.query(sql);
            Assert.assertTrue(2 == persons.size());
            sql = "select id, name from person where id = -1";
            persons = session.query(sql);
            Assert.assertTrue(0 == persons.size());
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
    @Test 
    public void testQueryT() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // Test the method: query(Class<T> type, String sql, Object... params)
            String sql = "select id, name from person where id = ?";
            List<Person> persons = session.query(Person.class, sql, 1);
            Assert.assertTrue(1 == persons.size());
            persons = session.query(Person.class, sql, -1);
            Assert.assertTrue(0 == persons.size());
            // Test the method: query(Class<T> type, String sql)
            sql = "select id, name from person";
            persons = session.query(Person.class, sql);
            Assert.assertTrue(2 == persons.size());
            sql = "select id, name from person where id = -1";
            persons = session.query(Person.class, sql);
            Assert.assertTrue(0 == persons.size());
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    @Test 
    public void testQueryColumn() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // Test the method: queryColumn(String columnName, String sql, Object... params)
            String sql = "select id, name from person where id = ?";
            List<String> names = session.queryColumn("name", sql, 1);
            Assert.assertTrue(1 == names.size());
            names = session.queryColumn("name", sql, -1);
            Assert.assertTrue(0 == names.size());
            // Test the method: queryColumn(String columnName, String sql)
            sql = "select id, name from person order by name";
            names = session.queryColumn("name", sql);
            Assert.assertTrue(2 == names.size());
            Assert.assertTrue("Jason Xing".equals(names.get(0)));
            Assert.assertTrue("Moon Liu".equals(names.get(1)));
            sql = "select id, name from person where id = -1";
            names = session.queryColumn("name", sql);
            Assert.assertTrue(0 == names.size());
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    @Test 
    public void testGet() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // Test the method: get(String sql, Object... params)
            String sql = "select id, name from person where id = ?";
            Map<String, Object> person = session.get(sql, 1);
            Assert.assertTrue(((Integer)person.get("id")).compareTo(1) == 0);
            Assert.assertTrue(((String)person.get("name")).equals("Jason Xing"));
            person = session.get(sql, -1);
            Assert.assertNull(person);
            sql = "select id, name from person where id != ? order by id desc";
            person = session.get(sql, -1);
            Assert.assertTrue(((Integer)person.get("id")).compareTo(2) == 0);
            Assert.assertTrue(((String)person.get("name")).equals("Moon Liu"));
            // Test the method: get(String sql)
            sql = "select id, name from person where id = 1";
            person = session.get(sql);
            Assert.assertTrue(((Integer)person.get("id")).compareTo(1) == 0);
            Assert.assertTrue(((String)person.get("name")).equals("Jason Xing"));
            sql = "select id, name from person where id = -1";
            person = session.get(sql);
            Assert.assertNull(person);
            sql = "select id, name from person order by id desc";
            person = session.get(sql);
            Assert.assertTrue(((Integer)person.get("id")).compareTo(2) == 0);
            Assert.assertTrue(((String)person.get("name")).equals("Moon Liu"));
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
    @Test 
    public void testGetT() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // Test the method: get(Class<T> type, String sql, Object... params)
            String sql = "select id, name from person where id = ?";
            Person person = session.get(Person.class, sql, 1);
            Assert.assertTrue(person.getId() == 1);
            Assert.assertTrue(person.getName().equals("Jason Xing"));
            person = session.get(Person.class, sql, -1);
            Assert.assertNull(person);
            sql = "select id, name from person where id != ? order by id desc";
            person = session.get(Person.class, sql, -1);
            Assert.assertTrue(person.getId() == 2);
            Assert.assertTrue(person.getName().equals("Moon Liu"));
            // Test the method: get(Class<T> type, String sql)
            sql = "select id, name from person where id = 1";
            person = session.get(Person.class, sql);
            Assert.assertTrue(person.getId() == 1);
            Assert.assertTrue(person.getName().equals("Jason Xing"));
            sql = "select id, name from person where id = -1";
            person = session.get(Person.class, sql);
            Assert.assertNull(person);
            sql = "select id, name from person order by id desc";
            person = session.get(Person.class, sql);
            Assert.assertTrue(person.getId() == 2);
            Assert.assertTrue(person.getName().equals("Moon Liu"));
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    @Test 
    public void testExecute() {
        Session session = null;
        // Test the method: execute(String sql, Object... params)
        try {
            session = SessionFactory.openSession();
            String sql = "update person set name = ? where id = ?";
            int rowNumUpdated = session.execute(sql, "Jason", 1);
            Assert.assertTrue(1 == rowNumUpdated);
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }        
        try {
            session = SessionFactory.openSession();
            String sql = "select id, name from person where id = ?";
            Map<String, Object> person = session.get(sql, 1);
            Assert.assertTrue(((String)person.get("name")).equals("Jason"));
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
        // Test the method: execute(String sql)
        try {
            session = SessionFactory.openSession();
            String sql = "update person set name = 'Moon' where id = 2";
            int rowNumUpdated = session.execute(sql);
            Assert.assertTrue(1 == rowNumUpdated);
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }        
        try {
            session = SessionFactory.openSession();
            String sql = "select id, name from person where id = ?";
            Map<String, Object> person = session.get(sql, 2);
            Assert.assertTrue(((String)person.get("name")).equals("Moon"));
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }

    }

    @Test 
    public void testGetValue() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // Test the method: getValue(String sql, Object... params)
            String sql = "select name from person where id = ?";
            String name = session.getValue(sql, 1);
            Assert.assertTrue(name.equals("Jason Xing"));
            name = session.getValue(sql, -1);
            Assert.assertNull(name);
            // Test the method: getValue(String sql)
            sql = "select name from person where id = 1";
            name = session.getValue(sql);
            Assert.assertTrue(name.equals("Jason Xing"));
            sql = "select name from person where id = -1";
            name = session.getValue(sql);
            Assert.assertNull(name);
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
    @After
    public void tearDown() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            // Drop tables
            String sql = "drop table person";
            session.execute(sql);
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }    

}
