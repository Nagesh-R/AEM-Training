package com.demo.core.content;

import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;


@Model(adaptables = Resource.class)
public class PageModel {
    private static final Logger LOG = LoggerFactory.getLogger(PageModel.class);

    @Self
    private Resource resource;

    List<Map<String,String>> list = new ArrayList<>();

    public List<Map<String, String>> getList() {
        return list;
    }

    @PostConstruct
    public void getTaggedPages(){

        TagManager tagManager  = resource.getResourceResolver().adaptTo(TagManager.class);
        RangeIterator<Resource> iterator = tagManager.find("AemDemo:articlepage");
        while(iterator.hasNext()){
            Resource resource = iterator.next();
           // LOG.info("....."+resource);
         // Iterator<Resource> tag  = resource.listChildren();
            LOG.info("..."+resource.getValueMap().get("jcr:title"));
            LOG.info("..."+resource.getValueMap().get("pageTitle"));



        }






    }
}
