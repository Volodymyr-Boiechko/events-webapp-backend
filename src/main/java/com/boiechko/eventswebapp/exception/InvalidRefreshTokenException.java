package com.boiechko.eventswebapp.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends SystemApiException {

  public InvalidRefreshTokenException(String message, HttpStatus status) {
    super(message, status);
  }
}
