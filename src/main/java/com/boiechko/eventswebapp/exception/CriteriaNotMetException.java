package com.boiechko.eventswebapp.exception;

import org.springframework.http.HttpStatus;

public class CriteriaNotMetException extends SystemApiException {

  public CriteriaNotMetException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }

  public CriteriaNotMetException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
