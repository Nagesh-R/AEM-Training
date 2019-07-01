package com.demo.core.servlets;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
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
                //"sling.servlet.paths="+"/bin/aemtraning/formresourceservlet",
                "sling.servlet.selectors=" + "formservlet",
                "sling.servlet.selectors=" + "editform",
                "sling.servlet.selectors=" + "deleteform",
                "sling.servlet.extensions=" + "json"
        })
public class FormServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(FormServlet.class);

    private static final long serialVersionUID = 1L;



    Resource resource;
    ResourceUtil resourceUtil;

    @Override
    protected void doPost( final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {
        try {

            String selector = req.getRequestPathInfo().getSelectorString();
            if(selector.equals("formservlet")){
                creatingNode(req,resp);
            }else if(selector.equals("editform")){
                editNode(req,resp);
            }else if(selector.equals("deleteform")){
                deleteNode(req,resp);
            }

           /* resource = req.getResource();
            //resp.setContentType("application/json");
            if (req.getParameter("nodePath") == null) {
                String path = resource.getPath() + '/' + Math.random();
                ResourceResolver resourceResolver = req.getResourceResolver();

                String first_name = req.getParameter("firstName");
                String last_name = req.getParameter("lastName");
                String age = req.getParameter("age");
                Map < String, Object > map = new HashMap < String, Object > ();
                map.put("first_name", first_name);
                map.put("last_name", last_name);
                map.put("age", age);

                Resource resourceNode = resourceResolver.getResource(req.getParameter("resourcePath"));

                if (resourceNode == null) {

                    Resource resourceNodeProperties = resourceUtil.getOrCreateResource(resourceResolver, path, map, "", true);
                    resp.getWriter().println("data saved successfully");
                    LOG.info("data saved successfully");
                } else {
                    final ModifiableValueMap properties = resourceNode.adaptTo(ModifiableValueMap.class);
                    properties.put("first_name", req.getParameter("firstName"));
                    properties.put("last_name", req.getParameter("lastName"));
                    properties.put("age", req.getParameter("age"));
                    resourceResolver.commit();
                    resp.getWriter().println("data updated successfully");

                }
            } else {
                // To delete the resource
                ResourceResolver resourceResolver = req.getResourceResolver();
                Resource myResource = resourceResolver.getResource(req.getParameter("nodePath"));
                resourceResolver.delete(myResource);
                resourceResolver.commit();
                resp.getWriter().println("Node deleted successfully:");
            }
*/
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }


    }

    public void creatingNode(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            resource = request.getResource();
            String path = resource.getPath() + '/' + Math.random();
            ResourceResolver resourceResolver = request.getResourceResolver();

            String first_name = request.getParameter("firstName");
            String last_name = request.getParameter("lastName");
            String age = request.getParameter("age");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("first_name", first_name);
            map.put("last_name", last_name);
            map.put("age", age);

            Resource resourceNode = resourceResolver.getResource(request.getParameter("resourcePath"));

            if (resourceNode == null) {

                Resource resourceNodeProperties = resourceUtil.getOrCreateResource(resourceResolver, path, map, "", true);
                response.getWriter().println("data saved successfully");
                LOG.info("data saved successfully");
            } else {
                final ModifiableValueMap properties = resourceNode.adaptTo(ModifiableValueMap.class);
                properties.put("first_name", request.getParameter("firstName"));
                properties.put("last_name", request.getParameter("lastName"));
                properties.put("age", request.getParameter("age"));
                resourceResolver.commit();
                response.getWriter().println("data updated successfully");

            }
        }
        catch (Exception e)
        {
            LOG.info(e.getMessage());
        }
    }

    public void editNode(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException
    {
        try{
            resource = request.getResource();
            ResourceResolver resourceResolver = request.getResourceResolver();
            response.setContentType("application/json");

            Resource myResource = resourceResolver.getResource(request.getParameter("nodePath"));
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("first_name", myResource.getValueMap().get("first_name", String.class));
            jsonObject.addProperty("last_name", myResource.getValueMap().get("last_name", String.class));
            jsonObject.addProperty("age", myResource.getValueMap().get("age", String.class));
            response.getWriter().write(jsonObject.toString());

        }
        catch(Exception e)
        {
            LOG.info(e.getMessage());
        }

    }

    public void deleteNode(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException
    {
        try {
            resource = request.getResource();
            ResourceResolver resourceResolver = request.getResourceResolver();
            Resource myResource = resourceResolver.getResource(request.getParameter("nodePath"));
            resourceResolver.delete(myResource);
            resourceResolver.commit();
            response.getWriter().println("Node deleted successfully:");
        }
        catch(Exception e)
        {
            LOG.info(e.getMessage());
        }

    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        try {
            resource = request.getResource();
            response.setContentType("application/json");
            //if (request.getParameter("nodePath") == null) {
                JsonArray jsonarray = new JsonArray();
                Iterator < Resource > resourceIterator = resource.listChildren();

                while (resourceIterator.hasNext()) {
                    Resource childnode = resourceIterator.next();
                    String nodePath = childnode.getPath();

                    JsonObject json = new JsonObject();

                    json.addProperty("first_name", childnode.getValueMap().get("first_name", String.class));
                    json.addProperty("last_name", childnode.getValueMap().get("last_name", String.class));
                    json.addProperty("age", childnode.getValueMap().get("age", String.class));
                    json.addProperty("path", nodePath);
                    jsonarray.add(json);
                }

                response.getWriter().write(jsonarray.toString());
            /*} else {

                // Fetching value with respected node

                ResourceResolver resourceResolver = request.getResourceResolver();

                Resource myResource = resourceResolver.getResource(request.getParameter("nodePath"));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("first_name", myResource.getValueMap().get("first_name", String.class));
                jsonObject.addProperty("last_name", myResource.getValueMap().get("last_name", String.class));
                jsonObject.addProperty("age", myResource.getValueMap().get("age", String.class));
                response.getWriter().write(jsonObject.toString());
            }*/

        } catch (Exception e) {
            e.getMessage();
        }

    }
}