package com.demo.core.content;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class)

public class LinkModel {
    private static final Logger LOG = LoggerFactory.getLogger(LinkModel.class);

    @Self
    private SlingHttpServletRequest request;
    @Inject
    private String link;

    public String getLink()
    {
        return  link;
    }

    @PostConstruct
    private void fetchDataUsingLink() {

     if (link!=null && link.startsWith("/content") && !link.endsWith(".html"))
     {
       link =link + ".html";
     }
     else
     {

     }

    }
}
