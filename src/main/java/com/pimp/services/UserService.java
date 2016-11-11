package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
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
        String userName = user.getUserName();
        if (existsWithUsername(userName)) {
            throw new EntityAlreadyExistsException("User already exists with username: " + userName);
        }
        String email = user.getEmail();
        if (existsWithEmail(email)) {
            throw new EntityAlreadyExistsException("User already exists with email: " + email);
        }

        UserDocument userDocument = UserDocument
                .from(user)
                .setRoles(Arrays.asList("USER"))
                .setPassword(encoder.encode(user.getPassword()));

        userRepository.save(userDocument);

        return userDocument;
    }

    private boolean existsWithUsername(String userName) {
        return userRepository.findByUserName(userName) != null;
    }

    private boolean existsWithEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User findByUserName(String username) {
        UserDocument userDocument = userRepository.findByUserName(username);

        if (userDocument == null) {
            throw new EntityNotFoundException("Unable to find user " + username);
        }

        return User.from(userDocument);
    }

}
