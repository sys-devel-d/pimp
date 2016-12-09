package com.pimp.services;

import com.pimp.PimpRestApplication;
import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
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
import static org.assertj.core.api.AssertionsForClassTypes.fail;

/*
 * This suite should only be run with maven verify, to avoid messing up your db,
 * instead the embedded mongo db will be used.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PimpRestApplication.class, MongoConfig.class, ProjectService.class, ProjectRepository.class},
        properties = {"db.port=37017", "banner."})
public class ProjectServiceIT {

    @Autowired
    ProjectService service;

    @Test(expected = EntityAlreadyExistsException.class)
    public void testDuplicate() throws Exception {
        Project project = new Project().setName("FooProject").setUserNames(Arrays.asList("Foo", "Bar"));

        service.create(project);
        service.create(project);
    }

    @Test
    public void testFindProject() throws Exception {
        Project project = new Project().setName("FooProject").setUserNames(Arrays.asList("Foo", "Bar"));
        service.create(project);

        Project queriedProject = service.find("FooProject");

        assertThat(queriedProject).isNotNull();
        assertThat(queriedProject.getUserNames()).contains("Foo", "Bar");
    }

    @Test
    public void testFindByUsername() throws Exception {
        Project projectA = new Project().setName("ProjectA").setUserNames(Arrays.asList("PatrickBateman", "PaulAllen"));
        Project projectB = new Project().setName("ProjectB").setUserNames(Arrays.asList("PaulAllen", "Peterson"));
        service.create(projectA);
        service.create(projectB);

        List<Project> ProjectList = service.findByUserName("PaulAllen");

        assertThat(ProjectList).hasSize(2);
    }

    @Test
    public void testDelete() throws Exception {
        Project project = new Project().setName("Project").setUserNames(Arrays.asList("Foo", "Bar"));

        assertAbsent("Project");

        service.create(project);
        service.delete("Project");

        assertAbsent("Project");
    }

    @Test
    public void testAdd() throws Exception {
        Project project = new Project().setName("UnderstaffedProject");

        service.create(project);
        service.add(project.getName(), "NewUser");

        Project foundProject = service.find(project.getName());

        assertThat(foundProject.getUserNames()).contains("NewUser");
        assertThat(project.getKey()).isEqualTo(foundProject.getKey());
    }

    @Test
    public void testRemove() throws Exception {
        Project project = new Project().setName("OverstaffedProject").add("OldUser");

        service.create(project);
        service.remove(project.getName(), "OldUser");

        Project foundProject = service.find(project.getName());

        assertThat(foundProject.getUserNames()).doesNotContain("OldUser");
        assertThat(project.getKey()).isEqualTo(foundProject.getKey());
    }

    private void assertAbsent(String name) {
        try {
            service.find(name);
            fail("FooProject was found, but should not exist.");
        } catch (EntityNotFoundException e) {
            // ok
        }
    }
}