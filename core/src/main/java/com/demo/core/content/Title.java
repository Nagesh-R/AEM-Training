package com.demo.core.content;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Iterator;


@Model(adaptables = Resource.class)
public class Sample {

/*

    private static final Logger LOG = LoggerFactory.getLogger(Sample.class);

    @Self
    private Resource resource;

    @PostConstruct
    private void fetchTitle()
    {*/
/*
        ResourceResolver resourceResolver = resource.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);*//*


        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        Page parentPage = pageManager.getContainingPage(resource); // instaed of going to node by node

        LOG.info("Current Page Title:-" + parentPage.getTitle());

        Iterator<Page> child= parentPage.listChildren();
        while(child.hasNext())
        {
            Page childtitle= child.next();
            LOG.info("ChildPage title is:  " + childtitle.getTitle());
        }
    }
}
*/

    private static final Logger LOG = LoggerFactory.getLogger(Sample.class);

    @Self
    private Resource resource;

    @PostConstruct
    private void title() {
/*

        Resource parentPageTitle = resource.getParent().getParent();
//        if (parentPageTitle != null) {
            LOG.info("parent page title is : " + parentPageTitle.getValueMap().get("jcr:title").toString());

        */
/*else {
            LOG.info("parent is null");
        }*//*


        resource1 = resource.getParent().getParent().getParent();
        if (resource1 != null)
        {
//            List<Resource> pageList = new ArrayList<>();
            Iterator<Resource> childPage = resource1.listChildren();
            while (childPage.hasNext()) {
                Resource childpage = childPage.next();
//                String childpagename = childpage.getName();
               */
/* pageList.add(childpage);
                LOG.info("..." + pageList);*//*

                if (!childpage.equals("jcr:content")) {
//                    for (Resource rc : pageList) {
                        Iterator<Resource> childNodeIterator = childpage.getChildren().iterator();
                        while (childNodeIterator.hasNext()) {
                            Resource childNode = childNodeIterator.next();
                            if (childNode.getName().equals("jcr:content"))
                            {
//                                LOG.info("path is : " + childNode.getPath());
                                LOG.info("title is : " + childNode.getValueMap().get("jcr:title"));
                            }
                        }


                    }
                }
            }
        else {
            LOG.info("No child pages are found");
        }
*/


        if (resource != null) {
              Resource page = resource.getParent().getParent().getParent();
              Iterator<Resource> childPage = page.listChildren();

//            LOG.info("list childresn: "+resource.getParent().getParent().getParent().listCildren().toString());
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