package com.pimp.services;

import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.User;
import com.pimp.domain.UserDocument;
import com.pimp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public UserDocument createUser(User user) {
        UserDocument userDocument = new UserDocument()
                .setEmail(user.getEmail())
                .setUserName(user.getUserName())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRoles(Arrays.asList("USER"))
                .setPassword(encoder.encode(user.getPassword()));

        userRepository.save(userDocument);

        return userDocument;
    }

    public User findByUserName(String username) {
        UserDocument userDocument = userRepository.findByUserName(username);

        if (userDocument == null) {
            throw new EntityNotFoundException("Unable to find user " + username);
        }

        return new User()
                .setEmail(userDocument.getEmail())
                .setUserName(userDocument.getUserName())
                .setFirstName(userDocument.getFirstName())
                .setLastName(userDocument.getLastName())
                .setRoles(userDocument.getRoles())
                .setPassword(userDocument.getPassword());
    }
}
