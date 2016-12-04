package com.pimp.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pimp.commons.mongo.IKeyedObject;

/**
 * Created by julianfink on 02/12/16.
 */
public class Event implements IKeyedObject {

  @JsonProperty
  private String key = new ObjectId().toString();
  @JsonProperty
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date start;
  @JsonProperty
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private Date end;
  @JsonProperty
  @Size(min=2, max=30)
  private String title;
  @JsonProperty
  private boolean allDay = false;
  @JsonProperty
  List<String> participants = new ArrayList<>();

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

  public void setTitle(String title) {
    this.title = title;
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
}
