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
package org.projectsample.simplest.web.exception;

/**
 * Super class of all web exceptions.
 * 
 * @author Jason Xing
 */
@SuppressWarnings("serial")
public class WebException extends RuntimeException {
  
   public WebException() {
       super();
   }

   public WebException(String message) {
       super(message);
   }

   public WebException(Exception e) {
       super(e.toString());
   }
   
}