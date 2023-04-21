package com.spike.config;


import com.spike.filter.ManageApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @PACKAGE_NAME: com.example.fxpgJava.config
 * @NAME: WebAuthFilterConfig 用户管理模块过滤器拦截配置
 * @USER: spike
 * @DATE: 2023/4/12 10:17
 * @PROJECT_NAME: FXPG_Java
 */
@Configuration
public class WebAuthFilterConfig {

    @Bean
    public FilterRegistrationBean webAuthFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(manageApiFilter());
        registration.setName("ManageApiFilter");
        //用户管理路径拦截
        registration.addUrlPatterns("/User/FindByID");
        registration.addUrlPatterns("/User/List");
        registration.addUrlPatterns("/User/Update");
        registration.addUrlPatterns("/User/Delete");
        registration.addUrlPatterns("/User/Register");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter manageApiFilter() {
        return new ManageApiFilter();
    }
}
