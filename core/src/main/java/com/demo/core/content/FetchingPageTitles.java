package com.demo.core.content;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Iterator;



@Model(adaptables = Resource.class)
public class FetchingPageTitles {

    private static final Logger LOG = LoggerFactory.getLogger(FetchingPageTitles.class);

    @Self
    private Resource resource;
    Resource parentpage;

    @PostConstruct
    private void fetchTitles() {
        if (resource != null) {
            Iterator<Resource>  parentpage = resource.getParent().getParent().getParent().listChildren();
            while (parentpage.hasNext()) {
                Resource childpage = parentpage.next();
                if(childpage.getName().equalsIgnoreCase("jcr:content")){
                    LOG.info("parent page title is : " + childpage.getValueMap().get("jcr:title").toString());
                }else {
                    Iterator<Resource> childNodeIterator = childpage.getChildren().iterator();
                    childNodeIterator.forEachRemaining(childnodes -> LOG.info("title of childpage is: " + childnodes.getValueMap().get("jcr:title")));
                }

           }
        } else {
            LOG.info("Resource not found");
        }


      /*  PageManager pageManager  = resource.getResourceResolver().adaptTo(PageManager.class);
          Page page = pageManager.getContainingPage(resource);
          Iterator<Page> pagenodeIterator = page.listChildren();
          pagenodeIterator.forEachRemaining(childPageTitle->LOG.info("child page title is: "+childPageTitle.getTitle()+"   "+"page title is:  "+childPageTitle.getPageTitle()));
*/

        }
    }

