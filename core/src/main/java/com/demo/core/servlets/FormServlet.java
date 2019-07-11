package com.demo.core.servlets;

import com.day.cq.search.QueryBuilder;
import com.demo.core.service.FormDataLimitService;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Simple Form Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "aem-demo/components/content/formdata",
                "sling.servlet.selectors=" + "formservlet",
                "sling.servlet.selectors=" + "editform",
                "sling.servlet.selectors=" + "deleteform",
                "sling.servlet.extensions=" + "json"
        })
public class FormServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(FormServlet.class);
    @Reference
    private FormDataLimitService formDataLimitService;


    private static final long serialVersionUID = 1L;

    Resource resource;
    ResourceUtil resourceUtil;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Form Servlet");
        try {

            String selector = req.getRequestPathInfo().getSelectorString();
            if (selector.equals("formservlet")) {
                creatingNode(req, resp);
            } else if (selector.equals("editform")) {
                editNode(req, resp);
            } else if (selector.equals("deleteform")) {
                deleteNode(req, resp);
            }

        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public void creatingNode(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            resource = request.getResource();
            String path = resource.getPath() + '/' + Math.random();
            ResourceResolver resourceResolver = request.getResourceResolver();
            String first_name = request.getParameter("firstName");
            String last_name = request.getParameter("lastName");
            String age = request.getParameter("age");
            String gender=request.getParameter("gender");
            Map < String, Object > map = new HashMap < String, Object > ();
            map.put("first_name", first_name);
            map.put("last_name", last_name);
            map.put("age", age);
            map.put("gender",gender);
            response.setContentType("application/json");
            Resource resourceNode = resourceResolver.getResource(request.getParameter("resourcePath"));

            if (resourceNode == null) {
                if (countNode(resource) < formDataLimitService.getDataCount()) {
                    Resource resourceNodeProperties = resourceUtil.getOrCreateResource(resourceResolver, path, map, "", true);
                    JsonObject newResourceCreationJsonResponse = new JsonObject();
                    newResourceCreationJsonResponse.addProperty("status", "success");
                    newResourceCreationJsonResponse.addProperty("message", "data saved successfully");
                    response.getWriter().print(newResourceCreationJsonResponse.toString());
                } else {
                    JsonObject resoureceLimitExceedJsonResponse = new JsonObject();
                    resoureceLimitExceedJsonResponse.addProperty("status", "error");
                    resoureceLimitExceedJsonResponse.addProperty("message", "Can not create more then 5 nodes :");
                    response.getWriter().print(resoureceLimitExceedJsonResponse.toString());
                }

            } else {
                final ModifiableValueMap properties = resourceNode.adaptTo(ModifiableValueMap.class);
                properties.put("first_name", request.getParameter("firstName"));
                properties.put("last_name", request.getParameter("lastName"));
                properties.put("age", request.getParameter("age"));
                resourceResolver.commit();
                JsonObject modifiedResourceJsonResponse = new JsonObject();
                modifiedResourceJsonResponse.addProperty("status", "edit");
                modifiedResourceJsonResponse.addProperty("message", "data has been updated");
                response.getWriter().print(modifiedResourceJsonResponse.toString());
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public void editNode(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            //resource = request.getResource();
            ResourceResolver resourceResolver = request.getResourceResolver();
            response.setContentType("application/json");
            Resource pathForEditNode = resourceResolver.getResource(request.getParameter("nodePath"));
            JsonObject editNodeJsonResponse = new JsonObject();
            editNodeJsonResponse.addProperty("first_name", pathForEditNode.getValueMap().get("first_name", String.class));
            editNodeJsonResponse.addProperty("last_name", pathForEditNode.getValueMap().get("last_name", String.class));
            editNodeJsonResponse.addProperty("age", pathForEditNode.getValueMap().get("age", String.class));
            response.getWriter().write(editNodeJsonResponse.toString());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public void deleteNode(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
           // resource = request.getResource();
            ResourceResolver resourceResolver = request.getResourceResolver();
            Resource pathForDeleteNode = resourceResolver.getResource(request.getParameter("nodePath"));
            if (pathForDeleteNode != null) {
                resourceResolver.delete(pathForDeleteNode);
                resourceResolver.commit();
                response.getWriter().println("Node deleted successfully:");
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

    }

    public int countNode(Resource resource) {
        int nodeCount = 0;
        Iterator < Resource > resourceIterator = resource.listChildren();
        while (resourceIterator.hasNext()) {
            Resource childnode = resourceIterator.next();
            nodeCount++;
        }
        return nodeCount;
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            resource = request.getResource();
            response.setContentType("application/json");
            JsonArray getAllNodesInArray = new JsonArray();
            Iterator<Resource> resourceIterator = resource.listChildren();
           int limitNodeCount =0;
                while (resourceIterator.hasNext()&& limitNodeCount<formDataLimitService.getDataWithLimit()) {
                    Resource childnode = resourceIterator.next();
                    String nodePath = childnode.getPath();
                    JsonObject allNodesJsonResponse = new JsonObject();
                    allNodesJsonResponse.addProperty("first_name", childnode.getValueMap().get("first_name", String.class));
                    allNodesJsonResponse.addProperty("last_name", childnode.getValueMap().get("last_name", String.class));
                    allNodesJsonResponse.addProperty("age", childnode.getValueMap().get("age", String.class));
                    allNodesJsonResponse.addProperty("gender",childnode.getValueMap().get("gender",String.class));
                    allNodesJsonResponse.addProperty("path", nodePath);
                    getAllNodesInArray.add(allNodesJsonResponse);
                    limitNodeCount++;
                }
            response.getWriter().write(getAllNodesInArray.toString());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }
}