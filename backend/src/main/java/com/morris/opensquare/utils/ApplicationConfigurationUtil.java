package com.morris.opensquare.utils;

import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component("applicationConfigurationUtil")
public class ApplicationConfigurationUtil {

    public ApplicationPropertiesConfiguration getApplicationPropertiesConfiguration() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationPropertiesConfiguration.class);
        return context.getBean(ApplicationPropertiesConfiguration.class);
    }
}
