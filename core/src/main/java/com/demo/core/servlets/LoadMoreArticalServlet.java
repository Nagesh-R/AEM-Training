package com.demo.core.servlets;


import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.google.gson.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Load More Data Servlet",
                /* "sling.servlet.methods=" + HttpConstants.METHOD_POST,*/
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "aem-demo/components/content/article",
                "sling.servlet.selectors=" + "loadServlet",
                "sling.servlet.extensions=" + "json"
        })
public class LoadMoreArticalServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(LoadMoreArticalServlet.class);

    @Reference
    private QueryBuilder queryBuilder;

    private boolean resultComparision;

    private Session session;
    ArrayList arrayList = new ArrayList();

    public ArrayList getArrayList() {
        return arrayList;
    }

    public boolean getResultComparision() {
        return resultComparision;
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Resource resource = request.getResource();
        response.setContentType("application/json");
        ResourceResolver resourceResolver = resource.getResourceResolver();
        String[] offsetValue = request.getRequestPathInfo().getSelectors();
        Map<String, String> map = new HashMap<>();
        int value = Integer.parseInt(offsetValue[1]) + 3;
        map.put("path", "/content/home");
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:template");
        map.put("property.value", "/apps/aem-demo/templates/article");
        map.put("p.limit", "3");
        map.put("p.offset", offsetValue[1]);
        map.put("orderby", "@jcr:created");
        map.put("orderby.sort", "desc");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
        SearchResult result = query.getResult();
        JsonArray getMoreElement = new JsonArray();
        Iterator<Resource> resourceIterator = result.getResources();
        Map<String, Object> mapJson = new HashMap<>();
        Gson gson = new Gson();
        while (resourceIterator.hasNext()) {
            Resource nodes = resourceIterator.next();
            Page page = nodes.adaptTo(Page.class);
            JsonObject jsonResponse = new JsonObject();
            if (page != null) {
                jsonResponse.addProperty("title", page.getTitle());
                jsonResponse.addProperty("description", (String) page.getProperties().get("jcr:description"));
                jsonResponse.addProperty("image", (String) page.getProperties().get("refrenceimage"));
                jsonResponse.addProperty("path", page.getPath());
                getMoreElement.add(jsonResponse);
            }
        }
        resultComparision = loadMore(result, value);
        mapJson.put("data", getMoreElement);
        mapJson.put("loadMore", (Object) resultComparision);
        String obj = gson.toJson(mapJson);
        response.getWriter().write(obj);
    }


    private boolean loadMore(SearchResult result, int hidden_value) {

        boolean showButton;

        if (hidden_value < result.getTotalMatches()) {
            showButton = true;
        } else {
            showButton = false;
        }
        return showButton;
    }

}

