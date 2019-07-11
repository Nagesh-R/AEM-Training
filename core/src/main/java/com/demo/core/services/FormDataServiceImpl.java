package com.demo.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = FormDataServiceImpl.Config.class)
@Component(service = FormDataService.class, immediate = true)
public class FormDataServiceImpl implements FormDataService {

    private FormDataServiceImpl.Config formDataConfig;

    @Activate
    protected void activate(final FormDataServiceImpl.Config config) {
        this.formDataConfig = config;
    }

    @ObjectClassDefinition(name = "Display of Form Data Service",
            description = "Form Data Display Configuration")

    public @interface Config {
        @AttributeDefinition(
                name = "Restrict Node Creation",
                description = "Configure Display Count of Form Data",
                type = AttributeType.INTEGER
        )
        int getMaxNodeCount() default 5;


        @AttributeDefinition(
                name = "No.of Nodes to be Displayed",
                description = "Configure of No.of Nodes to be Displayed",
                type = AttributeType.INTEGER
        )
        int getNumberOfNodes() default 3;

        @AttributeDefinition(
                name = "Display of Genders",
                description = "Configuration for display of gender",
                type = AttributeType.STRING
        )
        String[] getDisplayGender();
    }

    @Override
    public int getMaxNodeCount() {
        return formDataConfig.getMaxNodeCount();
    }

    @Override
    public int getNumberOfNodesDisplayed() {
        return formDataConfig.getNumberOfNodes();
    }

    @Override
    public String[] getDisplayOfGender() {
        return formDataConfig.getDisplayGender();
    }
}
