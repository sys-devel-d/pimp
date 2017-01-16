package com.pimp.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pimp.commons.exceptions.EntitiesNotFoundException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Group;
import com.pimp.services.GroupService;
import com.pimp.services.UserService;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public abstract class GroupController<T extends Group> {

  private final GroupService<T> groupService;
  private final UserService userService;

  public GroupController(GroupService groupService, UserService userService) {
    this.groupService = groupService;
    this.userService = userService;
  }

  @RequestMapping(method = GET, path = "/user/{userName}")
  public List<T> getList(@PathVariable String userName) {
    return groupService.findByUserName(userName);
  }

  @Deprecated
  @RequestMapping(method = GET, path = "/{name}")
  public T get(@PathVariable String name) {
    return groupService.find(name);
  }

  @Deprecated
  @RequestMapping(method = GET)
  public List<T> getAll() {
    return groupService.findAll();
  }

  @RequestMapping(method = GET, path = "/id/{key}")
  public T getByKey(@PathVariable String key) {
    return groupService.findByKey(key);
  }

  @RequestMapping(method = PATCH, path = "/id/{key}")
  public T editGroup(@PathVariable String key, @Valid @RequestBody T newGroup) {
    T group = groupService.findByKey(key);
    group.setName(newGroup.getName());
    group.setUserNames(newGroup.getUserNames());
    return groupService.save(group);
  }

  @RequestMapping(method = POST)
  public T create(@Valid @RequestBody T group) {
    List<String> nonExistingUsers =
      group.getUserNames().stream().filter(u -> !userService.exists(u)).collect(Collectors.toList());
    if (!nonExistingUsers.isEmpty()) {
      throw new EntitiesNotFoundException(
        "The following users do not exist: " + nonExistingUsers.stream().reduce((s1, s2) -> s1 + ", " + s2).get());
    }

    return groupService.create(group);
  }

  @RequestMapping(method = DELETE, path = "/id/{key}")
  public void delete(@PathVariable String key) {
    groupService.delete(key);
  }

  @RequestMapping(method = PATCH, path = "/{groupName}/{userName}")
  public void add(@PathVariable String groupName, @PathVariable String userName) {
    if (!userService.exists(userName)) {
      throw new EntityNotFoundException("User " + userName + "does not exist.");
    }

    groupService.add(groupName, userName);
  }
}
