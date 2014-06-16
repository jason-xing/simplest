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
package org.projectsample.simplest.s1.security.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.projectsample.simplest.s1.exception.DbException;
import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.dbutils.SessionFactory;

/**
 * @author Jason Xing
 */
public class Destroy {
    
    public void dropTables() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            List<String> sqls = new ArrayList<String>();
            // 用户的角色
            sqls.add("drop table user_role");
            // 用户
            sqls.add("drop table \"user\"");
            // 角色的权限
            sqls.add("drop table role_permission");
            // 角色
            sqls.add("drop table role");
            // 权限
            sqls.add("drop table permission");
            // 菜单（前台）。resource的一种，继承自resource。
            sqls.add("drop table menu");
            // 资源
            sqls.add("drop table resource");
            session.batch(sqls.toArray(new String[0]));
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    public void deleteData() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            List<String> sqls = new ArrayList<String>();
            // 用户的角色
            sqls.add("delete from user_role");
            // 用户
            sqls.add("delete from \"user\"");
            // 角色的权限
            sqls.add("delete from role_permission");
            // 角色
            sqls.add("delete from role");
            // 权限
            sqls.add("delete from permission");
            // 菜单（前台）。resource的一种，继承自resource。
            sqls.add("delete from menu");
            // 资源
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
