package com.boiechko.eventswebapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
  BEARER("Bearer"),
  IDENTIFICATION("Identification");

  private final String value;
}
