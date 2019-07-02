package com.demo.core.services;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = FormDataDisplayServiceImpl.Config.class)
@Component(service = FormDataDisplayService.class, immediate = true)

public class FormDataDisplayServiceImpl implements FormDataDisplayService {
    private FormDataDisplayServiceImpl.Config formDataDisplayConfig;

    @Activate
    protected void activate(final FormDataDisplayServiceImpl.Config config) {
        this.formDataDisplayConfig = config;
    }

    @ObjectClassDefinition(name = "Form Data Service",
            description = "Form Data Display Configuration")

    public @interface Config {

        @AttributeDefinition(
                name = "Display Count",
                description = "Configure Display Count of Form Data",
                type = AttributeType.INTEGER
        )
        int getFormDataDisplayCount() default 5;

    }

    @Override
    public int getDisplayCount(){
        return formDataDisplayConfig.getFormDataDisplayCount();
    }

}


