package com.pimp.commons;

import org.springframework.http.HttpStatus;

/**
 * @author Kevin Goy
 */
public class JSONError {

  private final int statusCode;
  private final String error;
  private final String message;

  private JSONError(int statusCode, String error, String message) {
    this.statusCode = statusCode;
    this.error = error;
    this.message = message;
  }

  private JSONError(HttpStatus httpStatus, String message) {
    this(httpStatus.value(), httpStatus.getReasonPhrase(), message);
  }

  public static JSONError create(HttpStatus httpStatus, String message) {
    return new JSONError(httpStatus, message);
  }

  public static JSONError badRequest(String message) {
    return create(HttpStatus.BAD_REQUEST, message);
  }

  public static JSONError badRequest() {
    return badRequest((String) null);
  }

  public static JSONError unauthorized(String message) {
    return create(HttpStatus.UNAUTHORIZED, message);
  }

  public static JSONError unauthorized() {
    return unauthorized((String) null);
  }

  public static JSONError forbidden(String message) {
    return create(HttpStatus.FORBIDDEN, message);
  }

  public static JSONError forbidden() {
    return forbidden((String) null);
  }

  public static JSONError notFound(String message) {
    return create(HttpStatus.NOT_FOUND, message);
  }

  public static JSONError notFound() {
    return notFound((String) null);
  }

  public static JSONError methodNotAllowed(String message) {
    return create(HttpStatus.METHOD_NOT_ALLOWED, message);
  }

  public static JSONError methodNotAllowed() {
    return methodNotAllowed((String) null);
  }

  public static JSONError conflict(String message) {
    return create(HttpStatus.CONFLICT, message);
  }

  public static JSONError conflict() {
    return conflict((String) null);
  }

  public static JSONError preconditionFailed(String message) {
    return create(HttpStatus.PRECONDITION_FAILED, message);
  }

  public static JSONError preconditionFailed() {
    return preconditionFailed((String) null);
  }

  public static JSONError badImplementation(String message) {
    return create(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

  public static JSONError badImplementation() {
    return badImplementation((String) null);
  }

  public static JSONError notImplemented(String message) {
    return create(HttpStatus.NOT_IMPLEMENTED, message);
  }

  public static JSONError notImplemented() {
    return notImplemented((String) null);
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public String getError() {
    return this.error;
  }

  public String getMessage() {
    return this.message;
  }

  public String toString() {
    return "[JSONError " + this.statusCode + " " + this.error + ": " + this.message + "]";
  }
}
