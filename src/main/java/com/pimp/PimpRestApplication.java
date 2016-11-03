package com.pimp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import com.google.common.collect.Lists;

import com.pimp.commons.MongoFileStorage;
import com.pimp.commons.SimpleCorsFilter;

@SpringBootApplication
public class PimpRestApplication {

  public static void main(String[] args) {
    SpringApplication.run(PimpRestApplication.class, args);
  }

  @Bean
  public MongoFileStorage mongoFileStorage(GridFsTemplate gridFsTemplate) {
    return new MongoFileStorage(gridFsTemplate);
  }

  @Bean(name = "mappingConverter")
  public MongoConverter mongoConverter(MongoDbFactory mongoDbFactory) {
    MappingMongoConverter mappingMongoConverter =
        new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
    return mappingMongoConverter;
  }

  @Bean
  public FilterRegistrationBean corsFilter() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new SimpleCorsFilter());
    filterRegistrationBean.setUrlPatterns(Lists.newArrayList("/api/*"));
    return filterRegistrationBean;
  }
}
