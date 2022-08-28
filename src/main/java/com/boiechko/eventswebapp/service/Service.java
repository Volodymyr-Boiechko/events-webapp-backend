package com.boiechko.eventswebapp.service;

import java.util.Objects;
import lombok.NonNull;

public interface Service<T> {

  /**
   * Service type.
   *
   * @return a type that service can handle
   */
  T getType();

  default boolean canHandle(@NonNull final T t) {
    return Objects.equals(getType(), t);
  }
}
