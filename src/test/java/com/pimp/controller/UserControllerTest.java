package com.pimp.controller;

import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.User;
import com.pimp.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest extends ControllerTest {

    private MockMvc server;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        server = MockMvcBuilders
                .standaloneSetup(new UserController(userService))
                .setControllerAdvice(new RestAdvice())
                .build();
    }

    @Test
    public void testGetUser() throws Exception {
        when(userService.findByUserName(any()))
                .thenReturn(new User().setEmail("foo@bar.org"));

        server.perform(get("/users/foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"foo@bar.org\"}"));
    }

    @Test
    public void testPasswordIsOmitted() throws Exception {
        when(userService.findByUserName(any()))
                .thenReturn(new User().setEmail("foo@bar.org").setPassword("secret"));

        server.perform(get("/users/foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"foo@bar.org\"}"));
    }

    @Test
    public void testMissingUserParam() throws Exception {
        when(userService.findAll()).thenReturn(new ArrayList<>());
        server.perform(get("/users")).andExpect(content().json("[]"));
    }

    @Test
    public void testNotExistingUser() throws Exception {
        when(userService.findByUserName(any()))
                .thenThrow(new EntityNotFoundException());

        server.perform(get("/users/foo"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUserWithInvalidContent() throws Exception {
        server.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"foo@bar.org\"}"))
                .andExpect(status().isUnprocessableEntity());
    }
}