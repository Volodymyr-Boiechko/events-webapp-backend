package com.boiechko.eventswebapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends SystemApiException {

  public NotFoundException(final String message) {
    super(message, HttpStatus.NOT_FOUND);
  }

  public NotFoundException() {
    super("Resource not found", HttpStatus.NOT_FOUND);
  }
}
