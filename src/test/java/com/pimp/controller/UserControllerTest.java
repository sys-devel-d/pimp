package com.pimp.controller;

import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.User;
import com.pimp.services.UserService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .thenReturn(new User().setEmail("foo@bar.org").setStatus("Not available"));

        server.perform(get("/users/foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"foo@bar.org\", \"status\": \"Not available\"}"));
    }

    @Test
    public void testPasswordIsOmitted() throws Exception {
        when(userService.findByUserName(any()))
                .thenReturn(new User().setEmail("foo@bar.org").setPassword("secret"));

        server.perform(get("/users/foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"foo@bar.org\"}"))
                .andExpect(jsonPath("password").doesNotExist());
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
                        .content("{\"email\":\"foo@pim-plus.org\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

  @Test
  public void testCreateWithInvalidEmail() throws Exception {
        JSONObject json = new JSONObject();
        json
            .put("userName", "foo")
            .put("email", "foo@bar.org")
            .put("firstName", "Foo")
            .put("lastName", "Bar")
            .put("password", "foobarbaz");

        server.perform(post("/users")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json.toString()))
          .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateWithValidEmail() throws Exception {
    JSONObject json = new JSONObject();
    json
      .put("userName", "foo")
      .put("email", "foo@pim-plus.org")
      .put("firstName", "Foo")
      .put("lastName", "Bar")
      .put("password", "foobarbaz");

    server.perform(post("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json.toString()))
      .andExpect(status().isOk());
  }
}
