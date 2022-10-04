package com.boiechko.eventswebapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {

  private Long id;
  private String country;
  private String city;
  private String street;
  private String postalCode;

  public AddressDto(final LocationDto locationDto) {
    this.country = locationDto.getCountry();
    this.city = locationDto.getCity();
  }
}
