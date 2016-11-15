package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pimp.commons.mongo.IKeyedObject;

import java.io.Serializable;

public class Message implements Serializable, IKeyedObject{

    @JsonProperty
    private String message;
    @JsonProperty
    private String key;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
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

    }
}
