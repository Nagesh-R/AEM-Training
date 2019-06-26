package com.demo.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import com.demo.core.content.Card;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service= Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Servlet based on Resource Path",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes="+"aem-demo/components/content/card",
                "sling.servlet.selectors="+"demoservlet",
                "sling.servlet.extensions=" + "json"
        })
public class ResourceBasePathServlet extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceBasePathServlet.class);
    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Enter inside servlet");
        final Resource resource = req.getResource();
        resp.setContentType("text/plain");
        resp.getWriter().print("Servlet called...");
        resp.getWriter().print("Servlet for resource type");
       // resp.getWriter().write("Title = " + resource.getValueMap().get(JcrConstants.JCR_TITLE));
            resp.getWriter().close();
    }
}
