package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.User;
import com.pimp.domain.UserDocument;
import com.pimp.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    UserService userService;
    private BCryptPasswordEncoder encoder;

    @Before
    public void setUp() throws Exception {
        encoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, encoder);
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User()
                .setEmail("foo@bar.org")
                .setUserName("fooMeister")
                .setFirstName("Foo")
                .setLastName("Bar")
                .setPassword("secret");
        UserDocument userDocument = userService.createUser(user);

        assertThat(userDocument.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDocument.getUserName()).isEqualTo(user.getUserName());
        assertThat(userDocument.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDocument.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDocument.getRoles()).isEqualTo(Arrays.asList("USER"));
        assertThat(encoder.matches(user.getPassword(), userDocument.getPassword())).isTrue();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindNotExistingUser() throws Exception {
        when(userRepository.findByUserName(any())).thenReturn(null);

        userService.findByUserName("foo");
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testCreateAlreadyExistingUser() throws Exception {
        when(userRepository.findByUserName(any())).thenReturn(new UserDocument());

        User user = new User().setEmail("foo");
        userService.createUser(user);
    }
}