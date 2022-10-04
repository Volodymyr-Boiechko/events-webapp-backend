package com.boiechko.eventswebapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LocationDto {

  private String country;
  private String city;
}
