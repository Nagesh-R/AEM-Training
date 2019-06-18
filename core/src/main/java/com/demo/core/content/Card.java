package com.demo.core.content;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Card
{

    private static final Logger LOG = LoggerFactory.getLogger(Card.class);

    @Inject
    private String button;
    @Inject
    @Default(values ="image")
    private String image;
    @Inject
    @Default(values = "text")
    private  String text;
    @Inject
    @Default(values = "desc")
    private String desc;
    @Inject
    @Default(values = "target")
    private String target;
    @Inject
    @Default(values = "baname")
    private String bname;
    @Inject
    @Default(values = "Check")
    private String check;

    @Self
    private Resource resource;


    public String getButton() {
        return button;
    }


    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public String getDesc() {
        return desc;
    }

    public String getTarget() {
        return target;
    }

    public String getBname() {
        return bname;
    }

    public String getCheck() {
        return check;
    }

    @PostConstruct
    private void init()
    {

        if (button != null) {
            if(button.startsWith("/content"))
            {
                if(!button.endsWith(".html"))
                {
                    button = button + ".html";
                }
            }
        } else {

            LOG.info("button url not configured");
        }

    }

}
