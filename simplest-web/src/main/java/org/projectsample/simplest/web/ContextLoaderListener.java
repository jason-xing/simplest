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

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Load the configuration file simplest-web.xml when the ServletContext is 
 * initialized.
 * 
 * It will add an attribute named actions to the servletContext.
 * <p>
 * The actions is a map, it's key is path and it's value is action.
 * The action is also a map, it's key is: "path", "url", "urlCp".
 * 
 * @author Jason Xing
 */
public class ContextLoaderListener implements ServletContextListener {
    
    public void contextInitialized(ServletContextEvent arg0) {
        initConfFile(arg0.getServletContext());        
    }
    
    private void initConfFile(ServletContext servletContext) {
        Map<String, Map<String, String>> actions = 
                new HashMap<String, Map<String, String>>();
        URL confFileUrl = getClass().getClassLoader().
                getResource("simplest-web.xml");
        if (confFileUrl == null) {
            throw new RuntimeException("The simplest-web.xml does't exist.");
        }
        String confFilePath = confFileUrl.getPath();
        XmlFile xmlFile = new XmlFile();
        try {
            xmlFile.load(confFilePath);
        } catch (LoadXmlFileException e) {
            throw new RuntimeException("Failed to load the simplest-web.xml.");
        }
        Element root = xmlFile.getRoot();
        Node actionsNode = xmlFile.getChildByChildName(root, "actions");
        List<Node> actionNodes = xmlFile.getChildList(actionsNode);
        for (Node actionNode : actionNodes) {
            Node pathNode = xmlFile.getAttrByAttrName(actionNode, "path");
            Node urlNode = xmlFile.getAttrByAttrName(actionNode, "url");
            Node urlCpNode = xmlFile.getAttrByAttrName(actionNode, "urlCp");
            String urlCp = urlCpNode == null ? "" : urlCpNode.getNodeValue();
            Map<String, String> action = new HashMap<String, String>();
            action.put("path", pathNode.getNodeValue());
            action.put("url", urlNode.getNodeValue());
            action.put("urlCp", urlCp);
            actions.put(pathNode.getNodeValue(), action);
        }
        servletContext.setAttribute("actions", actions);
    }
    
    public void contextDestroyed(ServletContextEvent arg0) {
    }

}
