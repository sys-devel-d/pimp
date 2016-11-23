package com.pimp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {

    @NotEmpty
    @JsonProperty
    private String userName;
    @NotEmpty
    @JsonProperty
    private String email;
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
    private List<String> rooms;

    public static User from(UserDocument userDocument) {
        return new User()
                .setEmail(userDocument.getEmail())
                .setUserName(userDocument.getUserName())
                .setFirstName(userDocument.getFirstName())
                .setLastName(userDocument.getLastName())
                .setRoles(userDocument.getRoles())
                .setPassword(userDocument.getPassword())
                .setRooms(userDocument.getRooms());
    }

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public User setRooms(List<String> rooms) {
        this.rooms = rooms;
        return this;
    }

    public User addRoom(String roomName) {
        this.rooms.add(roomName);
        return this;
    }
}