package com.demo.core.content;


import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.util.*;

@Model(adaptables = Resource.class)
public class Article {

    private static final Logger LOG = LoggerFactory.getLogger(Article.class);
    @Self
    private Resource resource;
    @OSGiService
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


    @PostConstruct
    private void fetchArticleData() {

        ResourceResolver resourceResolver = resource.getResourceResolver();
        Map<String, String> map = new HashMap<String, String>();

        map.put("path", "/content/home");
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:template");
        map.put("property.value", "/apps/aem-demo/templates/article");
        map.put("p.limit", "3");
        map.put("p.offset", "0");
        map.put("orderby", "@jcr:created");
        map.put("orderby.sort", "desc");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
        SearchResult result = query.getResult();
        Iterator<Resource> resourceIterator = result.getResources();
        while (resourceIterator.hasNext()) {
            Resource nodes = resourceIterator.next();
            Page page = nodes.adaptTo(Page.class);
            Map<String, Object> getNodeMapValue = new HashMap<>();
            getNodeMapValue.put("title", page.getTitle());
            getNodeMapValue.put("description", page.getProperties().get("jcr:description"));
            getNodeMapValue.put("image", page.getProperties().get("refrenceimage"));
            getNodeMapValue.put("path", page.getPath());
            arrayList.add(getNodeMapValue);
        }
        resultComparision = loadMore(result, arrayList);
    }

    private boolean loadMore(SearchResult result, List<Map<String, String>> arrayList) {

        boolean showButton;

        if (result.getTotalMatches() > arrayList.size()) {
            showButton = true;
        } else {
            showButton = false;
        }
        return showButton;
    }
}
