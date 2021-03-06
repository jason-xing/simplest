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
package org.projectsample.simplest.s1.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.projectsample.simplest.s1.security.data.Destroy;
import org.projectsample.simplest.s1.security.data.Initialize;

/**
 * @author Jason Xing
 */
public class SecurityDataListener implements ServletContextListener {
    
    public void contextInitialized(ServletContextEvent arg0) {
        Initialize initialize = new Initialize();
        initialize.createTables();
        initialize.initData();
    }
    
    public void contextDestroyed(ServletContextEvent arg0) {
        Destroy destroy = new Destroy();
        destroy.deleteData();
        destroy.dropTables();
    }
    
}
