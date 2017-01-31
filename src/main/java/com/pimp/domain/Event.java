package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pimp.commons.mongo.IKeyedObject;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by julianfink on 02/12/16.
 */
public class Event implements IKeyedObject {

  private String key = new ObjectId().toString();
  private String calendarKey;
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date start;
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date end;
  @Size(min=2, max=30)
  private String title;
  @Size(max=256)
  private String place;
  @Size(max=4096)
  private String description;
  private String creator;
  @JsonProperty
  private boolean allDay = false;
  @JsonProperty
  private boolean isPrivate = false;
  private List<String> participants = new ArrayList<>();
  private List<String> invited = new ArrayList<>();
  private List<String> declined = new ArrayList<>();

  public Event() {
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public String getTitle() {
    return title;
  }

  public Event setTitle(String title) {
    this.title = title;
    return this;
  }

  public boolean isAllDay() {
    return allDay;
  }

  public void setAllDay(boolean allDay) {
    this.allDay = allDay;
  }

  public List<String> getParticipants() {
    return participants;
  }

  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  public String getCalendarKey() {
    return calendarKey;
  }

  public void setCalendarKey(String calendarKey) {
    this.calendarKey = calendarKey;
  }

  public String getPlace() {
    return place;
  }

  public Event setPlace(String place) {
    this.place = place;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Event setDescription(String description) {
    this.description = description;
    return this;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public Event setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
    return this;
  }

  public String getCreator() {
    return creator;
  }

  public Event setCreator(String creator) {
    this.creator = creator;
    return this;
  }

  public List<String> getInvited() {
    return invited;
  }

  public List<String> getDeclined() {
    return declined;
  }
}
