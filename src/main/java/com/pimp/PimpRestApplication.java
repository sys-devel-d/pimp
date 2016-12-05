package com.pimp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

;

@SpringBootApplication
public class PimpRestApplication {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper objectmapper = new ObjectMapper().findAndRegisterModules();
    objectmapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    objectmapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    return objectmapper;
  }

  public static void main(String[] args) {
    SpringApplication.run(PimpRestApplication.class, args);
  }
}
