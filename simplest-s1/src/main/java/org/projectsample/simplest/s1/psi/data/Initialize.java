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
package org.projectsample.simplest.s1.psi.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.projectsample.simplest.s1.exception.DbException;
import org.projectsample.simplest.dbutils.Session;
import org.projectsample.simplest.dbutils.SessionFactory;

/**
 * @author Jason Xing
 */
public class Initialize {
    
    public void createTables() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            List<String>  sqls = createTablesSqls();
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
        // 商品
        sql = "create table goods (                                                           \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    name            varchar(32) not null unique                                \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 采购单
        sql = "create table po (                                                              \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    \"no\"            varchar(16) not null unique,                             \n"
            + "    vendor          varchar(64) not null,                                      \n"
            + "    amount          numeric(10, 2) not null,                                   \n"
            + "    created_time    timestamp not null,                                        \n"
            + "    created_by      integer not null references \"user\" (id),                 \n"
            + "    status          smallint not null                                          \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 采购单条目
        sql = "create table po_item (                                                         \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    po_id           integer not null references po (id),                       \n"
            + "    goods_id        integer not null references goods (id),                    \n"
            + "    quantity        integer not null,                                          \n"
            + "    unit_cost       numeric(10, 2) not null                                    \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 销售单
        sql = "create table so (                                                              \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    \"no\"            varchar(16) not null unique,                             \n"
            + "    customer        varchar(64) not null,                                      \n"
            + "    salesperson     varchar(32) not null,                                      \n"
            + "    amount          numeric(10, 2) not null,                                   \n"
            + "    created_time    timestamp not null,                                        \n"
            + "    created_by      integer not null references \"user\" (id),                 \n"
            + "    status          smallint not null                                          \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 销售单条目
        sql = "create table so_item (                                                         \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    so_id           integer not null references so (id),                       \n"
            + "    goods_id        integer not null references goods (id),                    \n"
            + "    quantity        integer not null,                                          \n"
            + "    unit_cost       numeric(10, 2) not null                                    \n"
            + ")                                                                              \n";
        sqls.add(sql);
        // 入库单
        sql = "create table inventory_receipt (                                              \n"
            + "    id             integer not null generated always as identity primary key, \n"
            + "    \"no\"           varchar(16) not null unique,                             \n"
            + "    reference_type integer,                                                   \n"
            + "    reference_no   varchar(16),                                               \n"
            + "    created_time   timestamp not null,                                        \n"
            + "    created_by     integer not null references \"user\" (id),                 \n"
            + "    received_time  timestamp,                                                 \n"
            + "    received_by    integer references \"user\" (id),                          \n"
            + "    status         smallint not null                                          \n"
            + ")                                                                             \n";
        sqls.add(sql);
        // 入库单条目
        sql = "create table inventory_receipt_item (                                               \n"
            + "    id                   integer not null generated always as identity primary key, \n"
            + "    inventory_receipt_id integer not null references inventory_receipt (id),        \n"
            + "    goods_id             integer not null references goods (id),                    \n"
            + "    quantity             integer not null                                           \n"
            + ")                                                                                   \n";
        sqls.add(sql);
        // 出库单
        sql = "create table inventory_issue (                                                \n"
            + "    id             integer not null generated always as identity primary key, \n"
            + "    \"no\"           varchar(16) not null unique,                             \n"
            + "    reference_type integer,                                                   \n"
            + "    reference_no   varchar(16),                                               \n"
            + "    created_time   timestamp not null,                                        \n"
            + "    created_by     integer not null references \"user\" (id),                 \n"
            + "    issued_time    timestamp,                                                 \n"
            + "    issued_by      integer references \"user\" (id),                          \n"
            + "    status         smallint not null                                          \n"
            + ")                                                                             \n";
        sqls.add(sql);
        // 出库单条目
        sql = "create table inventory_issue_item (                                               \n"
            + "    id                 integer not null generated always as identity primary key, \n"
            + "    inventory_issue_id integer not null references inventory_issue (id),          \n"
            + "    goods_id           integer not null references goods (id),                    \n"
            + "    quantity           integer not null                                           \n"
            + ")                                                                                 \n";
        sqls.add(sql);
        // 库存
        sql = "create table inventory_stock (                                                 \n"
            + "    id              integer not null generated always as identity primary key, \n"
            + "    goods_id        integer not null references goods (id),                    \n"
            + "    quantity        integer not null                                           \n"
            + ")                                                                              \n";
        sqls.add(sql);
        return sqls;
    }

    public void initData() {
        
    }
}
