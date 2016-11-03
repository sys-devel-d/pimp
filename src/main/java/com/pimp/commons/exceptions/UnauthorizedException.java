package com.pimp.commons.exceptions;

/**
 * @author Kevin Goy
 */
public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}
