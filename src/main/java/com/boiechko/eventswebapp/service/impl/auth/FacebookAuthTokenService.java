package com.boiechko.eventswebapp.service.impl.auth;

import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.enums.DestinationType;
import com.boiechko.eventswebapp.mapper.AuthTokenMapper;
import com.boiechko.eventswebapp.repository.AuthTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class FacebookAuthTokenService extends AbstractAuthTokenService {

  public FacebookAuthTokenService(
      final AuthTokenRepository authTokenRepository, final AuthTokenMapper authTokenMapper) {
    super(DestinationType.FACEBOOK, authTokenRepository, authTokenMapper);
  }

  /** Facebook does not have refresh tokens, so refresh flow can not be implemented. */
  @Override
  protected AuthTokenDto refreshTokenFlow(final AuthTokenDto authTokenDto) {
    return authTokenDto;
  }
}
