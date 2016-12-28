package com.pimp.domain;

public class MessageNotification implements NotificationMessage {
    private String sendingUser;
    private String receivingUser;
    private String content;
    private String chatKey;

    public String getSendingUser() {
        return sendingUser;
    }

    public MessageNotification setSendingUser(String sendingUser) {
        this.sendingUser = sendingUser;
        return this;
    }

    public String getReceivingUser() {
        return receivingUser;
    }

    public MessageNotification setReceivingUser(String receivingUser) {
        this.receivingUser = receivingUser;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MessageNotification setContent(String content) {
        this.content = content;
        return this;
    }

    public String getChatKey() {
        return chatKey;
    }

    public MessageNotification setChatKey(String chatKey) {
        this.chatKey = chatKey;
        return this;
    }
}
