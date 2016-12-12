package com.pimp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pimp.domain.User;
import com.pimp.services.UserService;

import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = GET, path = "/{userName}")
  public User getUser(@PathVariable String userName) throws NotFoundException {
    return userService.findByUserName(userName);
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = POST)
  public void createUser(@Valid @RequestBody User user) {
    userService.createUser(user);
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = GET)
  public List<User> getAllUsers() {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return withoutCurrentUser(userService.findAll(), currentUser);
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = GET, path = "/search/{query}")
  public List<User> searchUser(@PathVariable String query) {
    if (query.length() < 3) {
      throw new IllegalArgumentException("Search string should have a length >= 3.");
    }
    User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<User> foundUsers = userService.query(query, Arrays.asList("firstName", "lastName", "_id"));

    return withoutCurrentUser(foundUsers, loggedInUser);
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = POST, path = "/{userName}/photo")
  public String addPhotoToUser(@PathVariable String userName, @RequestBody String file) {
    try {
      String photoKey = userService.uploadPhoto(userName, file);
      return userService.findPhotoByName(photoKey);
    }
    catch (IOException e) {
      throw new IllegalArgumentException("There is was a problem uploading a photo for User " + userName, e);
    }
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = GET, path = "/{userName}/photo/{photoKey}")
  @ResponseBody
  public String getUserPhoto(@PathVariable String userName, @PathVariable String photoKey) {
    try {
      return userService.findPhotoByName(photoKey);
    }
    catch (IOException e) {
      throw new IllegalArgumentException("There is no photo for User " + userName, e);
    }
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = PUT, path = "/status")
  public ResponseEntity<String> updateStatus(@RequestBody HashMap<String, String> requestBody) {
    if(requestBody.containsKey("updatedStatus")) {
      User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      currentUser.setStatus(requestBody.get("updatedStatus"));
      userService.save(currentUser);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter `updatedStatus`");
  }

  /*
    Helper
   */

  private List<User> withoutCurrentUser(List<User> users, User currentUser) {
    return users.stream()
            .filter(
                    user -> !user.getUserName().equals(currentUser.getUserName())
            ).collect(Collectors.toList());
  }
}
