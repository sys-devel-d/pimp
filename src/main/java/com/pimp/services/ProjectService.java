package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Project;
import com.pimp.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository repository;

    @Autowired
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public void create(Project project) {
        if (repository.findByName(project.getName()) != null) {
            throw new EntityAlreadyExistsException("Project with name " + project.getName() + " already exists.");
        }

        repository.save(project);
    }


    public Project find(String name) {
        Project project = repository.findByName(name);

        if (project == null) {
            throw new EntityNotFoundException("Project " + name + " does not exist.");
        }

        return project;
    }

    public List<Project> findByUserName(String userName) {
        List<Project> projects = repository.findByUserName(userName);

        if (projects.isEmpty()) {
            throw new EntityNotFoundException("User " + userName + " is not member of any project");
        }

        return projects;
    }

    public void delete(String name) {
        Project project = repository.findByName(name);
        if (project == null) {
            throw new EntityNotFoundException("Project " + name + " cannot be deleted, since it does not exist.");
        }

        repository.delete(project);
    }

    public void add(String projectName, String userName) {
        Project project = repository.findByName(projectName);

        if (project == null) {
            throw new EntityNotFoundException("Project " + projectName + " cannot be modified, since it does not exist.");
        }

        project.add(userName);

        repository.save(project);
    }

    public void remove(String projectName, String userName) {
        Project project = repository.findByName(projectName);

        if (project == null) {
            throw new EntityNotFoundException("Project " + projectName + " cannot be modified, since it does not exist.");
        }

        project.remove(userName);

        repository.save(project);
    }
}
