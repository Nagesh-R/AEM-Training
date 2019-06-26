package com.demo.core.content;

import org.apache.sling.api.resource.Resource;
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
public class FetchingDataFromForm {

    private static final Logger LOG = LoggerFactory.getLogger(FetchingDataFromForm.class);
    @Self
    private Resource resource;

    ArrayList arrayList = new ArrayList();
    public ArrayList getArrayList() {
        return arrayList;
    }

    @PostConstruct
    private void fetchTitles() {
        if (resource != null) {
            Iterator<Resource> formParentNode = resource.listChildren();
            Map<String, Object> map = new HashMap<String, Object>();

            while (formParentNode.hasNext()) {
                Resource childnode = formParentNode.next();
                map.put("firstname", childnode.getValueMap().get("firstname"));
                map.put("dob", childnode.getValueMap().get("dob"));
                map.put("age", childnode.getValueMap().get("age"));
                map.put("address", childnode.getValueMap().get("address"));
                map.put("path", childnode.getPath());
                arrayList.add(map);
                LOG.info("arraylist : " + arrayList);

            }

        }
    }
}
