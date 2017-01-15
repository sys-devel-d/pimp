package com.pimp.controller;

import com.pimp.domain.Project;
import com.pimp.services.ProjectService;
import com.pimp.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

    private MockMvc server;

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        server = MockMvcBuilders
                .standaloneSetup(new ProjectController(projectService, userService))
                .setControllerAdvice(new RestAdvice())
                .build();
    }

    @Test
    public void testGet() throws Exception {
        when(projectService.find("Foo"))
                .thenReturn(
                        new Project()
                                .setName("Foo")
                                .setUserNames(Arrays.asList("PatrickBateman", "PaulAllen")));

        server.perform(get("/project/Foo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"name\":\"Foo\",\"userNames\":[\"PatrickBateman\", \"PaulAllen\"]}"));
    }

    @Test
    public void testCreate() throws Exception {
        when(userService.exists(any())).thenReturn(true);

        Project project = new Project()
                .setName("Foo")
                .setUserNames(Arrays.asList("PatrickBateman", "PaulAllen"));

        server.perform(post("/project")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"name\":\"Foo\",\"userNames\":[\"PatrickBateman\", \"PaulAllen\"]}")
        )
                .andExpect(status().isOk());

        verify(projectService, only()).create(project);
    }

    @Test
    public void testPostWithEmpyName() throws Exception {
        server.perform(post("/project")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"name\":\"\",\"userNames\":[\"PatrickBateman\", \"PaulAllen\"]}")
        )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testPostWithMissingName() throws Exception {
        server.perform(post("/project")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"userNames\":[\"PatrickBateman\", \"PaulAllen\"]}")
        )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testDelete() throws Exception {
        server.perform(delete("/project/foo")).andExpect(status().isOk());

        verify(projectService, only()).delete("foo");
    }

    @Test
    public void testAddUser() throws Exception {
        when(userService.exists("userFoo")).thenReturn(true);
        server.perform(patch("/project/foo/userFoo"));

        verify(projectService, only()).add("foo", "userFoo");
    }

    @Test
    public void testAddNonExistingUser() throws Exception {
        when(userService.exists("userFoo")).thenReturn(false);
        server.perform(patch("/project/foo/userFoo"))
                .andExpect(status().isNotFound());
    }

  @Test
  public void testFindByKey() throws Exception {
    Project project = new Project()
      .setName("Foo")
      .setUserNames(Arrays.asList("PatrickBateman", "PaulAllen"));
    project.setKey("foo");

    when(projectService.findByKey(anyString())).thenReturn(project);

    ResultActions resultActions = server.perform(get("/project/id/foo"));
    resultActions
      .andExpect(status().isOk())
      .andExpect(content().json("{\"key\":\"foo\",\"name\":\"Foo\",\"userNames\":[\"PatrickBateman\", \"PaulAllen\"]}"));
  }
}
