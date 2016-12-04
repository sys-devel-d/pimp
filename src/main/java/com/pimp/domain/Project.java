package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Project {

    @JsonProperty
    private String name;
    @JsonProperty
    private List<String> userNames = new LinkedList<>();

    public static Project from(ProjectDocument document) {
        return new Project()
                .setName(document.getName())
                .setUserNames(document.getUserNames());
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public Project setUserNames(List<String> userNames) {
        this.userNames = userNames;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (name != null ? !name.equals(project.name) : project.name != null) return false;
        return userNames != null ? userNames.equals(project.userNames) : project.userNames == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (userNames != null ? userNames.hashCode() : 0);
        return result;
    }
}
