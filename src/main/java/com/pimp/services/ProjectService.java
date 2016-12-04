package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Project;
import com.pimp.domain.ProjectDocument;
import com.pimp.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository repository;

    @Autowired
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public void createProject(Project project) {
        if (repository.findByName(project.getName()) != null) {
            throw new EntityAlreadyExistsException("Project with name " + project.getName() + " already exists.");
        }

        repository.save(ProjectDocument.from(project));
    }


    public Project find(String name) {
        ProjectDocument document = repository.findByName(name);

        if (document == null) {
            throw new EntityNotFoundException("Project " + name + " does not exist.");
        }

        return Project.from(document);
    }

    public List<Project> findByUserName(String userName) {
        List<ProjectDocument> documents = repository.findByUserName(userName);

        if (documents.isEmpty()) {
            throw new EntityNotFoundException("User " + userName + " is not member of any project");
        }

        return documents.stream().map(Project::from).collect(Collectors.toList());
    }
}
