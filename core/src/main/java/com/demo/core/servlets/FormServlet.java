package com.demo.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;


@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Form Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "aem-demo/components/content/form",
                "sling.servlet.selectors=" + "form",
                "sling.servlet.selectors=" + "deleteFormNode",
                "sling.servlet.selectors=" + "editNode",
                "sling.servlet.extensions=" + "json"
        })

public class FormServlet extends SlingAllMethodsServlet {
    private Logger LOG = LoggerFactory.getLogger(FormServlet.class);

    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String AGE = "Age";

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {
        String selector= req.getRequestPathInfo().getSelectorString();
        if (selector.equals("form"))
        {
            createFormNode(req,resp);
        }
        else if (selector.equals("deleteFormNode"))
        {
            deleteNode(req,resp);
        }
        else if (selector.equals("editNode"))
        {
            editNode(req,resp);
        }
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            Resource resource = request.getResource();
            JsonArray array = new JsonArray();

            Iterator < Resource > parentNode = resource.listChildren();
            while (parentNode.hasNext()) {
                Resource childNode = parentNode.next();
                ValueMap childValueMap= childNode.getValueMap();
                JsonObject data = new JsonObject();
                data.addProperty("First-Name", childValueMap.get(FIRST_NAME, String.class));
                data.addProperty("Last-Name", childValueMap.get(LAST_NAME, String.class));
                data.addProperty(AGE, childValueMap.get(AGE, String.class));
                data.addProperty("resourcePath",childNode.getPath());
                array.add(data);
            }
            response.setContentType("application/json");
            response.getWriter().print(array.toString());
            response.getWriter().close();
        } catch (Exception e) {
            LOG.error("Exception caught in doGet Method", e);
        }
    }

    private void createFormNode(SlingHttpServletRequest req, SlingHttpServletResponse resp){
        try {
            ResourceResolver resourceResolver= req.getResourceResolver();
            String nodePath = req.getParameter("resourcePath");
            Resource resource1 = null;
            if(nodePath != null && !nodePath.equals("")){
                resource1 = resourceResolver.getResource(nodePath);
            }
            String first_name = req.getParameter("fname");
            String last_name = req.getParameter("lname");
            String age = req.getParameter("age");
            Map detail = new HashMap();
            detail.put("FirstName", first_name);
            detail.put(LAST_NAME, last_name);
            detail.put(AGE, age);

            if(resource1==null) {
                ResourceUtil.getOrCreateResource(req.getResource().getResourceResolver(), req.getResource().getPath().concat("/userdetail") + String.valueOf(Math.random()).substring(2, 8), detail, null, true);
            }
            else {
                ModifiableValueMap modifiableValueMap = resource1.adaptTo(ModifiableValueMap.class);
                modifiableValueMap.put("FirstName", req.getParameter("fname"));
                modifiableValueMap.put(LAST_NAME, last_name);
                modifiableValueMap.put(AGE, age);
                resourceResolver.commit();
            }
        } catch (Exception e) {
            LOG.error("Exception caught in createFormNode Servlet", e);
        }
    }
    private  void deleteNode (SlingHttpServletRequest request,SlingHttpServletResponse response)
    {
        try
        {
            String nodePath = request.getParameter("resourcePath"); //rResource will accept only the string
            ResourceResolver resourceResolver= request.getResourceResolver();
            Resource node=resourceResolver.getResource(nodePath);
            resourceResolver.delete(node);
            resourceResolver.commit();

        }
        catch (Exception e) {
            LOG.error("Exception caught in deleteNode Servlet", e);
        }
    }
    private void editNode (SlingHttpServletRequest request,SlingHttpServletResponse response)
    {
        try
        {
            ResourceResolver resourceResolver= request.getResourceResolver();
            response.setContentType("application/json");
            String nodePath = request.getParameter("resourcePath");
            Resource resource =resourceResolver.getResource(nodePath);
            JsonObject dataFetch= new JsonObject();
            dataFetch.addProperty(FIRST_NAME,  resource.getValueMap().get(FIRST_NAME,String.class));
            dataFetch.addProperty(LAST_NAME,  resource.getValueMap().get(LAST_NAME,String.class));
            dataFetch.addProperty(AGE,  resource.getValueMap().get(AGE,String.class));

            response.getWriter().print(dataFetch);
        }
        catch (Exception e) {
            LOG.error("Exception caught in editNode Servlet", e);
        }

    }
}

