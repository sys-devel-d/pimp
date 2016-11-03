package com.pimp.commons.exceptions;

/**
 * @author Kevin Goy
 */
public class SpringBeanCreationException extends Exception {
  public SpringBeanCreationException(String s) {
    super(s);
  }

  public SpringBeanCreationException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
