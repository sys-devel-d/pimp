package com.pimp.controller;

import com.pimp.domain.Project;
import com.pimp.services.ProjectService;
import com.pimp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/project")
@PreAuthorize("#oauth2.hasScope('user_actions')")
public class ProjectController extends GroupController<Project> {

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        super(projectService, userService);
    }
}
