package com.boiechko.eventswebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenDto {

  private String jwtToken;
}
