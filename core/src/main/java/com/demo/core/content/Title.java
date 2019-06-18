package com.demo.core.content;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Iterator;

@Model(adaptables = Resource.class)
public class Title
{
    private final Logger LOG = LoggerFactory.getLogger(Title.class);

    @Self
    private Resource resource1, resource2;

    @PostConstruct
    private void init() {


      /*  resource1 = resource1.getParent().getParent();
//        LOG.info("The resource is {}", resource1.toString());

        LOG.info("Title is : {}", resource1.getValueMap().get("jcr:title"));

        resource2 = resource2.getParent().getParent();*/

        if (resource1 != null) {
            Iterator<Resource> main = resource1.getParent().getParent().getParent().listChildren();
            while (main.hasNext()) {
                Resource parent = main.next();
                if(parent.getName().equals("jcr:content")) {
                    LOG.info("Main Page Title : "+parent.getValueMap().get("jcr:title"));
                }else{
                    Iterator<Resource> child = parent.listChildren();
                    while (child.hasNext()) {
                        Resource childNode = child.next();
                        if(childNode.getName().equals("jcr:content")) {
                            LOG.info("Child Page Title : " + childNode.getValueMap().get("jcr:title"));
                        }
                    }
                }
            }
        }
        else {
            LOG.info("No child pages found");
        }
    }


/*
        ResourceResolver resourceResolver = resource.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page parent = pageManager.getContainingPage(resource);
        LOG.info("Title : " + parent.getTitle());

        Iterator <Page> child = parent.listChildren();
        while(child.hasNext()){
            Page childpage = child.next();
            LOG.info("Title of child: "+childpage.getTitle());
        }*/

      /*  PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        Page parent = pageManager.getContainingPage(resource);
        LOG.info("Title : "+parent.getTitle());

        Iterator <Page> child = parent.listChildren();
        while(child.hasNext()){
            Page cpage = child.next();
            LOG.info("Title of child: "+cpage.getTitle());

        }*/

//    }
}