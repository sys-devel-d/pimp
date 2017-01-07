package com.pimp.domain;

public class Notification extends Message {

    private NotificationType type;
    private boolean acknowledged;
    private String eventKey;
    private String calendarKey;
    private String sendingUser;
    private String receivingUser;

    public NotificationType getType() {
        return type;
    }

    public Notification setType(NotificationType type) {
        this.type = type;
        return this;
    }

    public String getSendingUser() {
        return sendingUser;
    }

    public void setSendingUser(String sendingUser) {
        this.sendingUser = sendingUser;
    }

    public String getReceivingUser() {
        return receivingUser;
    }

    public void setReceivingUser(String receivingUser) {
        this.receivingUser = receivingUser;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public Notification setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
        return this;
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
}
