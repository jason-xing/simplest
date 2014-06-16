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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBC connection factory. It's a singleton. 
 * 
 * <p>
 * You can get a connection created by the DriverManager by the class.<br>
 * It depends on the configuration file named <b>jdbccf.properties</b>.
 * In it, there are four necessary properties:
 * <b>driverClassName</b>, <b>url</b>, <b>user</b>, <b>password</b>. 
 * <p>
 * When use it,<br>
 * if can't find the JDBCCF configuration file, throw NullPointerException;<br>
 * if failed to load the JDBCCF properties, throw RuntimeException;<br>
 * if the property JDBCCF url or driverClassName is empty, throw NullPointerException;<br>
 * if can't find the JDBC driver class, throw RuntimeException.
 * 
 * @author Jason Xing
 */
public class Jdbccf {

    private static final Logger logger = LoggerFactory.getLogger(Jdbccf.class);

    private static final Jdbccf instance = new Jdbccf();
    
    private String confFileName = "jdbccf.properties";

    private Properties properties = new Properties();
    
    private String url = "";

    private Jdbccf() {
        // Get the JDBCCF configuration file.
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(confFileName);
        if (inputStream == null) {
            throw new NullPointerException("Can't find the JDBCCF configuration file.");
        } 
        // Load the JDBCCF properties. 
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the JDBCCF properties.");
        }
        url = properties.getProperty("url");
        if (url == null || url.trim().equals("")) {
            throw new NullPointerException("The JDBCCF property url is empty.");
        }
        String driverClassName = properties.getProperty("driverClassName");
        if (driverClassName == null || driverClassName.trim().equals("")) {
            throw new NullPointerException("The JDBCCF property driverClassName is empty.");
        }
        // Load the JDBC driver class. 
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find the JDBC driver class.");
        }
    }

    /**
     * Get the instance of this class.
     */
    public static Jdbccf getInstance() {
        return instance;
    }

    /**
     * Get the configuration file's name.
     */
    public String getConfFileName() {
        return confFileName;
    }

    /**
     * Get a connection created by the DriverManager. 
     * 
     * <p>
     * The connection is <i>not</i> autoCommit.
     * 
     * @return a real connection to the DB
     * 
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, properties);
        logger.debug("Got a connection: " + conn);
        conn.setAutoCommit(false);
        return conn;
    }
    
}