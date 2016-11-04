package com.pimp.domain;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pimp.commons.mongo.IKeyedObject;
import com.pimp.model.chat.Message;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoom implements IKeyedObject {

  @Id
  private String key;
  @NotEmpty
  @JsonProperty
  private String roomName;
  @NotEmpty
  @JsonProperty
  private String creator;
  @JsonProperty
  private Date createdAt;
  @JsonProperty
  private Date lastModifiedAt;
  private List<User> participants;
  private List<Message> messages;

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getLastModifiedAt() {
    return lastModifiedAt;
  }

  public void setLastModifiedAt(Date lastModifiedAt) {
    this.lastModifiedAt = lastModifiedAt;
  }

  public List<User> getParticipants() {
    return participants;
  }

  public void setParticipants(List<User> participants) {
    this.participants = participants;
  }

  public void addParticipant(User participant) {
    this.participants.add(participant);
  }

  public void addParticipants(List<User> participants) {
    this.participants.addAll(participants);
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public void addMessage(Message message) {
    this.messages.add(message);
  }

  public void addMessages(List<Message> message) {
    this.messages.addAll(message);
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
  }
}
