package com.boiechko.eventswebapp.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class IdentificationTokenDto implements Serializable {

  private String temporaryToken;
  private String type;

  public IdentificationTokenDto(String temporaryToken) {
    this.temporaryToken = temporaryToken;
  }
}
