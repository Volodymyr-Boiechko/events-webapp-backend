package com.boiechko.eventswebapp.dto;

import com.boiechko.eventswebapp.enums.DestinationType;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthTokenDto {

  private Long id;
  private UserDto user;
  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private LocalDateTime refreshTokenExpiresIn;
  private LocalDateTime issued;
  private LocalDateTime expires;
  private DestinationType destinationType;

}
