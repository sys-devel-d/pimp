package com.pimp.controller;

import com.pimp.domain.User;
import com.pimp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    User user = userService.findByUserName(userName);

    return user;
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = GET, path = "/{userName}/rooms")
  public List<String> getRooms(@PathVariable String userName) {
    return userService.findByUserName(userName).getRooms();
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = POST)
  public void createUser(@Valid @RequestBody User user) {
    userService.createUser(user);
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = GET, path = "/search/{query}")
  public List<User> searchUser(@PathVariable String query) {
    if (query.length() < 3) {
      throw new IllegalArgumentException("Search string should have a length >= 3.");
    }

    return userService.query(query, Arrays.asList("firstName", "lastName", "_id"));
  }
}
