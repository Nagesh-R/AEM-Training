package com.demo.core.content;

import com.day.cq.search.QueryBuilder;
import com.demo.core.services.FormLimitDataDisplay;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
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

    @OSGiService
    private FormLimitDataDisplay formLimitDataDisplay;

    @OSGiService
    private QueryBuilder queryBuilder;

    private ArrayList userDataArray=new ArrayList();
    public ArrayList getUserDataArray() {
        return userDataArray;
    }

    private String[] userGender;
    public String[] getUserGender() {
        return userGender;
    }


    @PostConstruct
    private void displayData() {
        int countNode=0;
        Iterator<Resource> parentNode = resource.listChildren();
        while (parentNode.hasNext() && countNode < formLimitDataDisplay.DisplayLimitCount()) {
            Resource childNode = parentNode.next();
            ValueMap chilNodeMap= childNode.getValueMap();
            Map data= new HashMap<>();
            data.put("firstName",chilNodeMap.get("FirstName",String.class));
            data.put("LastName",chilNodeMap.get("LastName",String.class));
            data.put("Age",chilNodeMap.get("Age",String.class));
            userDataArray.add(data);
            countNode ++;
        }
        LOG.info("data"+userDataArray);
        userGender =formLimitDataDisplay.Gender();
    }
}
