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

public class FormModel
{
    private static final Logger LOG = LoggerFactory.getLogger(CardModel.class);

    @Self
    private Resource resource;

    private ArrayList userDataArray=new ArrayList();

    @PostConstruct
    private void displayData()
    {
        Iterator<Resource> parentNode = resource.listChildren();
        while (parentNode.hasNext())
        {
            Resource childNode = parentNode.next();
            ValueMap chilNodeMap= childNode.getValueMap();
            Map data= new HashMap<>();
            data.put("firstName",chilNodeMap.get("FirstName",String.class));
            data.put("LastName",chilNodeMap.get("LastName",String.class));
            data.put("Age",chilNodeMap.get("Age",String.class));
            userDataArray.add(data);
        }

        LOG.info("data"+userDataArray);
    }

    public ArrayList getUserDataArray() {
        return userDataArray;
    }
}
