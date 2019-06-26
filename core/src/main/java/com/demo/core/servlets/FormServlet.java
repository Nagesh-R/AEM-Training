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
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Form Servlet based on Resource Path",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "aem-demo/components/content/form",
                "sling.servlet.selectors=" + "creatingnodeservlet",
                "sling.servlet.selectors=" + "updatingnodedataservlet",
                "sling.servlet.selectors=" + "deletingnodeervlet",
                "sling.servlet.extensions=" + "json"
        })
public class FormServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(FormServlet.class);
    public static final String LAST_NAME = "lastName";
    public static final String DOB = "dob";
    public static final String AGE = "age";
    public static final String ADDRESS = "address";
    public static final String FIRSTNAME = "firstname";
    public static final String PATH = "path";

    @Override
    protected void doPost(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) throws ServletException, IOException {
        String selector = request.getRequestPathInfo().getSelectorString();
        if(selector.equals("formservlet")){
            creatingNode(request,response);
        }else if(selector.equals("updatingnodedataservlet")){
            updateNode(request,response);
        }else if(selector.equals("deletingnodeervlet")){
            deleteNode(request,response);

        }

    }
    private void creatingNode(final SlingHttpServletRequest request,
                              final SlingHttpServletResponse response){
        try {
            LOG.info("Enter inside Form servlet");
            Resource resource = request.getResource();
            ResourceResolver resourceResolver = request.getResourceResolver();
            PrintWriter out = response.getWriter();
            String formpath = resource.getPath() + '/' + String.valueOf(Math.random()).substring(2,8);
            Map<String, Object> hashmap = new HashMap<String, Object>();
            hashmap.put(FIRSTNAME, request.getParameter(LAST_NAME));
            hashmap.put(DOB, request.getParameter(DOB));
            hashmap.put(AGE, request.getParameter(AGE));
            hashmap.put(ADDRESS, request.getParameter(ADDRESS));
            String existingpath = request.getParameter("path");
            Resource resourcepath = resourceResolver.getResource(existingpath);
            if(resourcepath == null ) {
                Resource createNewNode = ResourceUtil.getOrCreateResource(resourceResolver, formpath, hashmap, "", true);
            }else {
                ModifiableValueMap modifiableValueMap = resourceResolver.getResource(existingpath).adaptTo(ModifiableValueMap.class);
                modifiableValueMap.put(FIRSTNAME, request.getParameter(LAST_NAME));
                modifiableValueMap.put(DOB, request.getParameter(DOB));
                modifiableValueMap.put(AGE, request.getParameter(AGE));
                modifiableValueMap.put(ADDRESS, request.getParameter(ADDRESS));
                resourceResolver.commit();


            }
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            Resource resource = request.getResource();
            response.setContentType("application/json");
            Iterator<Resource> formparentnode = resource.listChildren();
            JsonArray jsonArray = new JsonArray();
            while (formparentnode.hasNext()) {
                Resource childnode = formparentnode.next();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(FIRSTNAME, childnode.getValueMap().get(FIRSTNAME, String.class));
                jsonObject.addProperty(DOB, childnode.getValueMap().get(DOB, String.class));
                jsonObject.addProperty(AGE, childnode.getValueMap().get(AGE, String.class));
                jsonObject.addProperty(ADDRESS, childnode.getValueMap().get("address", String.class));
                jsonObject.addProperty(PATH, childnode.getPath());
                jsonArray.add(jsonObject);
            }
            response.getWriter().write(jsonArray.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void  updateNode(final SlingHttpServletRequest request,final SlingHttpServletResponse response)throws ServletException, IOException{
        try{
            ResourceResolver resourceResolver = request.getResourceResolver();
            String path = request.getParameter("path");
            Resource resourcepath = resourceResolver.getResource(path);
            response.setContentType("application/json");
            JsonObject jsonObject = new JsonObject();
            if(resourcepath!=null){
                ValueMap valueMap = resourcepath.getValueMap();
                jsonObject.addProperty(FIRSTNAME,valueMap.get(FIRSTNAME).toString());
                jsonObject.addProperty(DOB,valueMap.get("dob").toString());
                jsonObject.addProperty(AGE,valueMap.get(AGE).toString());
                jsonObject.addProperty(ADDRESS,valueMap.get(ADDRESS).toString());

            }
            response.getWriter().write(jsonObject.toString());
            resourceResolver.commit();

    }catch(Exception e){
            e.printStackTrace();
            LOG.info("Error in Updating node"+e.getMessage());
        }
    }

    private void deleteNode(final SlingHttpServletRequest request,final SlingHttpServletResponse response) {
        try{
            ResourceResolver resourceResolver = request.getResourceResolver();
            String path = request.getParameter("path");
            Resource resourcepath = resourceResolver.getResource(path);
            if(resourcepath!=null){
                resourceResolver.delete(resourcepath);
                resourceResolver.commit();
            }
        }catch(Exception e){
            e.printStackTrace();
            LOG.info("Error in deleteNode: "+e.getMessage());
        }
    }


}
