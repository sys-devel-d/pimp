package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoom {

    @NotEmpty
    private String roomName;
    private String roomType;
    private List<Message> messages;
    private List<User> participants;
    private HashMap<String, String> displayNames;

    public static String ROOM_TYPE_PRIVATE = "PRIVATE";
    public static String ROOM_TYPE_GROUP = "GROUP";
    public static String HASH_KEY_GROUP_DISPLAY_NAME = "_GROUP_DISPLAY_NAME_";

    public static ChatRoom from(ChatRoomDocument chatRoomDocument) {
        return new ChatRoom()
                .setRoomName(chatRoomDocument.getRoomName())
                .setParticipants(chatRoomDocument.getParticipants())
                .setRoomType(chatRoomDocument.getRoomType())
                .setDisplayNames(chatRoomDocument.getDisplayNames())
                .setMessages(chatRoomDocument.getMessages());

    }

    public String getRoomName() {
        return roomName;
    }

    public ChatRoom setRoomName(String roomName) {
        this.roomName = roomName;
        return this;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public ChatRoom addMessage(Message message) {
        if (messages == null) {
          messages = new LinkedList<>();
        }
        this.messages.add(message);
        return this;
    }

    public ChatRoom addParticipant(User participant) {
        if (participants == null) {
          participants = new LinkedList<>();
        }
        this.participants.add(participant);
        return this;
    }

    public ChatRoom setParticipants(List<User> participants) {
        this.participants = participants;
        return this;
    }

    public List<User> getParticipants() {
        return this.participants;
    }

    public ChatRoom setMessages(List<Message> messages) {
        this.messages = messages;
        return this;
    }

    public String getRoomType() {
        return roomType;
    }

    public ChatRoom setRoomType(String roomType) {
        this.roomType = roomType;
        return this;
    }

    public HashMap<String, String> getDisplayNames() {
        return displayNames;
    }

    public ChatRoom setDisplayNames(HashMap<String, String> displayNames) {
        this.displayNames = displayNames;
        return this;
    }
}
