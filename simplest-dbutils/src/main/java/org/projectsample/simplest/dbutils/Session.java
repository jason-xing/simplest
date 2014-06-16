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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DB session.
 * 
 * <p>
 * Here is an example:
 * <pre>
 * public static void sampleMethod() {
 *     Session session = null;
 *     try {
 *         session = SessionFactory.openSession();
 *         // Insert two records.
 *         String sqlInsert = "insert into person(id, name) values(?, ?)";
 *         session.execute(sqlInsert, 1, "Jason Xing");
 *         session.execute(sqlInsert, 2, "Moon Liu");
 *         // Get a person.
 *         String sqlGet = "select id, name from person where id = ?";
 *         Map<String, Object> person = session.get(sqlGet, 1);
 *         System.out.println("person=" + person);
 *         // Update a person.
 *         String sqlUpdate = "update person set name = ? where id = ?";
 *         session.execute(sqlUpdate, "Jason", 1);
 *         // Query the person modified.
 *         person = session.get(sqlGet, 1);
 *         System.out.println("personModified=" + person);
 *         session.commit();
 *     } catch (SQLException e) {
 *         session.rollback();
 *     } finally {
 *         session.close();
 *     }
 * }
 * </pre>
 * <p>
 * In a framework without the service and DAO layer, write the codes like that above. 
 * Otherwise, if you use a framework with the service layer and DAO layer, you can write the codes 
 * in the service layer like this below: 
 * <pre>
 * public static void sampleMethod() {
 *     Session session = null;
 *     try {
 *         session = SessionFactory.openSession();
 *         Sample1Dao sample1Dao = new Sample1Dao(session);
 *         sample1Dao.sampleMethod();
 *         Sample2Dao sample2Dao = new Sample2Dao(session);
 *         sample2Dao.sampleMethod();
 *         session.commit();
 *     } catch (SQLException e) {
 *         session.rollback();
 *     } finally {
 *         session.close();
 *     }
 * }
 * </pre>
 * 
 * @author Jason Xing
 */
public class Session {
    
    private static final Logger logger = LoggerFactory.getLogger(Session.class);
	
    private static final QueryRunner qr = new QueryRunner();

    private Connection conn = null;
    
    public Session(Connection conn) {
        this.conn = conn;
    }  
  
