package com.pimp.controller;

import com.pimp.domain.User;
import com.pimp.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private MockMvc server;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        // ViewResolver needed to avoid circular dispatch warning
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/foo");

        server = MockMvcBuilders
                .standaloneSetup(new UserController(userRepository))
                .setControllerAdvice(new RestAdvice())
//                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testGetUser() throws Exception {
        when(userRepository.findByUserName(any()))
                .thenReturn(new User().setEmail("foo@bar.org"));

        server.perform(get("/users/foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"foo@bar.org\"}"));
    }

    @Test
    public void testPasswordIsOmitted() throws Exception {
        when(userRepository.findByUserName(any()))
                .thenReturn(new User().setEmail("foo@bar.org").setPassword("secret"));

        server.perform(get("/users/foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"foo@bar.org\"}"));
    }

    @Test
    public void testMissingUserParam() throws Exception {
        server.perform(get("/users")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testNotExistingUser() throws Exception {
        when(userRepository.findByUserName(any()))
                .thenReturn(null);

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