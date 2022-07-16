package com.boiechko.eventswebapp.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends SystemApiException {

  public UnauthorizedException(final String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
