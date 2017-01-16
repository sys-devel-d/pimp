package com.pimp.controller;

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

@RestController
@RequestMapping(path = "/project")
@PreAuthorize("#oauth2.hasScope('user_actions')")
public class ProjectController extends GroupController<Project> {

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        super(projectService, userService);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Project create(@Valid @RequestBody Project group) {
        return super.create(group);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void delete(@PathVariable String key) {
        super.delete(key);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void add(@PathVariable String groupName, @PathVariable String userName) {
        super.add(groupName, userName);
    }

}
