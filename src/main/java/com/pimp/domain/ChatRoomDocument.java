package com.pimp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class ChatRoomDocument {

  @Id
  private String roomName;
  private List<Message> messages;
  private List<User> participants;

  public static ChatRoomDocument from(ChatRoom chatRoom) {
    return new ChatRoomDocument()
            .setRoomName(chatRoom.getRoomName())
            .setParticipants(chatRoom.getParticipants())
            .setMessages(chatRoom.getMessages());
  }

  public String getRoomName() {
    return roomName;
  }

  public ChatRoomDocument setRoomName(String roomName) {
    this.roomName = roomName;
    return this;
  }

  public List<User> getParticipants() {
    return participants;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public ChatRoomDocument addMessage(Message message) {
    this.messages.add(message);
    return this;
  }

  public ChatRoomDocument setMessages(List<Message> messages) {
    this.messages = messages;
    return this;
  }

  public ChatRoomDocument setParticipants(List<User> participants) {
    this.participants = participants;
    return this;
  }
}
