package com.pimp.domain;

public class Notification extends Message{

    private NotificationType type;
    private boolean acknowledged;
    private NotificationMessage notificationMessage;

    public NotificationType getType() {
        return type;
    }

    public Notification setType(NotificationType type) {
        this.type = type;
        return this;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public Notification setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
        return this;
    }

    public NotificationMessage getNotificationMessage() {
        return notificationMessage;
    }

    public Notification setNotificationMessage(NotificationMessage notificationMessage) {
        this.notificationMessage = notificationMessage;
        return this;
    }
}
