package com.pimp.controller;

import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.User;
import com.pimp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = GET, path = "/{userName}")
    public User getUser(@PathVariable String userName) throws NotFoundException {
        User user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new EntityNotFoundException(String.format("Unable to find user " + userName));
        }

        return user;
    }

    @RequestMapping(method = POST)
    public void createUser(@Valid @RequestBody User user) {
        userRepository.save(user);
    }
}
