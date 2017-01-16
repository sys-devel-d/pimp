package com.pimp.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "db.port=37017")
@RunWith(SpringRunner.class)
public class NotificationControllerIT {

  private ResourceOwnerPasswordResourceDetails details;

  @LocalServerPort
  private int port;
  private OAuth2RestTemplate restTemplate;
  private LinkedBlockingDeque blockingQueue;
  private WebSocketStompClient stompClient;

  private final String channelName = "test";
  private WebSocketHttpHeaders webSocketHttpHeaders;

  @Before
  public void setUp() throws Exception {
    String token = authenticate();
    webSocketHttpHeaders = new WebSocketHttpHeaders();
    webSocketHttpHeaders.add("Authorization", "Bearer " + token);

    blockingQueue = new LinkedBlockingDeque<>();
    RestTemplateXhrTransport restTemplateXhrTransport = new RestTemplateXhrTransport();


    SockJsClient sockJsClient = new SockJsClient(Arrays.asList(restTemplateXhrTransport));
    sockJsClient.setHttpHeaderNames("Authorization");
    stompClient = new WebSocketStompClient(sockJsClient);
  }

  private void registerChannel(String channelName) {
    restTemplate.postForEntity("http://localhost:" + port + "/notification/" + channelName, "", String.class);
  }

  private String authenticate() {
    details = new ResourceOwnerPasswordResourceDetails();
    details.setUsername("test");
    details.setPassword("12345678");
    details.setClientId("angularClient");
    details.setClientSecret("secret123");
    details.setAccessTokenUri("http://localhost:" + port + "/oauth/token");

    restTemplate = new OAuth2RestTemplate(details);

    OAuth2AccessToken accessToken = restTemplate.getAccessToken();
    return accessToken.getValue();
  }

  @Test
  public void testAuthenticate() throws Exception {
    assertThat(restTemplate.getAccessToken()).isNotNull();
  }

  @Test
  public void testConnect() throws Exception {
    try {
      StompSession stompSession = stompClient.connect("ws://localhost:" + port + "/notifications", webSocketHttpHeaders, new StompSessionHandlerAdapter() {
      }).get();
    } catch (RuntimeException e) {
      fail("Could not connect to endpoint notifications. " + e.getMessage());
    }
  }


  class DefaultStompFrameHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
      return byte[].class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
      blockingQueue.offer(new String((byte[]) o));
    }
  }
}

