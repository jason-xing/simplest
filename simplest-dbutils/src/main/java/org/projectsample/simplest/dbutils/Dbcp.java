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

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DBCP. It's a singleton.
 * 
 * <p>
 * You can get a connection from the DBCP by the class.<br>
 * It depends on the configuration file named <b>dbcp.properties</b>.
 * In it, there are four necessary properties:
 * <b>driverClassName</b>, <b>url</b>, <b>username</b>, <b>password</b>.<br>
 * In addition, there is another important property called <b>initialSize</b>, it means the 
 * number of initial connections of the DBCP.
 * <p>
 * When use it,<br>
 * if can't find the DBCP configuration file, throw NullPointerException;<br>
 * if failed to load the DBCP properties, throw RuntimeException;<br>
 * if can't create the JDBC datasource, throw RuntimeException.
 * 
 * @author Jason Xing
 */
public class Dbcp {
    
    private static final Logger logger = LoggerFactory.getLogger(Dbcp.class);

    private static final Dbcp instance = new Dbcp();

    private String confFileName = "dbcp.properties";

    private DataSource dataSource = null;
    
    private Dbcp() {
        // Get the DBCP configuration file. 
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(confFileName);
        if (inputStream == null) {
            throw new NullPointerException("Can't find the DBCP configuration file.");
        }
        // Load the DBCP properties. 
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the DBCP properties.");
        }     
        // Create the JDBC datasource.
        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("Can't create the JDBC datasource");
        }
    }

    /**
     * Get the instance of this class.
     */
    public static Dbcp getInstance() {
        return instance;
    }

    /**
     * Get the configuration file's name.
     */
    public String getConfFileName() {
        return confFileName;
    }

    /**
     * Get the number of active connections of the DBCP.
     */
    public int getNumActive() {
        return ((BasicDataSource)dataSource).getNumActive();
    }

    /**
     * Get the number of idle connections of the DBCP.
     */
    public int getNumIdle() {
        return ((BasicDataSource)dataSource).getNumIdle();
    }  

    /**
     * Get the max number of active connections of the DBCP.
     */
    public int getMaxActive() {
        return ((BasicDataSource)dataSource).getMaxActive();
    }

    /**
     * Get a connection from the DBCP.
     * 
     * <p>
     * The connection is <i>not</i> autoCommit.
     * 
     * @return a connection from the DBCP
     * 
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        logger.debug("Got a connection: " + conn);
        conn.setAutoCommit(false);
        return conn;
    }

}