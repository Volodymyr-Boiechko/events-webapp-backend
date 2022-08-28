package com.boiechko.eventswebapp.service.impl.auth;

import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.entity.AuthTokenEntity;
import com.boiechko.eventswebapp.enums.DestinationType;
import com.boiechko.eventswebapp.exception.AuthTokenNotFoundException;
import com.boiechko.eventswebapp.exception.BadDestinationTypeException;
import com.boiechko.eventswebapp.exception.CriteriaNotMetException;
import com.boiechko.eventswebapp.exception.InvalidRefreshTokenException;
import com.boiechko.eventswebapp.exception.NotFoundException;
import com.boiechko.eventswebapp.exception.SystemApiException;
import com.boiechko.eventswebapp.mapper.AuthTokenMapper;
import com.boiechko.eventswebapp.repository.AuthTokenRepository;
import com.boiechko.eventswebapp.service.DestinationTypeService;
import com.boiechko.eventswebapp.util.Assert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
public abstract class AbstractAuthTokenService implements AuthTokenService, DestinationTypeService {

  protected final DestinationType targetDestination;
  protected final AuthTokenRepository authTokenRepository;
  protected final AuthTokenMapper authTokenMapper;
  protected final SecurityUtil securityUtil;

  public AbstractAuthTokenService(
      final DestinationType targetDestination,
      final AuthTokenRepository authTokenRepository,
      final AuthTokenMapper authTokenMapper,
      final SecurityUtil securityUtil) {
    this.targetDestination = targetDestination;
    this.authTokenRepository = authTokenRepository;
    this.authTokenMapper = authTokenMapper;
    this.securityUtil = securityUtil;
  }

  @Override
  public DestinationType getType() {
    return targetDestination;
  }

  @Override
  public void saveAuthToken(@NonNull final AuthTokenDto authTokenDto) {

    Assert.isTrue(
        Objects.isNull(authTokenDto.getDestinationType()),
        () -> new CriteriaNotMetException("Auth token destination type should not be empty"));

    Assert.isTrue(
        Objects.isNull(authTokenDto.getUser()) || Objects.isNull(authTokenDto.getUser().getId()),
        () -> new CriteriaNotMetException("Auth token should contain user"));

    AuthTokenEntity authTokenEntity;

    try {
      authTokenEntity =
          authTokenRepository
              .findByOwnerAccountIdAndDestinationType(
                  authTokenDto.getUser().getId(), targetDestination)
              .orElseThrow(NotFoundException::new);
      authTokenMapper.updateAuthTokenEntity(authTokenEntity, authTokenDto);
    } catch (Exception e) {
      authTokenEntity = authTokenMapper.toEntity(authTokenDto);
    }
    authTokenRepository.save(authTokenEntity);
  }

  @Override
  public AuthTokenDto updateAuthToken(@NonNull final AuthTokenDto authTokenDto) {

    Assert.isTrue(
        Objects.isNull(authTokenDto.getId()),
        () -> new CriteriaNotMetException("Auth token id should not be empty"));

    final AuthTokenEntity entity =
        authTokenRepository
            .findById(authTokenDto.getId())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "There is not auth token for id " + authTokenDto.getId()));

    authTokenMapper.updateAuthTokenEntity(entity, authTokenDto);
    authTokenRepository.save(entity);
    return authTokenMapper.toDto(entity);
  }

  @Override
  public AuthTokenDto refreshToken(final AuthTokenDto authTokenDto) {
    verifyAuthTokenTypeMatches(authTokenDto);

    Assert.isTrue(
        StringUtils.isEmpty(authTokenDto.getRefreshToken()),
        () -> new CriteriaNotMetException("Refresh token must not be empty"));

    final AuthTokenDto refreshedToken = refreshTokenFlow(authTokenDto);
    return updateAuthToken(refreshedToken);
  }

  private void verifyAuthTokenTypeMatches(final AuthTokenDto dto) {
    Assert.isTrue(
        Objects.equals(targetDestination, dto.getDestinationType()),
        BadDestinationTypeException::new);
  }

  protected abstract AuthTokenDto refreshTokenFlow(final AuthTokenDto authTokenDto);

  /**
   * Find auth token for given account id. In case token expired - refresh it and return alive
   * token.
   *
   * @return {@link AuthTokenDto}
   * @throws AuthTokenNotFoundException in case neither alive nor expired {@link AuthTokenEntity}
   *     was found for given account id.
   */
  @Override
  public AuthTokenDto authTokenForUser(final Long accountId) {
    AuthTokenDto userAuthToken =
        Optional.of(findByOwnerUserIdAndDestinationType(accountId, targetDestination))
            .orElseThrow(
                () ->
                    new AuthTokenNotFoundException(
                        String.format(
                            "No %s auth token found for account with id %s",
                            targetDestination, accountId)));

    if (authTokenExpired(userAuthToken) && canBeRefreshed(userAuthToken)) {
      userAuthToken = refreshToken(userAuthToken);
    } else if (authTokenExpired(userAuthToken) && !canBeRefreshed(userAuthToken)) {
      throw new InvalidRefreshTokenException(
          "Unable to refresh expired auth token, you'd better obtain new auth token for "
              + userAuthToken.getDestinationType(),
          HttpStatus.BAD_REQUEST);
    }

    return userAuthToken;
  }

  /**
   * Check if token expired, if property expires is null then token consider to be immortal (will
   * never expire).
   *
   * @return true if token expired. False otherwise
   */
  @Override
  public boolean authTokenExpired(AuthTokenDto authToken) {
    boolean tokenExpired = false;

    if (Objects.nonNull(authToken.getExpires())) {
      tokenExpired = LocalDateTime.now().isAfter(authToken.getExpires());
    }

    return tokenExpired;
  }

  @Override
  public boolean canBeRefreshed(final AuthTokenDto authToken) {
    return Objects.nonNull(authToken.getRefreshToken())
        && (Objects.isNull(authToken.getRefreshTokenExpiresIn())
            || authToken.getRefreshTokenExpiresIn().isAfter(LocalDateTime.now()));
  }

  @Override
  public void deleteById(final Long id) {
    authTokenRepository.deleteById(id);
  }

  @Override
  public void deleteAllByIds(final List<Long> authTokensIds) {
    if (!authTokensIds.isEmpty()) {
      authTokenRepository.deleteAllByIds(authTokensIds);
    }
  }

  @Override
  public AuthTokenDto authTokenForUserAndDestination(
      final Long userId, DestinationType destinationType) {
    if (targetDestination.equals(destinationType)) {
      return authTokenForUser(userId);
    } else {
      throw new BadDestinationTypeException();
    }
  }

  @Override
  public AuthTokenDto findByOwnerUserIdAndDestinationType(
      final Long userId, final DestinationType destinationType) {
    return authTokenMapper.toDto(
        authTokenRepository
            .findByOwnerAccountIdAndDestinationType(userId, destinationType)
            .orElseThrow(
                () ->
                    new AuthTokenNotFoundException(
                        String.format(
                            "There is no auth token for user with id %s and destination type %s",
                            userId, destinationType))));
  }

  protected RuntimeException basedOnStatusException(HttpClientErrorException e) {
    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
      return new InvalidRefreshTokenException(
          String.format(
              "An attempt to refresh access token with invalid refresh token "
                  + "in %s auth token service",
              targetDestination),
          HttpStatus.BAD_REQUEST);
    } else {
      return new SystemApiException(
          String.format("Bad response from %s Api. %s", targetDestination, e.getMessage()),
          HttpStatus.BAD_REQUEST);
    }
  }
}
