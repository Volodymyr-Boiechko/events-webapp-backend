package com.boiechko.eventswebapp.exception;

public class AuthTokenNotFoundException extends NotFoundException {

  public AuthTokenNotFoundException() {
    super("Auth token not found");
  }

  public AuthTokenNotFoundException(String message) {
    super(message);
  }
}
