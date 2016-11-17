package com.pimp.model.chat.api;

import java.time.Instant;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.pimp.domain.ChatRoom;
import com.pimp.model.chat.Message;
import com.pimp.services.IChatRoomService;

/**
 * The ChatController handles i/o socket communication,
 * which is published to a corresponding route, e.g. /broker.
 * It also defines the called method, once a message could be received.
 * The return value of the method is broadcasted to
 * all subscribers of /rooms/messages
 */
@RestController
public class ChatController {

  private IChatRoomService chatRoomService;
  private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

  public ChatController(IChatRoomService chatRoomService) {
    this.chatRoomService = chatRoomService;
  }

  @MessageMapping("/broker/{room}")
  @SendTo("/rooms/message/{room}")
  public Message message(Message message) throws Exception {
    Instant creationDate = Instant.now();
    Optional<ChatRoom> first = chatRoomService.getEntitiesByKeys(chatRoomService.getKeys())
      .stream()
      .filter(element -> element.getRoomName().equals(message.getRoomId()))
      .findFirst();
    if (!first.isPresent()) {
      LOGGER.info("There is no chatroom with id " + message.getRoomId());
      // TODO should be removed, once we have a mechanism
      // for creating rooms with privileges
      ChatRoom chatRoom = chatRoomService.create();
      chatRoom.setRoomName(message.getRoomId());
      chatRoom.getMessages().add(message);
      chatRoomService.insert(chatRoom);
    } else {
      ChatRoom chatRoom = first.get();
      Message newMessage = new Message();
      newMessage.setKey(new ObjectId().toString());
      newMessage.setRoomId(message.getRoomId());
      newMessage.setUserName(message.getUserName());
      newMessage.setCreationDate(creationDate);
      chatRoom.addMessage(newMessage);
      chatRoomService.update(chatRoom, false);
    }
    return new Message(message.getMessage(),
        message.getRoomId(), message.getUserName(), creationDate);
  }

}
