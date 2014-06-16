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
public class Destroy {
    
    public void dropTables() {
        Session session = null;
        try {
            session = SessionFactory.openSession();
            List<String> sqls = new ArrayList<String>();
            // 库存
            sqls.add("drop table inventory_stock");
            // 出库单条目
            sqls.add("drop table inventory_issue_item");
            // 出库单
            sqls.add("drop table inventory_issue");
            // 入库单条目
            sqls.add("drop table inventory_receipt_item");
            // 入库单
            sqls.add("drop table inventory_receipt");
            // 销售单条目
            sqls.add("drop table so_item");
            // 销售单
            sqls.add("drop table so");
            // 采购单条目
            sqls.add("drop table po_item");
            // 采购单
            sqls.add("drop table po");
            // 商品
            sqls.add("drop table goods");
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
            // 库存
            sqls.add("delete from inventory_stock");
            // 出库单条目
            sqls.add("delete from inventory_issue_item");
            // 出库单
            sqls.add("delete from inventory_issue");
            // 入库单条目
            sqls.add("delete from inventory_receipt_item");
            // 入库单
            sqls.add("delete from inventory_receipt");
            // 销售单条目
            sqls.add("delete from so_item");
            // 销售单
            sqls.add("delete from so");
            // 采购单条目
            sqls.add("delete from po_item");
            // 采购单
            sqls.add("delete from po");
            // 商品
            sqls.add("delete from goods");
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
