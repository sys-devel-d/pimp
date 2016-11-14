package com.pimp.model.chat.api;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.pimp.model.chat.Message;

/**
 * The ChatController handles i/o socket communication,
 * which is published to a corresponding route, e.g. /broker.
 * It also defines the called method, once a message could be received.
 * The return value of the method is broadcasted to
 * all subscribers of /rooms/messages
 */
@Controller
public class ChatController {

  @MessageMapping("/broker/{room}")
  @SendTo("/rooms/message/{room}")
  public Message message(Message message) throws Exception {
    return new Message(message.getMessage(),
        message.getRoomId(), message.getUsername());
  }

}
