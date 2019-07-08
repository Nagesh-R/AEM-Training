package com.demo.core.servlets;

import com.demo.core.services.FormLimitDataDisplay;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
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
                "sling.servlet.selectors=" + "createFormNode",
                "sling.servlet.selectors=" + "deleteFormNode",
                "sling.servlet.selectors=" + "editNode",
                "sling.servlet.extensions=" + "json"
        })

public class FormServlet extends SlingAllMethodsServlet {
    private Logger LOG = LoggerFactory.getLogger(FormServlet.class);

   /* public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String AGE = "Age";*/

    @Reference
    private FormLimitDataDisplay formDataDisplayService;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {
        String selector = req.getRequestPathInfo().getSelectorString();
        if (selector.equals("createFormNode")) {
            createFormNode(req, resp);
        } else if (selector.equals("deleteFormNode")) {
            deleteNode(req, resp);
        } else if (selector.equals("editNode")) {
            editNode(req, resp);
        }
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            Resource resource = request.getResource();
            JsonArray array = new JsonArray();
            Iterator<Resource> parentNode = resource.listChildren();
            int limitCount=0;
            while (parentNode.hasNext() && limitCount < formDataDisplayService.DisplayLimitCount()) {
                Resource childNode = parentNode.next();
                ValueMap childValueMap = childNode.getValueMap();
                JsonObject data = new JsonObject();
                data.addProperty("FirstName", childValueMap.get("FirstName", String.class));
                data.addProperty("LastName", childValueMap.get("LastName", String.class));
                data.addProperty("Age", childValueMap.get("Age", String.class));
                data.addProperty("Gender", childValueMap.get("Gender",String.class));
                data.addProperty("resourcePath", childNode.getPath());
                array.add(data);
                limitCount++;
            }
            response.setContentType("application/json");
            response.getWriter().print(array.toString());
            response.getWriter().close();
        } catch (Exception e) {
            LOG.error("Exception caught in doGet Method", e);
        }
    }

    private void createFormNode(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        try {
            ResourceResolver resourceResolver = req.getResourceResolver();
            String nodePath = req.getParameter("resourcePath");
            Resource resource1 = null;
            if (nodePath != null && !nodePath.equals("")) {
                resource1 = resourceResolver.getResource(nodePath);
            }
            String first_name = req.getParameter("fname");
            String last_name = req.getParameter("lname");
            String age = req.getParameter("age");
            String gender = req.getParameter("gender");
            Map detail = new HashMap();
            detail.put("FirstName", first_name);
            detail.put("LastName", last_name);
            detail.put("Age", age);
            detail.put("Gender",gender);

            if (resource1 == null) {
                if (countNode(req.getResource()) < formDataDisplayService.DisplayCount()) {
                    ResourceUtil.getOrCreateResource(req.getResource().getResourceResolver(), req.getResource().getPath().concat("/").concat(first_name) + String.valueOf(Math.random()).substring(2, 8), detail, null, true);
                } else {
                    JsonObject data = new JsonObject();
                    data.addProperty("status", "error");
                    data.addProperty("message", "Maximum nodes are already created");
                    resp.setContentType("application/json");
                    resp.getWriter().print(data.toString());
                    resp.getWriter().close();
                }
            } else {
                ModifiableValueMap modifiableValueMap = resource1.adaptTo(ModifiableValueMap.class);
                modifiableValueMap.put("FirstName", req.getParameter("fname"));
                modifiableValueMap.put("LastName", last_name);
                modifiableValueMap.put("Age", age);
                modifiableValueMap.put("Gender", gender);
                resourceResolver.commit();
                JsonObject data = new JsonObject();
                data.addProperty("status", "edit");
                data.addProperty("editmessage", "the node has been edited");
                resp.setContentType("application/json");
                resp.getWriter().print(data.toString());
            }
        } catch (Exception e) {
            LOG.error("Exception caught in createFormNode Servlet", e);
        }
    }

    private void deleteNode(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            String nodePath = request.getParameter("resourcePath"); //rResource will accept only the string
            ResourceResolver resourceResolver = request.getResourceResolver();
            Resource node = resourceResolver.getResource(nodePath);
            resourceResolver.delete(node);
            resourceResolver.commit();

        } catch (Exception e) {
            LOG.error("Exception caught in deleteNode Servlet", e);
        }
    }

    private void editNode(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            response.setContentType("application/json");
            String nodePath = request.getParameter("resourcePath");
            Resource resource = resourceResolver.getResource(nodePath);
            JsonObject dataFetch = new JsonObject();
            dataFetch.addProperty("FirstName", resource.getValueMap().get("FirstName", String.class));
            dataFetch.addProperty("LastName", resource.getValueMap().get("LastName", String.class));
            dataFetch.addProperty("Age", resource.getValueMap().get("Age", String.class));
            dataFetch.addProperty("Gender", resource.getValueMap().get("Gender", String.class));
            response.getWriter().print(dataFetch);
        } catch (Exception e) {
            LOG.error("Exception caught in editNode Servlet", e);
        }

    }

    private int countNode(Resource resource) {
        Iterator<Resource> parentNode = resource.listChildren();
        int count = 0;
        while (parentNode.hasNext()) {
            Resource childNode = parentNode.next();
            count++;
        }
        return count;
    }
}