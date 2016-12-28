package com.pimp.domain;

import java.util.List;

public class EventInvitation implements NotificationMessage {

    private String eventKey;
    private List<String> invitedUsers;
    private String invitingUser;
    private String calendarKey;

    public String getEventKey() {
        return eventKey;
    }

    public EventInvitation setEventKey(String eventKey) {
        this.eventKey = eventKey;
        return this;
    }

    public List<String> getInvitedUsers() {
        return invitedUsers;
    }

    public EventInvitation setInvitedUsers(List<String> invitedUsers) {
        this.invitedUsers = invitedUsers;
        return this;
    }

    public String getInvitingUser() {
        return invitingUser;
    }

    public EventInvitation setInvitingUser(String invitingUser) {
        this.invitingUser = invitingUser;
        return this;
    }

    public String getCalendarKey() {
        return calendarKey;
    }

    public EventInvitation setCalendarKey(String calendarKey) {
        this.calendarKey = calendarKey;
        return this;
    }
}
