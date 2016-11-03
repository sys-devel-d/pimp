package com.pimp.commons;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.pimp.commons.exceptions.BadRequestException;
import com.pimp.commons.exceptions.DeleteEntityException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.commons.exceptions.ForbiddenException;
import com.pimp.commons.exceptions.PreconditionFailedException;
import com.pimp.commons.exceptions.UnauthorizedException;

@RestController
@RequestMapping({"/api"})
public abstract class AbstractRestController {

  protected abstract Logger getLogger();

  public AbstractRestController() {
  }

  @ExceptionHandler({IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public JSONError illegalArgument(IllegalArgumentException e) {
    getLogger().warn(e.getMessage(), e);
    return JSONError.badRequest();
  }

  @ExceptionHandler({BadRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public JSONError badRequest(BadRequestException e) {
    getLogger().warn(e.getMessage(), e);
    return JSONError.badRequest();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public JSONError serverError(Exception e) {
    getLogger().error("Unable to respond!", e);
    return JSONError.badImplementation();
  }

  @ExceptionHandler({PreconditionFailedException.class})
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  public JSONError preconditionFailed(PreconditionFailedException e) {
    getLogger().warn(e.getMessage(), e);
    return JSONError.preconditionFailed();
  }

  @ExceptionHandler({UnrecognizedPropertyException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public JSONError unrecognizedProperty(PreconditionFailedException e) {
    getLogger().warn(e.getMessage(), e);
    return JSONError.badRequest();
  }

  @ExceptionHandler({EntityNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public JSONError notFound(EntityNotFoundException e) {
    getLogger().warn(e.getMessage());
    return JSONError.notFound();
  }

  @ExceptionHandler({UnauthorizedException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public JSONError unauthorized(UnauthorizedException e) {
    getLogger().warn(e.getMessage(), e);
    return JSONError.unauthorized(e.getMessage());
  }

  @ExceptionHandler({ForbiddenException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public JSONError forbidden(ForbiddenException e) {
    getLogger().debug("Handle forbidden exception", e);
    return JSONError.forbidden();
  }

  @ExceptionHandler({EntityValidationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public JSONError entityValidation(EntityValidationException e) {
    getLogger().warn(e.getMessage(), e);
    return JSONError.badRequest();
  }


  @ExceptionHandler({DeleteEntityException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected JSONError deleteEntityException(DeleteEntityException e) {
    getLogger().error("Unable to delete entity: ", e);
    return JSONError.badImplementation("Cannot delete entity!");
  }

}
