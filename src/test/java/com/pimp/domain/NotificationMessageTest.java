package com.pimp.domain;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationMessageTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  public void testDeserialize() throws Exception {
    String testValue = "{\"sendingUser\":\"foo\",\"receivingUser\":\"bar\",\"content\":\"hi foo bar\",\"chatKey\":\"fooKey\"}";

    NewChatMessageNotification notification = mapper.readValue(testValue, NewChatMessageNotification.class);

    assertThat(notification.getChatKey()).isEqualTo("fooKey");
    assertThat(notification.getContent()).isEqualTo("hi foo bar");
    assertThat(notification.getReceivingUser()).isEqualTo("bar");
    assertThat(notification.getSendingUser()).isEqualTo("foo");
  }

  @Test
  public void testSerialize() throws Exception {
    NotificationMessage notificationMessage = new NewChatMessageNotification()
      .setChatKey("fooKey")
      .setContent("hi foo bar")
      .setReceivingUser("bar")
      .setSendingUser("foo");

    String asString = mapper.writer().writeValueAsString(notificationMessage);

    assertThat(asString)
      .isEqualTo("{\"sendingUser\":\"foo\",\"receivingUser\":\"bar\",\"content\":\"hi foo bar\",\"chatKey\":\"fooKey\"}");
  }
}
