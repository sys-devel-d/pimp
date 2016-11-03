package com.pimp.commons.exceptions;

/**
 * @author Kevin Goy
 */
public class PreconditionFailedException extends RuntimeException {

  public PreconditionFailedException() {
  }

  public PreconditionFailedException(String message) {
    super(message);
  }
}
