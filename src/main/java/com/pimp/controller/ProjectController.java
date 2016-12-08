package com.pimp.controller;

import com.pimp.domain.Project;
import com.pimp.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(method = GET, path = "/{name}")
    public Project get(@PathVariable String name) {
        return projectService.find(name);
    }

    @RequestMapping(method = POST)
    public void create(@Valid @RequestBody Project project) {
        projectService.create(project);
    }

    @RequestMapping(method = DELETE, path = "/{name}")
    public void delete(@PathVariable String name) {
        projectService.delete(name);
    }
}