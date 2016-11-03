package com.pimp.commons.mongo;

/**
 * @author Kevin Goy
 */
public interface IUpdateListener<T extends IKeyedObject> {
  void onUpdate(T var1);

  void onDelete(T var1);

  void onDeleteAll();
}
