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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DB session factory.
 * 
 * @author Jason Xing
 */
public class SessionFactory {
    
    /*
     * Connection source type: DBCP(a connection is from the DBCP) or 
     * JDBCCF(a connection is created by the DriverManager).
     */
    private static String connSourceType = null;

    /**
     * Open a DB session.
     * 
     * <p>
     * If the DBCP configuration file exists, open a session binding a connection from the DBCP,<br>
     * else if the JDBCCF configuration file exists, open a session binding a connection created by the DriverManager,<br>
     * else if neither the DBCP configuration file nor the JDBCCF configuration file exists, throw NullPointerException.
     *  
     * @throws SQLException 
     */
    public static Session openSession() throws SQLException {
        Connection conn = null;
        if (connSourceType != null) {
            if (connSourceType.equals("DBCP")) {
                conn = Dbcp.getInstance().getConnection();
            } else if (connSourceType.equals("JDBCCF")) {
                conn = Jdbccf.getInstance().getConnection();
            }
        } else {
            InputStream dbcpConfFile = getDbcpConfFile();
            if (dbcpConfFile != null) {
                conn = Dbcp.getInstance().getConnection();
                connSourceType = "DBCP";
            } else {
                InputStream jdbccfConfFile = getJdbccfConfFile();
                if (jdbccfConfFile != null) {
                    conn = Jdbccf.getInstance().getConnection();
                    connSourceType = "JDBCCF";
                } else {
                    throw new NullPointerException("Can't find the DBCP or JDBCCF configuration file.");
                }
            }
        }
        return new Session(conn);
    }
    
    private static InputStream getDbcpConfFile() {
        String confFileName = Dbcp.getInstance().getConfFileName();
        return Dbcp.getInstance().getClass().getClassLoader().getResourceAsStream(confFileName);
    }
    
    private static InputStream getJdbccfConfFile() {
        String confFileName = Jdbccf.getInstance().getConfFileName();
        return Jdbccf.getInstance().getClass().getClassLoader().getResourceAsStream(confFileName);
    }
}
