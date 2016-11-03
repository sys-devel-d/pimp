package com.pimp.commons;

import java.net.IDN;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.google.common.collect.Lists;
import com.pimp.commons.exceptions.DeleteEntityException;
import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.commons.exceptions.SpringBeanCreationException;

/**
 * @author Kevin Goy
 */
public abstract class AbstractEntityService<T extends IKeyedObject> extends PimpSpringBean
    implements IAbstractEntityService<T> {

  protected static final int MAX_UNIQUE_KEY_ATTEMPTS = 9;

  protected IKeyedObjectRepository<T> entityRepository;

  protected List<IUpdateListener<T>> updateListeners = new ArrayList<>();

  private Class<T> entityClass;

  public AbstractEntityService(IKeyedObjectRepository<T> entityRepository, Class<T> entityClass) {
    this.entityRepository = entityRepository;
    this.entityClass = entityClass;
  }

  protected void postConstruct() throws SpringBeanCreationException {
    super.postConstruct();

    try {
      createInitialEntities();
    }
    catch (EntityValidationException e) {
      throw new SpringBeanCreationException("Unable to create entity service!", e);
    }
  }

  @Override
  public void createInitialEntities() throws EntityValidationException {
    // nothing
  }

  @Override
  public String generateUniqueKey(String name) throws IllegalStateException {

    String punyCode = IDN.toASCII(name).toUpperCase();
    String sanitizedKey = punyCode.replaceAll("\\W", "_");
    String resultKey = sanitizedKey.toUpperCase();
    int counter = 0;
    while (this.has(resultKey)) {
      counter++;
      resultKey = (sanitizedKey + "." + UUID.randomUUID()).toUpperCase();
      if (counter > MAX_UNIQUE_KEY_ATTEMPTS) {
        throw new IllegalStateException("Could not generate unique entity key");
      }
    }
    return resultKey;
  }

  @Override
  public void insert(T entity) throws EntityValidationException {
    insert(entity, true);
  }

  @Override
  public void insert(T entity, boolean validate) throws EntityValidationException {

    if (validate) {
      validate(entity);
    }

    entityRepository.save(entity);
    onUpdate(entity);
  }

  public void insertAll(List<T> entities) throws EntityValidationException {
    insertAll(entities, true);
  }

  public void insertAll(List<T> entities, boolean validate) throws EntityValidationException {

    if (validate) {
      for (T entity : entities) {
        validate(entity);
      }
    }

    entityRepository.save(entities);
  }

  @Override
  public void insertOrUpdate(T entity) throws EntityValidationException {
    insertOrUpdate(entity, true);
  }

  @Override
  public void insertOrUpdate(T entity, boolean validate) throws EntityValidationException {

    if (entity.getKey() != null && has(entity.getKey())) {
      update(entity, validate);
    }
    else {
      insert(entity, validate);
    }
  }

  @Override
  public void update(T entity) throws EntityValidationException {
    update(entity, true);
  }

  @Override
  public void update(T entity, boolean validate) throws EntityValidationException {
    if (validate) {
      validate(entity);
    }

    entityRepository.save(entity);
    onUpdate(entity);
  }

  @Override
  public T get(String entityKey) {
    return entityRepository.findOne(entityKey);
  }

  @Override
  public Iterator<T> getEntityIterator() {
    return entityRepository.findAll().iterator();
  }

  @Override
  public Iterator<T> getEntityIterator(int first, int count) {
    return getEntityIterator(first, count, null, true);
  }

  @Override
  public Iterator<T> getEntityIterator(int first, int count, String sortField, boolean ascendingSort) {

    PageRequest pageRequest;
    if (sortField != null) {
      Sort.Direction direction = ascendingSort ? Sort.Direction.ASC : Sort.Direction.DESC;
      pageRequest = new PageRequest(first, count, new Sort(direction, sortField));
    }
    else {
      pageRequest = new PageRequest(first, count);
    }
    return entityRepository.findAll(pageRequest).iterator();
  }

  @Override
  public long getSize() {
    return entityRepository.count();
  }

  @Override
  public void delete(T entity) throws DeleteEntityException {
    for (IUpdateListener<T> updateListener : updateListeners) {
      updateListener.onDelete(entity);
    }

    entityRepository.delete(entity);
  }

  @Override
  public void deleteAll() throws DeleteEntityException {
    for (IUpdateListener updateListener : updateListeners) {
      updateListener.onDeleteAll();
    }

    entityRepository.deleteAll();
  }

  @Override
  public boolean has(String entityKey) {
    return entityRepository.exists(entityKey);
  }

  @Override
  public List<String> getKeys() {
    return entityRepository.findAll().stream().map(IKeyedObject::getKey).collect(Collectors.toList());
  }

  @Override
  public Class<T> getEntityClass() {
    return entityClass;
  }

  @Override
  public List<T> getEntitiesByKeys(List<String> keys) {
    return Lists.newArrayList(entityRepository.findAll(keys));
  }

  @Override
  public void addUpdateListener(IUpdateListener<T> updateListener) {
    updateListeners.add(updateListener);
  }

  protected void onUpdate(T entity) {
    for (IUpdateListener<T> updateListener : updateListeners) {
      updateListener.onUpdate(entity);
    }
  }

  public void setUpdateListeners(List<IUpdateListener<T>> updateListeners) {
    this.updateListeners = updateListeners;
  }

}