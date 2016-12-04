package com.pimp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class ProjectDocument {

    @Id
    private String name;
    private List<String> userNames;

    public static ProjectDocument from(Project project) {
        return new ProjectDocument()
                .setName(project.getName())
                .setUserNames(project.getUserNames());
    }

    public String getName() {
        return name;
    }

    public ProjectDocument setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public ProjectDocument setUserNames(List<String> userNames) {
        this.userNames = userNames;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectDocument that = (ProjectDocument) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return userNames != null ? userNames.equals(that.userNames) : that.userNames == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (userNames != null ? userNames.hashCode() : 0);
        return result;
    }
}
