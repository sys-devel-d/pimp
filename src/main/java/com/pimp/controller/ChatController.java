package com.pimp.controller;

import com.pimp.domain.ChatRoom;
import com.pimp.domain.Message;
import com.pimp.domain.User;
import com.pimp.services.ChatRoomService;
import com.pimp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

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
  private UserService userService;

  @Autowired
  public ChatController(ChatRoomService chatRoomService, UserService userService) {
    this.chatRoomService = chatRoomService;
    this.userService = userService;
  }

  /**
   * This is only invoked when the client joins the room (subscribes).
   * On the client this is exposed as `/app/initial-messages/{room}`
   */
  @SubscribeMapping("/initial-messages/{room}/{user}")
  public List<Message> sendInitialMessages(
          @DestinationVariable("room") String roomName,
          @DestinationVariable("user") String userName) {
    User user = userService.findByUserName(userName);
    ChatRoom chatRoom = chatRoomService.createIfNotExists(roomName);
    if(!user.getRooms().contains(roomName)) {
      user.addRoom(roomName);
      userService.save(user);
      chatRoom.addParticipant(user);
      chatRoomService.save(chatRoom);
    }
    return chatRoom.getMessages();
  }

  @MessageMapping("/broker/{room}")
  @SendTo("/rooms/message/{room}")
  public Message message(Message message) throws Exception {
    Instant creationDate = Instant.now();
    message.setCreationDate(creationDate);
    message.setKey(new ObjectId().toString());
    /* Here we can be sure that the room exists as it must have been
     * created in this.sendInitialMessage() if it hadn't previously existed
     */
    ChatRoom chatRoom = chatRoomService.findByRoomName(message.getRoomId());
    chatRoom.addMessage(message);
    chatRoomService.save(chatRoom);
    return message;
  }
}
