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
package org.projectsample.simplest.s1.security.bean;

public class Role {

    private Integer id;
    private String name;
    private Integer parentId;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public boolean equals(Object o) {   
        if (this == o) {   
            return true;   
        }   
        if (o.getClass() == Role.class) {   
            Role r = (Role)o;   
            return (r.id.compareTo(id) == 0);
        }   
        return false;   
    }   
       
    public int hashCode() {   
        return id.hashCode();   
    }  

}