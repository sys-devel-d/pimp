package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pimp.commons.mongo.IKeyedObject;

import java.io.Serializable;
import java.time.Instant;

public class Message implements Serializable, IKeyedObject{

    @JsonProperty
    private String message;
    @JsonProperty
    private String key;
    @JsonProperty
    private String userName;
    @JsonProperty
    private String roomId;
    @JsonProperty
    private Instant creationDate = Instant.now();

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, String roomId, String userName, Instant creationDate) {
        this.message = message;
        this.roomId = roomId;
        this.userName = userName;
        this.creationDate = creationDate;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
}
