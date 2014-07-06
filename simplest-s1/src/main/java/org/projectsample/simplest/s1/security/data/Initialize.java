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
import org.projectsample.simplest.s1.security.utils.MD5;
import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.dbutils.SessionFactory;

/**
 * @author Jason Xing
 */
public class Initialize {
    
    private String ADMIN_USER_NAME = "admin";
    private String ADMIN_PASSWORD = "123456";
    
    public void createTables() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            List<String> sqls = createTablesSqls();
            session.batch(sqls.toArray(new String[0]));
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }

    private List<String> createTablesSqls() {
        List<String> sqls = new ArrayList<String>();
        String sql = "";
        // 资源
        sql = "create table resource (                                                        \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    type            varchar(16) not null,                                      \n"
            + "    name            varchar(64) not null,                                      \n"
            + "    value           varchar(128),                                              \n"
            + "    unique (type, name)                                                        \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 菜单（前台）。resource的一种，继承自resource。
        sql = "create table menu (                                                        \n"
            + "    id              integer not null primary key references resource (id), \n"
            + "    parent_id       integer references menu (id),                          \n"
            + "    \"order\"         integer not null,                                    \n"
            + "    is_leaf         boolean not null                                       \n"
            + ")                                                                          \n";
        sqls.add(sql);
        // 权限
        sql = "create table permission (                                                      \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    name            varchar(64) not null unique,                               \n"
            + "    resource_id     integer not null references resource (id),                 \n"
            + "    operation       varchar(16) not null,                                      \n"
            + "    unique (resource_id, operation)                                            \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 角色
        sql = "create table role (                                                            \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    name            varchar(64) not null unique,                               \n"
            + "    parent_id       integer references role (id)                               \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 角色的权限
        sql = "create table role_permission (                                   \n"
            + "    role_id         integer not null references role (id),       \n"
            + "    permission_id   integer not null references permission (id), \n"
            + "    unique (role_id, permission_id)                              \n"
            + ")                                                                \n";
        sqls.add(sql);
        // 用户。user在DB中是关键字
        sql = "create table \"user\" (                                                        \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    username        varchar(32) not null unique,                               \n"
            + "    password_enc    varchar(32) not null,                                      \n"
            + "    email           varchar(32) unique                                         \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 用户的角色
        sql = "create table user_role (                                       \n"
            + "    user_id         integer not null references \"user\" (id), \n"
            + "    role_id         integer not null references role (id),     \n"
            + "    unique (user_id, role_id)                                  \n"
            + ")                                                              \n";
        sqls.add(sql);
        return sqls;
    }

    public void initData() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            String RESOURCE_INSERT_SQL = "insert into resource (type, name, value) values (?, ?, ?)";
            String RESOURCE_MAX_ID_SQL = "select max(id) from resource";
            String MENU_INSERT_SQL = "insert into menu (id, parent_id, \"order\", is_leaf) values (?, ?, ?, ?)";
            String PERMISSION_INSERT_SQL = "insert into permission (name, resource_id, operation) values (?, ?, ?)";
            String PERMISSION_MAX_ID_SQL = "select max(id) from permission";
            String ROLE_INSERT_SQL = "insert into role (name, parent_id) values (?, ?)";
            String ROLE_MAX_ID_SQL = "select max(id) from role";
            String ROLE_PERMISSION_INSERT_SQL = "insert into role_permission (role_id, permission_id) values (?, ?)";
            String USER_MAX_ID_SQL = "select max(id) from \"user\"";

            // 插入根菜单
            session.execute(RESOURCE_INSERT_SQL, "MENU", "Root", null);
            Integer rootMenuId = session.getValue(RESOURCE_MAX_ID_SQL);
            session.execute(MENU_INSERT_SQL, rootMenuId, null, 1, false);

            // 插入UserLogin URL
            session.execute(RESOURCE_INSERT_SQL, "URL", "UserLogin", "/UserLogin.action");
            Integer userLoginUrlId = session.getValue(RESOURCE_MAX_ID_SQL);
            // 插入UserLogin URL权限
            session.execute(PERMISSION_INSERT_SQL, "UserLogin", userLoginUrlId, "VISIT");
            Integer userLoginPermissionId = session.getValue(PERMISSION_MAX_ID_SQL);
            // 插入UserRegister URL
            session.execute(RESOURCE_INSERT_SQL, "URL", "UserRegister", "/UserRegister.action");
            Integer userRegisterUrlId = session.getValue(RESOURCE_MAX_ID_SQL);
            // 插入UserRegister URL权限
            session.execute(PERMISSION_INSERT_SQL, "UserRegister", userRegisterUrlId, "VISIT");
            Integer userRegisterPermissionId = session.getValue(PERMISSION_MAX_ID_SQL);
            // 插入UserRegisterEnter URL
            session.execute(RESOURCE_INSERT_SQL, "URL", "UserRegisterEnter", "/UserRegisterEnter.action");
            Integer userRegisterEnterUrlId = session.getValue(RESOURCE_MAX_ID_SQL);
            // 插入UserRegisterEnter URL权限
            session.execute(PERMISSION_INSERT_SQL, "UserRegisterEnter", userRegisterEnterUrlId, "VISIT");
            Integer userRegisterEnterPermissionId = session.getValue(PERMISSION_MAX_ID_SQL);
            // 插入UserLogout URL
            session.execute(RESOURCE_INSERT_SQL, "URL", "UserLogout", "/UserLogout.action");
            Integer userLogoutUrlId = session.getValue(RESOURCE_MAX_ID_SQL);
            // 插入UserLogout URL权限
            session.execute(PERMISSION_INSERT_SQL, "UserLogout", userLogoutUrlId, "VISIT");
            Integer userLogoutPermissionId = session.getValue(PERMISSION_MAX_ID_SQL);

            // 插入MyInfoEnter URL
            session.execute(RESOURCE_INSERT_SQL, "URL", "MyInfoEnter", "/MyInfoEnter.action");
            Integer myInfoEnterUrlId = session.getValue(RESOURCE_MAX_ID_SQL);
            // 插入MyInfoEnter URL权限
            session.execute(PERMISSION_INSERT_SQL, "MyInfoEnter", myInfoEnterUrlId, "VISIT");
            Integer myInfoEnterPermissionId = session.getValue(PERMISSION_MAX_ID_SQL);
            // 插入MyInfoModify URL
            session.execute(RESOURCE_INSERT_SQL, "URL", "MyInfoModify", "/MyInfoModify.action");
            Integer myInfoModifyUrlId = session.getValue(RESOURCE_MAX_ID_SQL);
            // 插入MyInfoModify URL权限
            session.execute(PERMISSION_INSERT_SQL, "MyInfoModify", myInfoModifyUrlId, "VISIT");
            Integer myInfoModifyPermissionId = session.getValue(PERMISSION_MAX_ID_SQL);

            // 插入基础角色（根角色）
            session.execute(ROLE_INSERT_SQL, "BaseRole", null);
            Integer baseRoleId = session.getValue(ROLE_MAX_ID_SQL);
            // 为基础角色分配MyInfoEnter URL权限
            session.execute(ROLE_PERMISSION_INSERT_SQL, baseRoleId, myInfoEnterPermissionId);
            // 为基础角色分配MyInfoModify URL权限
            session.execute(ROLE_PERMISSION_INSERT_SQL, baseRoleId, myInfoModifyPermissionId);
            // 插入普通角色
            session.execute(ROLE_INSERT_SQL, "CommonRole", baseRoleId);
            // 以下是两个比较特殊的角色
            // 插入管理员角色
            session.execute(ROLE_INSERT_SQL, "Administrator", null);
            Integer administratorRoleId = session.getValue(ROLE_MAX_ID_SQL);
            // 插入匿名者角色
            session.execute(ROLE_INSERT_SQL, "Anonymous", null);
            Integer anonymousRoleId = session.getValue(ROLE_MAX_ID_SQL);
            // 为匿名者角色分配UserLogout URL权限
            session.execute(ROLE_PERMISSION_INSERT_SQL, anonymousRoleId, userLogoutPermissionId);
            // 为匿名者角色分配UserLogin URL权限
            session.execute(ROLE_PERMISSION_INSERT_SQL, anonymousRoleId, userLoginPermissionId);
            // 为匿名者角色分配UserRegister URL权限
            session.execute(ROLE_PERMISSION_INSERT_SQL, anonymousRoleId, userRegisterPermissionId);
            // 为匿名者角色分配UserRegisterEnter URL权限
            session.execute(ROLE_PERMISSION_INSERT_SQL, anonymousRoleId, userRegisterEnterPermissionId);
            
            // 插入管理员用户
            String USER_INSERT_SQL = "insert into \"user\" (username, password_enc, email) values (?, ?, ?)";
            session.execute(USER_INSERT_SQL, ADMIN_USER_NAME, MD5.encodeString(ADMIN_PASSWORD, null), "jasons.xing@gmail.com");
            Integer administratorUserId = session.getValue(USER_MAX_ID_SQL);
            // 为管理员用户挂上管理员角色
            String USER_ROLE_INSERT_SQL = "insert into user_role (user_id, role_id) values (?, ?)";
            session.execute(USER_ROLE_INSERT_SQL, administratorUserId, administratorRoleId);
            
            session.commit();
        } catch (SQLException e) {
            session.rollback();
            throw new DbException(e);
        } finally {
            session.close();
        }
    }
    
}
