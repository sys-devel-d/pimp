package com.pimp.commons.exceptions;

/**
 * @author Kevin Goy
 */
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException() {
  }

  public EntityNotFoundException(String message) {
    super(message);
  }
}
