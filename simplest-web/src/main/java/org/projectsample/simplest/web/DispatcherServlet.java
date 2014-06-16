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
package org.projectsample.simplest.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.projectsample.simplest.web.exception.ActionNotFoundException;

/**
 * A dispatcher of a web request.
 * 
 * <p>
 * Forward a web request to one servlet, meanwhile add a parameter named urlCp. Here, the dispatcher 
 * will read the configuration information from a attribute named actions of the ServletContext.
 * 
 * @author Jason Xing
 */
@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        String path = this.getPath(request);
        ServletContext context = request.getServletContext();
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> actions = 
            (Map<String, Map<String, String>>)context.getAttribute("actions");
        if (actions.containsKey(path)) {
            Map<String, String> action = actions.get(path);
            String url = action.get("url");
            String urlCp =  action.get("urlCp");
            // It's not necessary to combine other parameters, they are included in the request.
            String forwardTo = url + "?urlCp=" + urlCp;
            request.getRequestDispatcher(forwardTo).forward(request, response);
        } else {
            throw new ActionNotFoundException();
        }
    }
    
    private String getPath(HttpServletRequest request) {
        // For example: http://localhost:8080/ProjectName/Path?p1=v1&p2=v2.
        // appPath: ProjectName. 
        // If the project is the default project of the web server, appPath will be a empty string.
        String appPath = request.getContextPath();
        // uri: ProjectName/Path
        // uri will exclude "?p1=v1&p2=v2".
        String uri = request.getRequestURI();
        // The returned value starts with "/".
        return uri.replaceAll(appPath, "");
    }
}
