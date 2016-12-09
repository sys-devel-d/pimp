package com.pimp.controller;

import com.pimp.commons.exceptions.EntitiesNotFoundException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Project;
import com.pimp.services.ProjectService;
import com.pimp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/project")
@PreAuthorize("#oauth2.hasScope('user_actions')")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @RequestMapping(method = GET, path = "/{name}")
    public Project get(@PathVariable String name) {
        return projectService.find(name);
    }

    @RequestMapping(method = POST)
    public void create(@Valid @RequestBody Project project) {
        List<String> nonExistingUsers = project.getUserNames().stream()
                .filter(u -> !userService.exists(u))
                .collect(Collectors.toList());
        if (!nonExistingUsers.isEmpty()) {
            throw new EntitiesNotFoundException("The following users do not exist: " +
                nonExistingUsers.stream().reduce((s1, s2) -> s1 + ", " + s2).get()
            );
        }

        projectService.create(project);
    }

    @RequestMapping(method = DELETE, path = "/{name}")
    public void delete(@PathVariable String name) {
        projectService.delete(name);
    }

    @RequestMapping(method = PATCH, path = "/{projectName}/{userName}")
    public void add(@PathVariable String projectName, @PathVariable String userName) {
        if (!userService.exists(userName)) {
            throw new EntityNotFoundException("User " + userName + "does not exist.");
        }

        projectService.add(projectName, userName);
    }

    @RequestMapping(method = DELETE, path = "/{projectName}/{userName}")
    public void remove(@PathVariable String projectName, @PathVariable String userName) {
        if (!userService.exists(userName)) {
            throw new EntityNotFoundException("User " + userName + "does not exist.");
        }

        projectService.remove(projectName, userName);
    }
}
