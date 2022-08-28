package com.boiechko.eventswebapp.dto;

import lombok.Data;

@Data
public class IdentificationTokenDto {

  private String temporaryToken;
  private String type;

  public IdentificationTokenDto(String temporaryToken) {
    this.temporaryToken = temporaryToken;
  }
}
