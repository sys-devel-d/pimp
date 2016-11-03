package com.pimp.config;

import com.google.common.collect.Lists;
import com.pimp.commons.filters.SimpleCorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class HttpConfig{

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        return new FilterRegistrationBean();
    }

    @Bean
    public FilterRegistrationBean corsFilter(FilterRegistrationBean filterRegistrationBean) {
        filterRegistrationBean.setFilter(new SimpleCorsFilter());
        filterRegistrationBean.setUrlPatterns(Lists.newArrayList("/api/*"));
        return filterRegistrationBean;
    }
}
