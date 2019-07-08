package com.demo.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
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
import java.util.*;


@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Article Page Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "aem-demo/components/content/article",
                "sling.servlet.selectors=" + "article",
                "sling.servlet.extensions=" + "json"
        })
public class ArticleServlet extends SlingSafeMethodsServlet {
        private static Logger LOG = LoggerFactory.getLogger(ArticleServlet.class);

        @Reference
        private QueryBuilder queryBuilder;

        private boolean result;

        private List<Map<String, String>> userDataArray = new ArrayList<>();

        public List<Map<String, String>> getUserDataArray() {
                return userDataArray;
        }

        @Override
        protected void doGet(final SlingHttpServletRequest request,
                             final SlingHttpServletResponse response) throws ServletException, IOException {
                Gson gson = new Gson();

                ResourceResolver resourceResolver = request.getResourceResolver();
                String[] offsetValue = request.getRequestPathInfo().getSelectors();
                int articleCount = Integer.parseInt(offsetValue[1]) + 3;
                if (offsetValue.length == 2) {
                        Map<String, String> map = new HashMap<>();
                        map.put("path", "/content/demo");
                        map.put("type", "cq:Page");
                        map.put("property", "jcr:content/cq:template");
                        map.put("property.value", "/apps/aem-demo/templates/article");
                        map.put("p.limit", "3");
                        map.put("p.offset", offsetValue[1]);
                        map.put("orderby", "@jcr:created");
                        map.put("orderby.sort", "desc");


                        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
                        SearchResult searchResult = query.getResult();
                        List<Map<String, String>> array = new ArrayList<>();
                        Map<String, Object> responseData = new HashMap<>();
                        Iterator<Resource> resourceIterator = searchResult.getResources();
                        while (resourceIterator.hasNext()) {
                                Resource pageIterator = resourceIterator.next();
                                Page page = pageIterator.adaptTo(Page.class);
                                if (page != null) {
                                        ValueMap childPageMap = page.getProperties();
                                        Map<String, String> data = new HashMap<>();
                                        data.put("title", page.getTitle());
                                        data.put("description", childPageMap.get("jcr:description").toString());
                                        data.put("articlePath", page.getPath() + ".html");
                                        data.put("articleImage", childPageMap.get("filereference",
                                                "https://www.visa.com/images/merchantoffers/2018-03/1522070876523.Heritage-Resorts-logo-400x300.jpg"));
                                        array.add(data);
                                }
                        }
                        boolean hideButton = hideButton(searchResult, articleCount);
                        responseData.put("data", array);
                        responseData.put("boolean", hideButton);
                        response.setContentType("application/json");
                        response.getWriter().write(gson.toJson(responseData));
                }
                else {
                        System.out.println("OffSet value should not Empty");
                }
        }



        private boolean hideButton(SearchResult result, int articleCount) {
                boolean showLoadMore;
                if (result.getTotalMatches() <= articleCount) {
                        showLoadMore = true;

                } else {
                        showLoadMore = false;
                }
                return showLoadMore;
        }
}