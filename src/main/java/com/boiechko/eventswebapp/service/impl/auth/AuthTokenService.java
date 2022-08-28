package com.boiechko.eventswebapp.service.impl.auth;

import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.enums.DestinationType;
import java.util.List;
import lombok.NonNull;

public interface AuthTokenService {

  void saveAuthToken(@NonNull AuthTokenDto authTokenDto);

  AuthTokenDto updateAuthToken(@NonNull AuthTokenDto authTokenDto);

  AuthTokenDto refreshToken(final AuthTokenDto authTokenDto);

  AuthTokenDto authTokenForUser(final Long userId);

  boolean authTokenExpired(AuthTokenDto authToken);

  boolean canBeRefreshed(AuthTokenDto authToken);

  AuthTokenDto findByOwnerUserIdAndDestinationType(Long userId, DestinationType destinationType);

  AuthTokenDto authTokenForUserAndDestination(
      final Long userId, final DestinationType destinationType);

  void deleteById(final Long id);

  void deleteAllByIds(final List<Long> authTokensIds);
}
