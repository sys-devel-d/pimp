package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pimp.commons.mongo.IKeyedObject;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public abstract class Group implements IKeyedObject{

    @JsonProperty
    @Id
    private String key = new ObjectId().toString();
    @JsonProperty
    @NotEmpty
    @Indexed(unique = true)
    private String name;
    @JsonProperty
    private List<String> userNames = new LinkedList<>();

    public String getName() {
        return name;
    }

    public <T extends Group> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public <T extends Group> T setUserNames(List<String> userNames) {
        this.userNames = userNames;
        return (T) this;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (name != null ? !name.equals(group.name) : group.name != null) return false;
        return userNames != null ? userNames.equals(group.userNames) : group.userNames == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (userNames != null ? userNames.hashCode() : 0);
        return result;
    }

    public <T extends Group> T add(String userName) {
        userNames.add(userName);

        return (T) this;
    }

    public <T extends Group> T remove(String userName) {
        userNames.remove(userName);

        return (T) this;
    }
}
