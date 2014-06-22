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
package org.projectsample.simplest.s1.web.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.projectsample.simplest.s1.security.exception.InvalidUrlException;
import org.projectsample.simplest.s1.security.exception.NoPermissionException;
import org.projectsample.simplest.s1.security.exception.SessionInvalidException;
import org.projectsample.simplest.s1.security.service.ResourceService;
import org.projectsample.simplest.s1.security.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security filter.
 * 
 * @author Jason Xing
 */
public class SecurityFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    private RoleService roleService = new RoleService();
    private ResourceService resourceService = new ResourceService();

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpSession session = req.getSession(false);
        String path = this.getPath(req);
        // Filter only when the path is not empty.
        if (!path.equals("")) {
            // Check whether the path is a valid URL(one of all the registered URLs).
            if (!resourceService.isValidUrl(path)) {
                throw new InvalidUrlException();
            }
            // All the URLs of Anonymous
            Set<String> urlsOfAnonymous = roleService.getUrlsOfAnonymous();
            // The path is in the URLs of Anonymous. It can pass.
            if (urlsOfAnonymous.contains(path)) {
                
            } else {
                // the session is invalid.
                // Such as: Not login, logout, session timeout
                if (session == null) {
                    throw new SessionInvalidException();
                // Already login
                } else if (session.getAttribute("user") != null) {
                    @SuppressWarnings("unchecked")
                    Set<String> urls = (Set<String>)session.getAttribute("urls");
                    // Throw NoPermissionException if the path is neither in the URLs of the user nor in the URLs of Anonymous.
                    if (!urls.contains(path) && !urlsOfAnonymous.contains(path)) {
                        throw new NoPermissionException();
                    }
                } else {
                    throw new RuntimeException();
                }
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }
    
    /**
     * For example:
     * if the request URL is "http://localhost:8080/ProjectName/Path?p1=v1&p2=v2",
     * the returned will be "Path".
     */
    private String getPath(HttpServletRequest request) {
        // For example: http://localhost:8080/ProjectName/Path?p1=v1&p2=v2.
        // appPath: ProjectName. 
        // If the project is the default project of the web server, appPath will be a empty string.
        String appPath = request.getContextPath();
        // uri is like this: ProjectName/Path. It will not include "?p1=v1&p2=v2".
        String uri = request.getRequestURI();
        // The returned value starts with "/".
        return uri.replaceAll(appPath, "");
    }

}
