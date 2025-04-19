package com.ntou;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalUrlPrefixConfigCreditcard implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/springboot-microservice-loadbalancer/transactions", clazz -> {
            String packageName = clazz.getPackageName();
            return packageName.startsWith("com.ntou");
        });
    }
}

