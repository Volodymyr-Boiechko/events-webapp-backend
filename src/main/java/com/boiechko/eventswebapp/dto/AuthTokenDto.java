package com.boiechko.eventswebapp.dto;

import static com.boiechko.eventswebapp.util.DateUtils.convertEpochMillis;

import com.boiechko.eventswebapp.enums.DestinationType;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.social.oauth2.AccessGrant;

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

  public AuthTokenDto(final AccessGrant accessGrant) {
    this.accessToken = accessGrant.getAccessToken();
    this.expires = convertEpochMillis(accessGrant.getExpireTime());
    this.refreshToken = accessGrant.getRefreshToken();
  }
}
