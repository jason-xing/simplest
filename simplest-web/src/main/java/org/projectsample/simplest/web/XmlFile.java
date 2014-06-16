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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A xml file.
 * 
 * It's used for loading and parsing a xml file.
 * 
 * @author Jason Xing
 */
public class XmlFile {

    private Element root = null;
    
    public void load(String filePath) throws LoadXmlFileException {
        File file = new File(filePath);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            root = doc.getDocumentElement();
        } catch (SAXException e) {
            throw new LoadXmlFileException();
        } catch (IOException e) {
            throw new LoadXmlFileException();
        } catch (ParserConfigurationException e) {
            throw new LoadXmlFileException();
        }
    }    

    public Element getRoot() {
        return root;
    }

    /**
     * Get the child node list of the given node.
     * 
     * @param node The node
     * 
     * @return If the node has no children, return a empty list.
     */
    public List<Node> getChildList(Node node) {
        List<Node> list = new ArrayList<Node>();
        if (node.hasChildNodes()) {
            NodeList childList = node.getChildNodes();
            if (childList != null) {
                for (int i = 0; i < childList.getLength(); i++) {
                    Node child = childList.item(i);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        list.add(child);
                   }
                }
            }
        }
        return list;
    }
    
    /**
     * Get one child node of the given node by the name of the child.
     * 
     * @param node The node
     * @param childName The name of the child
     * 
     * @return If the node has no children, return null.
     *         If the node hasn't the child named childName, return null.
     */
    public Node getChildByChildName(Node node, String childName) {
        if (node.hasChildNodes()) {
            for (Node child = node.getFirstChild(); child !=null; 
                    child = child.getNextSibling()) {
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    if (child.getNodeName().equals(childName)) {
                        return child;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Get the attribute node of the given node by the name of the attribute.
     * 
     * @param node The node
     * @param attrName The name of the attribute
     * 
     * @return If the node has no attributes, return null.<br>
     *         If the node hasn't the attribute named attrName, return null.
     */
    public Node getAttrByAttrName(Node node, String attrName) {
        if (node.hasAttributes()) {
            NamedNodeMap attrMap = node.getAttributes();
            return attrMap.getNamedItem(attrName);
        }
        return null;
    }
    
}
