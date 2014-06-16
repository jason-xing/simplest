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

/**
 * DB Exception. It is used to test.
 * 
 * @author Jason Xing
 */
@SuppressWarnings("serial")
public class DbException extends RuntimeException {
  
   public DbException() {
       super();
   }

   public DbException(String message) {
       super(message);
   }

   public DbException(Exception e) {
       super(e.toString());
   }
   
}
