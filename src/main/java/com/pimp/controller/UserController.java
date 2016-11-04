package com.pimp.controller;

import com.pimp.domain.User;
import com.pimp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @RequestMapping(method = GET, path = "/{userName}")
    public User getUser(@PathVariable String userName) throws NotFoundException {
        User user = userService.findByUserName(userName);

        return user;
    }

    @RequestMapping(method = POST)
    public void createUser(@Valid @RequestBody User user) {
        userService.createUser(user);
    }
}