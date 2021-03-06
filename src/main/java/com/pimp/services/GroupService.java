package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Group;
import com.pimp.repositories.GroupRepository;

import java.util.List;

public abstract class GroupService<T extends Group> {

    private GroupRepository<T> repository;

    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    protected abstract String getGroupType();

    public T create(T group) {
        if (repository.findByName(group.getName()) != null) {
            throw new EntityAlreadyExistsException(getGroupType() + " with name " + group.getName() + " already exists.");
        }

        return repository.save(group);
    }

    public T findByKey(String key) {
      T group = repository.findByKey(key);

      if (group == null) {
        throw new EntityNotFoundException(getGroupType() + " with key " + key + " does not exist.");
      }

      return group;
    }

    public T find(String name) {
      T group = repository.findByName(name);

      if (group == null) {
        throw new EntityNotFoundException(getGroupType() + " " + name + " does not exist.");
      }

      return group;
    }

    public List<T> findAll() {
      List<T> groups = repository.findAll();

      if (groups == null) {
        throw new EntityNotFoundException("There are no " + getGroupType());
      }

      return groups;
    }

    public List<T> findByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    public void delete(String key) {
        T group = repository.findByKey(key);
        if (group == null) {
            throw new EntityNotFoundException(getGroupType() + " " + key+ " cannot be deleted, since it does not exist.");
        }

        repository.delete(group);
    }

    public void add(String projectName, String userName) {
        T group = repository.findByName(projectName);

        if (group == null) {
            throw new EntityNotFoundException(getGroupType() +  " " + projectName + " cannot be modified, since it does not exist.");
        }

        group.add(userName);

        repository.save(group);
    }

    public T save(T group) { return repository.save(group); }
}
