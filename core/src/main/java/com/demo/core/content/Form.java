package com.demo.core.content;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Model(adaptables = Resource.class)

public class Form
{
    private static final Logger LOG = LoggerFactory.getLogger(Form.class);
    @Self
    private Resource resource;
    private ArrayList userData=new ArrayList();
    @PostConstruct
    private void displayData()
    {
        Iterator<Resource> parent = resource.listChildren();
        while (parent.hasNext())
        {
            Resource child = parent.next();
            ValueMap childNode= child.getValueMap();
            Map data= new HashMap<>();
            data.put("firstName",childNode.get("FirstName",String.class));
            data.put("LastName",childNode.get("LastName",String.class));
            data.put("Age",childNode.get("Age",String.class));
            userData.add(data);
        }
        LOG.info("data"+userData);
    }
    public ArrayList getUserData()
    {
        return userData;
    }
}
