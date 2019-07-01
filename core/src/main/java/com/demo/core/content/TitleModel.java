package com.demo.core.content;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Iterator;


@Model(adaptables = Resource.class)
public class TitleModel {

    private static final Logger LOG = LoggerFactory.getLogger(TitleModel.class);

    @Self
    private Resource resource;

    @PostConstruct
    private void title() {
        if (resource != null) {
              Resource page = resource.getParent().getParent().getParent();
              Iterator<Resource> childPage = page.listChildren();
              while (childPage.hasNext()) {
                Resource childPage1 = childPage.next();
                if(childPage1.getName().equalsIgnoreCase("jcr:content"))
                {
                    LOG.info("Parent Page title is : " + childPage1.getValueMap().get("jcr:title").toString());
                }
                else
                    {
                    Iterator<Resource> childNode = childPage1.getChildren().iterator();
                        while (childNode.hasNext()) {
                            Resource childPages = childNode.next();
                            if (childPages.getName().equals("jcr:content"))
                            {
                                LOG.info("Child Page title is : " + childPages.getValueMap().get("jcr:title"));
                            }
                        }
                }
            }
        } else
            {
            LOG.info("Resource is not found");
        }


    }

}