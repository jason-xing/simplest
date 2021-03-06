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
package org.projectsample.simplest.s1.psi.bean;

public class InventoryIssueItem {

    private Integer id;
    private Integer inventoryIssueId;
    private Integer goodsId;
    private Integer quantity;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getInventoryIssueId() {
        return inventoryIssueId;
    }
    public void setInventoryIssueId(Integer inventoryIssueId) {
        this.inventoryIssueId = inventoryIssueId;
    }
    public Integer getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}