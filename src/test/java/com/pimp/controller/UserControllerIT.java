package com.pimp.controller;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "db.port=37017")
public class UserControllerIT {

  @LocalServerPort
  private int port;
  private RestTemplate restTemplate;

  @Before
  public void setUp() throws Exception {
    restTemplate = new RestTemplate();
  }

  @Test
  public void testCreateIsNotSecured() throws Exception {
    JSONObject json = new JSONObject();
    json
      .put("userName", "foo")
      .put("email", "foo@pim-plus.org")
      .put("firstName", "Foo")
      .put("lastName", "Bar")
      .put("password", "foobarbaz");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type","application/json");

    HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
      "http://localhost:" + port + "/users",
      request,
      String.class
    );
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
