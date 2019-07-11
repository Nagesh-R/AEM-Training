package com.demo.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Article Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "aem-demo/components/content/articlecomponent",
                "sling.servlet.selectors=" + "article",
                "sling.servlet.extensions=" + "json"
        })
public class ArticleServlet extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleServlet.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response) throws ServletException, IOException {
        LOG.debug("Flow control entered inside a GET method of ArticleServlet");
        ResourceResolver resourceResolver = request.getResourceResolver();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        String[] selector = request.getRequestPathInfo().getSelectors();
        if (selector != null) {
            Map<String, String> nodeProperties = new HashMap<>();
            nodeProperties.put("path", "/content");
            nodeProperties.put("type", "cq:Page");
            nodeProperties.put("property", "jcr:content/cq:template");
            nodeProperties.put("property.value", "/apps/aem-demo/templates/articlepage");
            nodeProperties.put("orderby", "@jcr:created");
            nodeProperties.put("orderby.sort", "desc");
            nodeProperties.put("p.limit", "3");
            nodeProperties.put("p.offset", selector[1]);

            Query query = queryBuilder.createQuery(PredicateGroup.create(nodeProperties), resourceResolver.adaptTo(Session.class));
            SearchResult result = query.getResult();

            Iterator<Resource> parentNode = result.getResources();
            Gson gson = new Gson();
            Map<String, Object> resultMap = new HashMap<>();
            JsonArray nodepropertiesarray = new JsonArray();
            while (parentNode.hasNext()) {
                Resource childNode = parentNode.next();
                Page page = childNode.adaptTo(Page.class);
                if (page != null) {
                    JsonObject addNodeProperties = new JsonObject();
                    addNodeProperties.addProperty("title", page.getTitle());
                    addNodeProperties.addProperty("description", page.getProperties().get("jcr:description", StringUtils.EMPTY));
                    addNodeProperties.addProperty("path", page.getPath());
                    addNodeProperties.addProperty("image", page.getProperties().get("filereference", "https://www.visa.com/images/merchantoffers/2018-03/1522070876523.Heritage-Resorts-logo-400x300.jpg"));
                    nodepropertiesarray.add(addNodeProperties);

                } else {
                    LOG.warn("childnode of path " + childNode.getPath() + " is not a valid page");
                }

            }
            int count = Integer.parseInt(selector[1]) + 3;
            Boolean showMoreResult = hideShowMore(result, count);
            resultMap.put("data", nodepropertiesarray);
            resultMap.put("resultShowMore", showMoreResult);
            String gsonresult = gson.toJson(resultMap);
            response.getWriter().write(gsonresult);
        }
    }

    private boolean hideShowMore(SearchResult result, int count) {
        long countPage = result.getTotalMatches();
        boolean ShowMore;
        if (count <= countPage) {
            ShowMore = true;
        } else {
            ShowMore = false;
        }
        return ShowMore;


    }

}
