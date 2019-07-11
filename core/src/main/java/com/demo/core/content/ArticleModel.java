package com.demo.core.content;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
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
public class ArticleModel {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleModel.class);

    @OSGiService
    private QueryBuilder queryBuilder;

    @Self
    private Resource resource;

    private List<Map<String,String>> nodeProperties = new ArrayList<>();

    public List<Map<String,String>> getNodeProperties() {
        return nodeProperties;
    }

    private boolean conditionResult;

    public boolean isConditionResult() {
        return conditionResult;
    }


    @PostConstruct
    private void articleData() {
        LOG.debug("FOC entered articleData method");
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Map<String, String> map = new HashMap<>();
        map.put("path", "/content");
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:template");
        map.put("property.value", "/apps/aem-demo/templates/articlepage");
        map.put("orderby", "@jcr:created");
        map.put("orderby.sort", "desc");
        map.put("p.limit", "3");
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
        SearchResult result = query.getResult();


        Iterator<Resource> parentNode = result.getResources();
        while (parentNode.hasNext()) {
            Resource childNode = parentNode.next();
            Page page = childNode.adaptTo(Page.class);
            if (page != null) {
                Map<String, String> childNodeValues = new HashMap<>();
                childNodeValues.put("title", page.getTitle());
                childNodeValues.put("description", page.getProperties().get("jcr:description", StringUtils.EMPTY));
                childNodeValues.put("path", page.getPath()+".html");
                childNodeValues.put("image", page.getProperties().get("filereference","https://www.visa.com/images/merchantoffers/2018-03/1522070876523.Heritage-Resorts-logo-400x300.jpg"));
                nodeProperties.add(childNodeValues);
            } else {
                LOG.warn("childnode of path " + childNode.getPath() + " is not a valid page");
            }
            conditionResult = conditionCheck(result, nodeProperties);
        }
      }


    private boolean conditionCheck(SearchResult result,List nodeProperties){
        boolean showMore;
        long countPage = result.getTotalMatches();
        if (countPage > nodeProperties.size()) {
          showMore = true;
        } else {
            showMore = false;
        }
       return showMore;
    }


}
