package com.demo.core.content;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.util.*;

@Model(adaptables = Resource.class)
public class ArticleModel {

    @Self
    private Resource resource;

    @OSGiService
    private QueryBuilder queryBuilder;

    private List < Map < String, String >> userDataArray = new ArrayList < > ();
    private boolean result;

    public List < Map < String, String >> getUserDataArray() {
        return userDataArray;
    }

    public boolean isResult() {
        return result;
    }

    @PostConstruct
    private void articleCard() {
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Map < String, String > map = new HashMap < > ();
        map.put("path", "/content/demo");
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:template");
        map.put("property.value", "/apps/aem-demo/templates/article");
        map.put("orderby", "@jcr:created");
        map.put("orderby.sort", "desc");
        map.put("p.limit", "3");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
        SearchResult searchResult = query.getResult();

        Iterator < Resource > resourceIterator = searchResult.getResources();
        while (resourceIterator.hasNext()) {
            Resource pageIterator = resourceIterator.next();
            Page page = pageIterator.adaptTo(Page.class);
            if (page != null) {
                ValueMap childPageMap = page.getProperties();
                Map < String, String > data = new HashMap < > ();
                data.put("title", page.getTitle());
                data.put("description", childPageMap.get("jcr:description").toString());
                data.put("articlePath", page.getPath() + ".html");
                data.put("articleImage", childPageMap.get("filereference",
                        "https://www.visa.com/images/merchantoffers/2018-03/1522070876523.Heritage-Resorts-logo-400x300.jpg"));
                userDataArray.add(data);
            }
        }
        result = loadMore(searchResult, userDataArray);
    }
    private boolean loadMore(SearchResult result, List < Map < String, String >> userDataArray) {
        boolean showLoadMore;
        if (result.getTotalMatches() > userDataArray.size()) {
            showLoadMore = true;

        } else {
            showLoadMore = false;
        }
        return showLoadMore;
    }
}