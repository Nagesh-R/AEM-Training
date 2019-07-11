package com.demo.core.service;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = FormDataLimitServiceImpl.Config.class)
@Component(service = FormDataLimitService.class, immediate = true)
public class FormDataLimitServiceImpl implements FormDataLimitService {



    private FormDataLimitServiceImpl.Config formDataDisplayConfig;

    @Activate
    protected void activate(final FormDataLimitServiceImpl.Config config) {
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
        int getFormDataDisplayCount() default 10;
        int getFormDataLimit() default  5;

        @AttributeDefinition(
                name = "Gender",
                description = "Configure Gender",
                type = AttributeType.STRING
        )

       String [] getGenderData() ;

    }


    @Override
    public int getDataCount(){
        return formDataDisplayConfig.getFormDataDisplayCount();
    }

    @Override
    public int getDataWithLimit(){
        return formDataDisplayConfig.getFormDataLimit();


    }
    @Override
    public String[] getGender()
    {
        return formDataDisplayConfig.getGenderData();
    }

}
