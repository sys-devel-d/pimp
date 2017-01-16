package com.pimp.controller;

import com.pimp.domain.User;
import com.pimp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

  @RequestMapping(method = GET, path = "/{userName}")
  @PreAuthorize("#oauth2.hasScope('user_actions')")
  public User getUser(@PathVariable String userName) throws NotFoundException {
    return userService.findByUserName(userName);
  }

  @RequestMapping(method = POST)
  public void createUser(@Valid @RequestBody User user) {
    if (user.getEmail().endsWith("@pim-plus.org")) {
      userService.createUser(user);
    } else {
      throw new IllegalArgumentException("Only accepting mail addresses ending with '@pim-plus.org'");
    }
  }

  @RequestMapping(method = GET)
  @PreAuthorize("#oauth2.hasScope('user_actions')")
  public List<User> getAllUsers() {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return withoutCurrentUser(userService.findAll(), currentUser);
  }

  @RequestMapping(method = GET, path = "/search/{query}")
  @PreAuthorize("#oauth2.hasScope('user_actions')")
  public List<User> searchUser(@PathVariable String query) {
    if (query.length() < 3) {
      throw new IllegalArgumentException("Search string should have a length >= 3.");
    }
    User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<User> foundUsers = userService.query(query, Arrays.asList("firstName", "lastName", "_id"));

    return withoutCurrentUser(foundUsers, loggedInUser);
  }

  @RequestMapping(method = GET, path = "/search/all/{query}")
  @PreAuthorize("#oauth2.hasScope('user_actions')")
  public List<User> searchAllUser(@PathVariable String query) {
    if (query.length() < 3) {
      throw new IllegalArgumentException("Search string should have a length >= 3.");
    }
    List<User> foundUsers = userService.query(query, Arrays.asList("firstName", "lastName", "_id"));

    return foundUsers;
  }

  @RequestMapping(method = POST, path = "/{userName}/photo")
  @PreAuthorize("#oauth2.hasScope('user_actions')")
  public String addPhotoToUser(@PathVariable String userName, @RequestBody String file) {
    try {
      String photoKey = userService.uploadPhoto(userName, file);
      return userService.findPhotoByName(photoKey);
    }
    catch (IOException e) {
      throw new IllegalArgumentException("There is was a problem uploading a photo for User " + userName, e);
    }
  }

  @RequestMapping(method = GET, path = "/{userName}/photo/{photoKey}")
  @ResponseBody
  @PreAuthorize("#oauth2.hasScope('user_actions')")
  public String getUserPhoto(@PathVariable String userName, @PathVariable String photoKey) {
    try {
      return userService.findPhotoByName(photoKey);
    }
    catch (IOException e) {
      throw new IllegalArgumentException("There is no photo for User " + userName, e);
    }
  }

  @RequestMapping(method = PUT, path = "/status")
  @PreAuthorize("#oauth2.hasScope('user_actions')")
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
