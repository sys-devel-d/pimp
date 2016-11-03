package com.pimp.commons;

import java.util.Iterator;
import java.util.List;

import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.commons.exceptions.DeleteEntityException;

/**
 * @author Kevin Goy
 */
public interface IAbstractEntityService<T extends IKeyedObject> {
  T create();

  void createInitialEntities() throws EntityValidationException;

  String generateUniqueKey(String var1) throws IllegalStateException;

  void validate(T var1) throws EntityValidationException;

  void insert(T var1) throws EntityValidationException;

  void insert(T var1, boolean var2) throws EntityValidationException;

  void insertAll(List<T> var1) throws EntityValidationException;

  void insertAll(List<T> var1, boolean var2) throws EntityValidationException;

  void insertOrUpdate(T var1) throws EntityValidationException;

  void insertOrUpdate(T var1, boolean var2) throws EntityValidationException;

  void update(T var1) throws EntityValidationException;

  void update(T var1, boolean var2) throws EntityValidationException;

  T get(String var1);

  Iterator<T> getEntityIterator();

  Iterator<T> getEntityIterator(int var1, int var2);

  Iterator<T> getEntityIterator(int var1, int var2, String var3, boolean var4);

  long getSize();

  void delete(T var1) throws DeleteEntityException;

  void deleteAll() throws DeleteEntityException;

  boolean has(String var1);

  List<String> getKeys();

  Class<T> getEntityClass();

  List<T> getEntitiesByKeys(List<String> var1);

  void addUpdateListener(IUpdateListener<T> var1);
}
