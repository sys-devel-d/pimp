package com.pimp.commons.exceptions;

import com.pimp.commons.mongo.IKeyedObject;

/**
 * @author Kevin Goy
 */
public class EntityValidationException extends Exception {
  private IKeyedObject entity;

  public IKeyedObject getEntity() {
    return this.entity;
  }

  public EntityValidationException(String message) {
    super(message);
  }

  public EntityValidationException(String message, IKeyedObject entity) {
    super(message);
    this.entity = entity;
  }

  public EntityValidationException(String message, Exception cause, IKeyedObject entity) {
    super(message, cause);
    this.entity = entity;
  }
}
