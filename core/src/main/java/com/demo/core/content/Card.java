package com.demo.core.content;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Card
{

    private static final Logger LOG = LoggerFactory.getLogger(Card.class);


    @Inject



    private String link;

    @Inject

    private String image;

    @Inject @Default(values="we retail")

    private  String title;

    @Inject
    @Default(values="this is we retail logo")
    private String description;

    @Inject

    private String cta;

    @Inject
    @Default(values="click")
    private String bname;

    @Inject
    @Default(values="true")
    private String EnableCTA;


    @Self
    private Resource resource;




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

    public String getCta() {
        return cta;
    }

    public String getBname() {
        return bname;
    }

    public String getenableCTA() {
        return EnableCTA;
    }




    @PostConstruct
    private void init()
    {
        if (link != null) {
            if(link.startsWith("/content"))
            {
                if(!link.endsWith(".html"))
                {
                    link = link + ".html";
                }
            }
        } else {
           // LOG.info("button url not configured");
        }

        if(image == null){
            image ="/apps/aem-demo/components/content/card/climber-gear-indoor.jpg";
        }

    }


}