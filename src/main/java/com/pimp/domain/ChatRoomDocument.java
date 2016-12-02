package com.pimp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Document
public class ChatRoomDocument {

  @Id
  private String roomName;
  private String roomType;
  private List<Message> messages;
  private List<User> participants;
  private HashMap<String, String> displayNames;

  public static ChatRoomDocument from(ChatRoom chatRoom) {
    return new ChatRoomDocument()
            .setRoomName(chatRoom.getRoomName())
            .setRoomType(chatRoom.getRoomType())
            .setDisplayNames(chatRoom.getDisplayNames())
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

  public ChatRoomDocument setMessages(List<Message> messages) {
    this.messages = messages;
    return this;
  }

  public ChatRoomDocument setParticipants(List<User> participants) {
    this.participants = participants;
    return this;
  }

  public String getRoomType() {
    return roomType;
  }

  public ChatRoomDocument setRoomType(String roomType) {
    this.roomType = roomType;
    return this;
  }

  public HashMap<String, String> getDisplayNames() {
    return displayNames;
  }

  public ChatRoomDocument setDisplayNames(HashMap<String, String> displayNames) {
    this.displayNames = displayNames;
    return this;
  }
}
