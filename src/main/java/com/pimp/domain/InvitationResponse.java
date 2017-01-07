package com.pimp.domain;

/**
 * Created by julianfink on 05/01/17.
 */
public class InvitationResponse {

  public static String ACCEPTED = "ACCEPTED";
  public static String DECLINED = "DECLINED";

  private String userName;
  private String state;
  private String answer;
  private String eventKey;
  private String calendarKey;
  private String invitee;

  public InvitationResponse() {
  }

  public String getInvitee() {
    return invitee;
  }

  public void setInvitee(String invitee) {
    this.invitee = invitee;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getEventKey() {
    return eventKey;
  }

  public void setEventKey(String eventKey) {
    this.eventKey = eventKey;
  }

  public String getCalendarKey() {
    return calendarKey;
  }

  public void setCalendarKey(String calendarKey) {
    this.calendarKey = calendarKey;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
