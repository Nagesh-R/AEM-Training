package com.demo.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Path Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/aem-demo/sling/servlet/path",
                "sling.servlet.extensions=" + "txt"
        })

public class PathServlet extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = 1L;
    private Logger LOG = LoggerFactory.getLogger(FormServlet.class);
    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("text/plain");
            resp.getWriter().print("Aem Sling path servlet Running......");
        } catch (Exception e) {
            LOG.error("Exception caught in Path Servlet", e);
        }
    }
}