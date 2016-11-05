package com.pimp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class UserDocument {

    @Id
    private String userName;
    private String password;
    @Indexed
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public static UserDocument from(User user) {
        return new UserDocument()
                .setEmail(user.getEmail())
                .setUserName(user.getUserName())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRoles(user.getRoles())
                .setPassword(user.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public UserDocument setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDocument setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserDocument setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDocument setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDocument setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UserDocument setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
