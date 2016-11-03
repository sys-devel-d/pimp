package com.pimp.commons.exceptions;

/**
 * @author Kevin Goy
 */
public class DeleteEntityException extends Exception {

  public DeleteEntityException(String message) {
    super(message);
  }

  public DeleteEntityException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
