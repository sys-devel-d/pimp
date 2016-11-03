package com.pimp.commons.exceptions;

public class ForbiddenException extends RuntimeException {

  public ForbiddenException() {
  }

  public ForbiddenException(String message) {
    super(message);
  }
}
