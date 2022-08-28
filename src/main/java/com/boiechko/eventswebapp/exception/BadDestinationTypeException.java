package com.boiechko.eventswebapp.exception;

import org.springframework.http.HttpStatus;

public class BadDestinationTypeException extends SystemApiException {

  public BadDestinationTypeException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }

  public BadDestinationTypeException() {
    super("Wrong destination type", HttpStatus.BAD_REQUEST);
  }
}
