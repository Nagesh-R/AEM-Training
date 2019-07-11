package com.demo.core.content;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables=SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkModel {
    private static final Logger LOG = LoggerFactory.getLogger(LinkModel.class);

    @Self
    private SlingHttpServletRequest slingHttpServletRequest;

    public String getUrl() {
        return link;
    }

    @Inject
    private String link;

    @PostConstruct
    private void linkModel() {

        if (link != null && link.startsWith("/content") && (!link.contains(".html"))) {
                    link = link +".html";
        } else {
            LOG.warn("Link is not found");
        }


    }
}
