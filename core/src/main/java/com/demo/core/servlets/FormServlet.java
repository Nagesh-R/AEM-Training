/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.demo.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



@Component(service=Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Servlet for Form Component",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes="+ "aem-demo/components/content/form",
                "sling.servlet.selectors=" + "form-servlet",
                "sling.servlet.selectors=" + "deleteNode",
                "sling.servlet.selectors=" + "editNode",
                "sling.servlet.extensions=" + "json"
        })
public class FormServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(FormServlet.class);


    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {
        String selector = req.getRequestPathInfo().getSelectorString();
        if(selector.equals("form-servlet"))
        {
            createNode(req, resp);
        }
        else if(selector.equals("deleteNode"))
        {
            deleteNode(req, resp);
        }
        else if(selector.equals("editNode"))
        {
            editNode(req,resp);
        }


    }
    private void createNode(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException
    {
        ResourceResolver resolver = req.getResourceResolver();

        String FirstName = req.getParameter("firstname");
        String LastName = req.getParameter("lastname");
        String Age = req.getParameter("age");

        String path = req.getParameter("nPath"); //added after ajax exist path declaration.

        Resource resource = null;
        if(!path.equals("") && path != null )
        {
            resource = resolver.getResource(path);
        }

        resp.getWriter().println(FirstName);
        resp.getWriter().println(LastName);
        resp.getWriter().print(Age);

        Map resourceProperties = new HashMap();
        resourceProperties.put("FirstName", FirstName);
        resourceProperties.put("LastName", LastName);
        resourceProperties.put("Age", Age);
        if(resource == null)
        {
        ResourceUtil.getOrCreateResource(resolver,
                req.getResource().getPath().concat("/data_") + String.valueOf(Math.random()).substring(2, 7),
                resourceProperties, null,
                true);
        }
        else
        {
            ModifiableValueMap properties = resolver.getResource(path).adaptTo(ModifiableValueMap.class);//for modifying the properties of the node whose path is known-- nPath.
            properties.put("FirstName", FirstName);
            properties.put("LastName", LastName);
            properties.put("Age", Age);
            resolver.commit();
        }

    }

    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException
    {

        try
        {
            JsonArray childArray = new JsonArray();
            Resource resource = req.getResource();
            Iterator<Resource> main = resource.listChildren();
            while (main.hasNext())
            {
                Resource child   = main.next();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("firstname", child.getValueMap().get("FirstName", String.class));
                jsonObject.addProperty("lastname", child.getValueMap().get("LastName", String.class));
                jsonObject.addProperty("age", child.getValueMap().get("Age", String.class));

                jsonObject.addProperty("nodePath", child.getPath());
                childArray.add(jsonObject);
                LOG.info("......." + child);

            }
            resp.setContentType("application/json");
            resp.getWriter().write(childArray.toString());

        }
        catch(Exception e)
        {
            LOG.error("Exception caught in doGet method of FormServlet",e);
        }

    }

    private void deleteNode(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException
    {

        ResourceResolver resolver = req.getResourceResolver();
        String path = req.getParameter("nodePath");
        Resource resource = resolver.getResource(path);
        if (resource != null) {
            resolver.delete(resource);
            resolver.commit();
        }


    }

    private void editNode(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException
    {

        ResourceResolver resolver = req.getResourceResolver();
        String path = req.getParameter("nodePath");
        Resource resource = resolver.getResource(path);
        resp.setContentType("application/json");
        JsonObject modified = new JsonObject();
        if(resource != null)
        {
            modified.addProperty("firstname", resource.getValueMap().get("FirstName", String.class));
            modified.addProperty("lastname", resource.getValueMap().get("LastName", String.class));
            modified.addProperty("age", resource.getValueMap().get("Age", String.class));
        }
        resp.getWriter().write(modified.toString());
        resolver.commit();
    }


}

