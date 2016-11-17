package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pimp.commons.mongo.IKeyedObject;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoom implements IKeyedObject {

  @Id
  private String key = new ObjectId().toString();
  @NotEmpty
  @JsonProperty
  private String roomName;
  @NotEmpty
  @JsonProperty
  private String creator;
  @JsonProperty
  private Instant createdAt = Instant.now();
  @JsonProperty
  private Instant lastModifiedAt = Instant.now();
  private List<User> participants = new ArrayList<>();
  private List<Message> messages = new ArrayList<>();

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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getLastModifiedAt() {
    return lastModifiedAt;
  }

  public void setLastModifiedAt(Instant lastModifiedAt) {
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
