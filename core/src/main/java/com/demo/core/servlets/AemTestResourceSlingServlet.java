package com.demo.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;


@Component(service= Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes="+ "aem-demo/components/structure/home",
                "sling.servlet.paths="+"/bin/aemtraning/testresourceservlet",
                "sling.servlet.selectors="+ "testservlet",
                "sling.servlet.extensions=" + "html"
        })
public class AemTestResourceSlingServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/plain");
        resp.getWriter().println("AemTestResourceSlingServlet called");
        System.out.println("hello welcome to the Sling Servlet");
        resp.getWriter().print("hello welcome to the Sling Servlet");
        resp.getWriter().close();
    }

}
