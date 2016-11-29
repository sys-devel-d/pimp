package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoom {

    @NotEmpty
    @JsonProperty
    private String roomName;
    @JsonProperty
    private String roomType;
    private List<Message> messages;
    private List<User> participants;

    // This is just for checking. Is not saved in DB.
    public static Set<String> ROOM_TYPES = new HashSet<String>(){{
        add("PRIVATE");
        add("GROUP");
    }};

    public static ChatRoom from(ChatRoomDocument chatRoomDocument) {
        return new ChatRoom()
                .setRoomName(chatRoomDocument.getRoomName())
                .setParticipants(chatRoomDocument.getParticipants())
                .setRoomType(chatRoomDocument.getRoomType())
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
        this.messages.add(message);
        return this;
    }

    public ChatRoom addParticipant(User participant) {
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
}