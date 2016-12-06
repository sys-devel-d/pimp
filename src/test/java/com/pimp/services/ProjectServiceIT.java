package com.pimp.services;

import com.pimp.PimpRestApplication;
import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.config.MongoConfig;
import com.pimp.domain.Project;
import com.pimp.repositories.ProjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * This suite should only be run with maven verify, to avoid messing up your db,
 * instead the embedded mongo db will be used.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PimpRestApplication.class, MongoConfig.class, ProjectService.class, ProjectRepository.class},
        properties = "db.port=37017")
public class ProjectServiceIT {

    @Autowired
    ProjectService service;

    @Test(expected = EntityAlreadyExistsException.class)
    public void testDuplicate() throws Exception {
        Project project = new Project().setName("FooProject").setUserNames(Arrays.asList("Foo", "Bar"));

        service.createProject(project);
        service.createProject(project);
    }

    @Test
    public void testFindProject() throws Exception {
        Project project = new Project().setName("FooProject").setUserNames(Arrays.asList("Foo", "Bar"));
        service.createProject(project);

        Project queriedProject = service.find("FooProject");

        assertThat(queriedProject).isNotNull();
        assertThat(queriedProject.getUserNames()).contains("Foo", "Bar");
    }

    @Test
    public void testFindByUsername() throws Exception {
        Project projectA = new Project().setName("ProjectA").setUserNames(Arrays.asList("PatrickBateman", "PaulAllen"));
        Project projectB = new Project().setName("ProjectB").setUserNames(Arrays.asList("PaulAllen", "Peterson"));
        service.createProject(projectA);
        service.createProject(projectB);

        List<Project> projectList = service.findByUserName("PaulAllen");

        assertThat(projectList).hasSize(2);
    }
}