package com.pimp.model.chat.api;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.pimp.model.chat.Message;

import java.util.ArrayList;

/**
 * The ChatController handles i/o socket communication,
 * which is published to a corresponding route, e.g. /broker.
 * It also defines the called method, once a message could be received.
 * The return value of the method is broadcasted to
 * all subscribers of /rooms/messages
 */
@Controller
public class ChatController {

  /**
   * This is only invoked when the client joins the room (subscribes).
   * On the client this is exposed as `/app/initial-messages/{room}`
   */
  @SubscribeMapping("/initial-messages/{room}")
  public ArrayList<Message> sendInitialMessages(@DestinationVariable("room") String room) {
    ArrayList<Message> messages = new ArrayList<>();
    messages.add(new Message("First message", room, "test"));
    messages.add(new Message("Second message", room, "test"));
    return messages;
  }

  @MessageMapping("/broker/{room}")
  @SendTo("/rooms/message/{room}")
  public Message message(Message message) throws Exception {
    return new Message(message.getMessage(),
        message.getRoomId(), message.getUsername());
  }

}
