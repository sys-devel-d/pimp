package com.pimp.domain;

public class NotificationChannel extends ChatRoom {

    public NotificationChannel() {
      super();
    }

    private String owner;

    public static NotificationChannel from(NotificationChannelDocument document) {
        return (NotificationChannel) new NotificationChannel()
                .setRoomName(document.getRoomName())
                .setParticipants(document.getParticipants())
                .setRoomType(document.getRoomType())
                .setDisplayNames(document.getDisplayNames())
                .setMessages(document.getMessages());
    }
}
