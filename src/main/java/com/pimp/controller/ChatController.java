package com.pimp.controller;

import com.pimp.domain.Message;
import com.pimp.services.ChatRoomService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * The ChatController handles i/o socket communication,
 * which is published to a corresponding route, e.g. /broker.
 * It also defines the called method, once a message could be received.
 * The return value of the method is broadcasted to
 * all subscribers of /rooms/message/{room}
 */
@RestController
public class ChatController {

  private ChatRoomService chatRoomService;

  @Autowired
  public ChatController(ChatRoomService chatRoomService) {
    this.chatRoomService = chatRoomService;
  }

  @MessageMapping("/broker/{room}")
  @SendTo("/rooms/message/{room}")
  public Message message(Message message) throws Exception {
    return handleIncomingMessage(message);
  }

  private Message handleIncomingMessage(Message message) {
    message.setCreationDate(Instant.now());
    message.setKey(new ObjectId().toString());
    chatRoomService.insertMessageIntoRoom(message);
    return message;
  }

}
