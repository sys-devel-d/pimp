package com.pimp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.pimp.commons.exceptions.*;
import com.pimp.commons.mongo.JSONError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestAdvice {

  Logger log = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler({IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public JSONError illegalArgument(IllegalArgumentException e) {
    log.warn(e.getMessage(), e);
    return JSONError.badRequest();
  }

  @ExceptionHandler({BadRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public JSONError badRequest(BadRequestException e) {
    log.warn(e.getMessage(), e);
    return JSONError.badRequest();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public JSONError serverError(Exception e) {
    log.error("Unable to respond!", e);
    return JSONError.badImplementation();
  }

  @ExceptionHandler({PreconditionFailedException.class})
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  @ResponseBody
  public JSONError preconditionFailed(PreconditionFailedException e) {
    log.warn(e.getMessage(), e);
    return JSONError.preconditionFailed();
  }

  @ExceptionHandler({UnrecognizedPropertyException.class, MissingPathVariableException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public JSONError unrecognizedProperty(Exception e) {
    log.warn(e.getMessage(), e);
    return JSONError.badRequest();
  }

  @ExceptionHandler({EntityNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public JSONError notFound(EntityNotFoundException e) {
    log.warn(e.getMessage());
    return JSONError.notFound();
  }

  @ExceptionHandler({UnauthorizedException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public JSONError unauthorized(UnauthorizedException e) {
    log.warn(e.getMessage(), e);
    return JSONError.unauthorized(e.getMessage());
  }

  @ExceptionHandler({ForbiddenException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public JSONError forbidden(ForbiddenException e) {
    log.debug("Handle forbidden exception", e);
    return JSONError.forbidden();
  }

//  @ExceptionHandler({EntityValidationException.class})
//  @ResponseStatus(HttpStatus.BAD_REQUEST)
//  @ResponseBody
//  public JSONError entityValidation(EntityValidationException e) {
//    log.warn(e.getMessage(), e);
//    return JSONError.badRequest();
//  }


  @ExceptionHandler({DeleteEntityException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  protected JSONError deleteEntityException(DeleteEntityException e) {
    log.error("Unable to delete entity: ", e);
    return JSONError.badImplementation("Cannot delete entity!");
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  protected JSONError methodNotSupported(HttpRequestMethodNotSupportedException e) {
    log.warn("Method not supported: ", e);
    return JSONError.methodNotAllowed(e.getMessage());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ResponseBody
  protected JSONError methodArgumentNotValidException(MethodArgumentNotValidException e) throws JsonProcessingException {
    log.warn("Method not supported: ", e);
    return JSONError.unprocessableEntity("Validation Error", e.getBindingResult());
  }

}
