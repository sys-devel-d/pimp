package com.pimp.domain;

public class Notification extends Message {

    private NotificationType type;
    private boolean acknowledged;
    // e.g. eventKey, messageKey
    private String referenceKey;
    // e.g. calendarKey, roomName
    private String referenceParentKey;
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

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public String getReferenceParentKey() {
        return referenceParentKey;
    }

    public void setReferenceParentKey(String referenceParentKey) {
        this.referenceParentKey = referenceParentKey;
    }
}
