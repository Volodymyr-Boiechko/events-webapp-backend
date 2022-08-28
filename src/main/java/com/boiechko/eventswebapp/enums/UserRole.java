package com.boiechko.eventswebapp.enums;

import com.boiechko.eventswebapp.exception.NotFoundException;
import java.util.Objects;
import java.util.stream.Stream;

public enum UserRole {
  USER,
  USER_ORGANIZER,
  ADMIN;

  public static UserRole findByName(final String roleName) {
    return Stream.of(values())
        .filter(userRole -> Objects.equals(userRole.name(), roleName))
        .findFirst()
        .orElseThrow(() -> new NotFoundException("User role not found"));
  }
}
