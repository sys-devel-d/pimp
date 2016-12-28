package com.pimp.domain;

public class NotificationChannelDocument extends ChatRoomDocument {

    public static NotificationChannelDocument from(NotificationChannel channel) {
        return (NotificationChannelDocument) new NotificationChannelDocument()
                .setRoomName(channel.getRoomName())
                .setRoomType(channel.getRoomType())
                .setDisplayNames(channel.getDisplayNames())
                .setParticipants(channel.getParticipants())
                .setMessages(channel.getMessages());
    }
}
