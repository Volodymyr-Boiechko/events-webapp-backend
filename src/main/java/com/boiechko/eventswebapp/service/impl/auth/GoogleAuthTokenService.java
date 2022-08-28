package com.boiechko.eventswebapp.service.impl.auth;

import static com.boiechko.eventswebapp.util.DateUtils.convertEpochMillis;
import static com.boiechko.eventswebapp.util.DateUtils.getCurrentDateTime;

import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.enums.DestinationType;
import com.boiechko.eventswebapp.exception.AuthTokenNotFoundException;
import com.boiechko.eventswebapp.mapper.AuthTokenMapper;
import com.boiechko.eventswebapp.repository.AuthTokenRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class GoogleAuthTokenService extends AbstractAuthTokenService {

  @Value("${google.clientId}")
  private String clientId;

  @Value("${google.clientSecret}")
  private String clientSecret;

  @Value("${google.refresh-token-expires-in-days}")
  private Long refreshTokenExpiresInDays;

  public GoogleAuthTokenService(
      final AuthTokenRepository authTokenRepository, final AuthTokenMapper authTokenMapper) {
    super(DestinationType.GOOGLE, authTokenRepository, authTokenMapper);
  }

  @Override
  protected AuthTokenDto refreshTokenFlow(final AuthTokenDto authTokenDto) {
    final AccessGrant refreshedAccessGrant = getRefreshedAccessGrant(authTokenDto);
    return mapToAuthTokenDto(refreshedAccessGrant, authTokenDto);
  }

  private AuthTokenDto mapToAuthTokenDto(
      final AccessGrant accessGrant, final AuthTokenDto authTokenDto) {
    final LocalDateTime now = getCurrentDateTime();
    authTokenDto.setAccessToken(accessGrant.getAccessToken());
    authTokenDto.setIssued(now);
    authTokenDto.setExpires(convertEpochMillis(accessGrant.getExpireTime()));
    authTokenDto.setRefreshToken(accessGrant.getRefreshToken());
    authTokenDto.setRefreshTokenExpiresIn(now.plusDays(refreshTokenExpiresInDays));
    return authTokenDto;
  }

  private AccessGrant getRefreshedAccessGrant(final AuthTokenDto authTokenDto) {

    final GoogleConnectionFactory googleConnectionFactory =
        new GoogleConnectionFactory(clientId, clientSecret);

    try {
      return googleConnectionFactory
          .getOAuthOperations()
          .refreshAccess(authTokenDto.getRefreshToken(), null);
    } catch (HttpClientErrorException e) {
      if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED)) {
        deleteById(authTokenDto.getId());
        throw new AuthTokenNotFoundException(
            "Your token has expired and cannot be refreshed, you need obtain a new one.");
      } else {
        throw basedOnStatusException(e);
      }
    }
  }
}
