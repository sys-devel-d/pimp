package com.pimp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

;

@SpringBootApplication
public class PimpRestApplication {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    return new ObjectMapper().findAndRegisterModules();
  }

  public static void main(String[] args) {
    SpringApplication.run(PimpRestApplication.class, args);
  }
}
