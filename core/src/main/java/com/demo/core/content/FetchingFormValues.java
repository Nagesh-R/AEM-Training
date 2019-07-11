package com.demo.core.content;

import com.demo.core.service.FormDataLimitService;
import org.apache.sling.api.resource.Resource;
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

public class FetchingFormValues {

    private static final Logger LOG = LoggerFactory.getLogger(FetchingFormValues.class);
    @Self
    private Resource resource;
    @OSGiService
    FormDataLimitService fromDataLimitService;
    ArrayList arrayList = new ArrayList();
    public ArrayList getArrayList() {
        return arrayList;
    }
    private String[] gender;
    public String[] getGender()
    {
        return gender;
    }

    @PostConstruct
    private void fetchData() {
        if (resource != null) {
            int limitNodeCount =0;
            Iterator<Resource> resourceIterator = resource.listChildren();
            while (resourceIterator.hasNext() && limitNodeCount<fromDataLimitService.getDataWithLimit()) {
                Resource childnode = resourceIterator.next();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("first_name",childnode.getValueMap().get("first_name"));
                map.put("last_name",childnode.getValueMap().get("last_name"));
                map.put("age",childnode.getValueMap().get("age"));
                map.put("gender",childnode.getValueMap().get("gender"));
                map.put("path",childnode.getPath());
                arrayList.add(map);
                limitNodeCount++;
                LOG.info("arraylist : " + arrayList);


            }


        }
       gender= fromDataLimitService.getGender();
    }



}
