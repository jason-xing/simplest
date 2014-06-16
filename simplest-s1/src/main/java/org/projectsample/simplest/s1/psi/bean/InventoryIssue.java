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


public class InventoryIssue {

    private Integer id;
    private String no;
    private Integer referenceType;
    private String referenceNo;
    private String createdTime;
    private Integer createdBy;
    private String issuedTime;
    private Integer issuedBy;
    private Integer status;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public Integer getReferenceType() {
        return referenceType;
    }
    public void setReferenceType(Integer referenceType) {
        this.referenceType = referenceType;
    }
    public String getReferenceNo() {
        return referenceNo;
    }
    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
    public Integer getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    public String getIssuedTime() {
        return issuedTime;
    }
    public void setIssuedTime(String issuedTime) {
        this.issuedTime = issuedTime;
    }
    public Integer getIssuedBy() {
        return issuedBy;
    }
    public void setIssuedBy(Integer issuedBy) {
        this.issuedBy = issuedBy;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    
}