package com.mau.msgboard_v4_thymeleaf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/messages");
        //By default, Spring Security will redirect unauthenticated users to /login,
        // and this configuration ensures a view exists for that path.
        registry.addViewController("/login");
    }


}