    /**
     * Query.
     * 
     * @param sql a SQL
     * @param params the replacement parameters
     * 
     * @return an empty list if no data
     * 
     * @throws SQLException if a database access error occurs
     */
    public List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        logger.debug("SQL= \n" + sql);
        // Print the parameters to the logger.
        printParams(params);
        MapListHandler h = new MapListHandler();
        return qr.query(conn, sql, h, params);
    }
    
    /**
     * Query.
     * 
     * @param sql a SQL
     * 
     * @return an empty list if no data
     * 
     * @throws SQLException if a database access error occurs 
     */
    public List<Map<String, Object>> query(String sql) throws SQLException {
        return query(sql, (Object[])null);
    }
    
    /**
     * Query.
     * 
     * @param type the class of the type of one element of the returned list
     * @param sql a SQL
     * @param params the replacement parameters
     * 
     * @return an empty list if no data
     * 
     * @throws SQLException if a database access error occurs 
     */
    public <T> List<T> query(Class<T> type, String sql, Object... params) throws SQLException {
        logger.debug("SQL= \n" + sql);
        // Print the parameters to the logger.
        printParams(params);
        ResultSetHandler<List<T>> h = new BeanListHandler<T>(type);
        return qr.query(conn, sql, h, params);
    }
    
    /**
     * Query.
     * 
     * @param type the class of the type of one element of the returned list
     * @param sql a SQL
     * 
     * @return an empty list if no data
     * 
     * @throws SQLException if a database access error occurs 
     */
    public <T> List<T> query(Class<T> type, String sql) throws SQLException {
        return query(type, sql, (Object[])null);
    }

    /**
     * Query values of a column.
     * 
     * @param columnName a column's name
     * @param sql a SQL
     * @param params the replacement parameters
     * 
     * @return an empty list if no data
     * 
     * @throws SQLException if a database access error occurs 
     */
    public <T> List<T> queryColumn(String columnName, String sql, Object... params) throws SQLException {
        logger.debug("SQL= \n" + sql);
        // Print the parameters to the logger.
        printParams(params);
        ResultSetHandler<List<T>> h = new ColumnListHandler<T>(columnName);
        return qr.query(conn, sql, h, params);
    }
    
    /**
     * Query values of a column.
     * 
     * @param columnName a column's name
     * @param sql a SQL
     * 
     * @return an empty list if no data
     * 
     * @throws SQLException if a database access error occurs
     */
    public <T> List<T> queryColumn(String columnName, String sql) throws SQLException {
        return queryColumn(columnName, sql, (Object[])null);
    }
    
    /**
     * Get one.
     * 
     * @param sql a SQL
     * @param params the replacement parameters
     * 
     * @return null if no data, the first one if more than one
     * 
     * @throws SQLException if a database access error occurs 
     */
    public Map<String, Object> get(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = query(sql, params);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
    
    /**
     * Get one.
     * 
     * @param sql a SQL
     * 
     * @return null if no data, the first one if more than one
     * 
     * @throws SQLException if a database access error occurs 
     */
    public Map<String, Object> get(String sql) throws SQLException {
        return get(sql, (Object[])null);
    }
    
    /**
     * Get one.
     * 
     * @param type the class of the type of the returned object
     * @param sql a SQL
     * @param params the replacement parameters
     * 
     * @return null if no data, the first one if more than one
     * 
     * @throws SQLException if a database access error occurs 
     */
    public <T> T get(Class<T> type, String sql, Object... params) throws SQLException {
        List<T> list = query(type, sql, params);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
    
    /**
     * Get one.
     * 
     * @param type the class of the type of the returned object
     * @param sql a SQL
     * 
     * @return null if no data, the first one if more than one
     * 
     * @throws SQLException if a database access error occurs 
     */
    public <T> T get(Class<T> type, String sql) throws SQLException {
        return get(type, sql, (Object[])null);
    }
    
    /**
     * Get a value.
     * 
     * @param sql a SQL
     * @param params the replacement parameters
     * 
     * @return null if no data
     * 
     * @throws SQLException if a database access error occurs 
     */
    public <T> T getValue(String sql, Object... params) throws SQLException {
        logger.debug("SQL= \n" + sql);
        // Print the parameters to the logger.
        printParams(params);
        ScalarHandler<T> h = new ScalarHandler<T>();
        return qr.query(conn, sql, h, params);
    }
    
    /**
     * Get a value.
     * 
     * @param sql a SQL
     * 
     * @return null if no data
     * 
     * @throws SQLException if a database access error occurs 
     */
    public <T> T getValue(String sql) throws SQLException {
        return getValue(sql, (Object[])null);
    }

    /**
     * Execute a SQL.
     * 
     * @param sql a SQL
     * @param params the replacement parameters
     * 
     * @return the number of the rows updated
     * 
     * @throws SQLException if a database access error occurs 
     */
    public int execute(String sql, Object... params) throws SQLException {
        logger.debug("SQL= \n" + sql);
        // Print the parameters to the logger.
        printParams(params);
        return qr.update(conn, sql, params);
     }
    
    /**
     * Execute a SQL.
     * 
     * @param sql a SQL
     * 
     * @return the number of the rows updated
     * 
     * @throws SQLException if a database access error occurs
     */
    public int execute(String sql) throws SQLException {
        return execute(sql, (Object[])null);
    }
    
    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     * 
     * @param sql a SQL
     * @param params an array of replacement parameters. Each row in this array is one set of batch replacement values.
     * 
     * @return the number of rows updated per statement.
     * 
     * @throws SQLException if a database access error occurs
     */
    public int[] batch(String sql, Object[][] params) throws SQLException {
        logger.debug("SQL= \n" + sql);
        // Print the parameters to the logger.
        for (Object[] params1 : params) {
            printParams(params1);
        }
        return qr.batch(conn, sql, params);
    }
    
    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     * 
     * @param sqls an array of SQL
     * 
     * @return the number of rows updated per statement.
     * 
     * @throws SQLException if a database access error occurs
     */
    public int[] batch(String[] sqls) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            for (String sql : sqls) {
                logger.debug("SQL= \n" + sql);
                st.addBatch(sql);
            }
            return st.executeBatch();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     * Commit the transaction.
     * 
     * <p>
     * It's necessary to execute it at the end of a transaction. 
     * 
     * @throws SQLException if a database access error occurs
     */
    public void commit() throws SQLException {
        if (conn != null) {
            conn.commit();
        }
    }
    
    /**
     * Rollback the transaction.
     */
    public void rollback() {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.error("Failed to rollback the transaction.");
            }
       }
    }
    
    /**
     * Close the session.
     * 
     * <p>
     * It's necessary to execute it to release the connection after using the session.
     */
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Failed to close the connection.");
            }
        }
    }
    
    /**
     * Print the parameters to the logger. 
     * 
     * <p>
     * The text will be printed like this: PARAMETERS=[v1;v2;null;v3;].<br>
     * 
     * @param params the replacement parameters
     */
    private void printParams(Object... params) {
        if (params != null) {
            String paramsStr = "";
            for (Object o : params) {
                if (o == null) {
                    paramsStr += "null;";
                } else {
                    paramsStr += (o.toString() + ";");
                }
            }
            logger.debug("PARAMETERS=[" + paramsStr + "]");
        }
    }
    
}