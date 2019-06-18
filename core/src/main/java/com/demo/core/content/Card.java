package com.demo.core.content;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Card
{

    private static final Logger LOG = LoggerFactory.getLogger(Card.class);

    @Self
    private Resource resource;

    @Inject
    private String link;
    @Inject

    private String image;
    @Inject

    private  String title;
    @Inject

    private String description;
    @Inject

    private String target;
    @Inject

    private String button;
    @Inject

    private String cta;



    public String getLink() {
        return link;
    }


    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTarget() {
        return target;
    }

    public String getButton() {
        return button;
    }

    public String getCta() {
        return cta;
    }

    @PostConstruct
    private void init() {
        if (link != null) {

            if (link.startsWith("/content")) {
                if (!link.endsWith(".html")) {
                    link = link + ".html";
                }
            }

        }

        else{
            LOG.info("button url not configured");
        }
         if(title == null){
             title = "default title";

         }
         if(description == null){
             description = "Description N/A. Please provide a valid description";
         }
         if(image == null){
             image = "Choose an Image!";
         }

    }




}




