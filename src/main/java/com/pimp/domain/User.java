package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @NotEmpty
    @JsonProperty
    private String email;
    @NotEmpty
    @JsonProperty
    private String userName;
    @Size(max = 255)
    @JsonProperty
    private String firstName;
    @Size(max = 255)
    @JsonProperty()
    private String lastName;
    @Size(min = 8, max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private List<String> roles;

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }


    public User addRoles(List<String> roles) {
        this.roles.addAll(roles);
        return this;
    }

    public User addRole(String role) {
        this.roles.add(role);
        return this;
    }

    public User setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}