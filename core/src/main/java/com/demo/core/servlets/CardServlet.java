package com.demo.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "aem-demo/components/content/card",
                "sling.servlet.selectors=" + "card",
                "sling.servlet.extensions=" + "json"
        })
public class CardServlet extends SlingSafeMethodsServlet {
    private Logger LOG = LoggerFactory.getLogger(FormServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("text/plain");
            resp.getWriter().println("CardModel Component Sling servlet is running.....");
            resp.getWriter().close();
        } catch (Exception e) {
            LOG.error("Exception caught in CardModel Servlet", e);
        }
    }
}