package com.pimp.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pimp.commons.mongo.IKeyedObject;

/**
 * Created by julianfink on 02/12/16.
 */
public class Calendar implements IKeyedObject {

  @Id
  private String key;
  @NotNull
  @Size(min=2, max=30)
  private String title;
  private String owner;
  @JsonProperty
  private boolean isPrivate = false;
  private List<String> subscribers = new ArrayList<>();
  private List<Event> events = new ArrayList<>();
  @Size(min=3, max=9) // 000 or #ff00ffaa
  private String hexColor;

  public Calendar() {
  }

  public Calendar(String key) {
    this.key = key;
  }

  public String getTitle() {
    return title;
  }

  public Calendar setTitle(String title) {
    this.title = title;
    return this;
  }

  public List<String> getSubscribers() {
    return subscribers;
  }

  public void setSubscribers(List<String> subscribers) {
    this.subscribers = subscribers;
  }

  public List<Event> getEvents() {
    return events;
  }

  public Calendar setEvents(List<Event> events) {
    this.events = events;
    return this;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public Calendar setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
    return this;
  }

  public Event getEventByKey(String eventKey) {
    Optional<Event> firstEvent = getEvents()
      .stream()
      .filter(event -> event.getKey().equals(eventKey))
      .findFirst();
    return firstEvent.isPresent() ? firstEvent.get() : null;
  }

  public boolean containsEvent(String eventKey) {
    return getEventByKey(eventKey) != null;
  }

  public String getOwner() {
    return owner;
  }

  public Calendar setOwner(String owner) {
    this.owner = owner;
    return this;
  }
}
