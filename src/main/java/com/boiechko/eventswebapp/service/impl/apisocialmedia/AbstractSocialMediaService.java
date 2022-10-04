package com.boiechko.eventswebapp.service.impl.apisocialmedia;

import static com.boiechko.eventswebapp.config.AppConstants.REDIRECT_URL;
import static com.boiechko.eventswebapp.config.AppConstants.TEMPORARY_IDENTIFIER;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.criteria.auth.IdentificationTokenExistsCriteria;
import com.boiechko.eventswebapp.dto.ApiAuthUrlDto;
import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.dto.IdentificationTokenDto;
import com.boiechko.eventswebapp.dto.JwtTokenDto;
import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.enums.DestinationType;
import com.boiechko.eventswebapp.exception.SystemApiException;
import com.boiechko.eventswebapp.service.AuthenticationService;
import com.boiechko.eventswebapp.service.DestinationTypeService;
import com.boiechko.eventswebapp.service.QueryService;
import com.boiechko.eventswebapp.service.UserService;
import com.boiechko.eventswebapp.service.impl.auth.AuthTokenService;
import com.boiechko.eventswebapp.util.GeneralUtils;
import com.boiechko.eventswebapp.util.HttpUtils;
import com.boiechko.eventswebapp.util.JacksonUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;

@Slf4j
public abstract class AbstractSocialMediaService
    implements SocialMediaService, DestinationTypeService {

  protected final DestinationType targetDestination;
  protected final IdentificationTokenExistsCriteria identificationTokenExistsCriteria;
  protected final QueryService queryService;
  protected final AuthTokenService authTokenService;
  protected final HttpSession httpSession;
  protected final HttpServletRequest httpServletRequest;
  protected final AuthenticationService authenticationService;
  protected final UserService userService;

  protected AbstractSocialMediaService(
      final DestinationType targetDestination,
      final IdentificationTokenExistsCriteria identificationTokenExistsCriteria,
      final QueryService queryService,
      final AuthTokenService authTokenService,
      final HttpSession httpSession,
      final HttpServletRequest httpServletRequest,
      final AuthenticationService authenticationService,
      final UserService userService) {
    this.targetDestination = targetDestination;
    this.identificationTokenExistsCriteria = identificationTokenExistsCriteria;
    this.queryService = queryService;
    this.authTokenService = authTokenService;
    this.httpSession = httpSession;
    this.httpServletRequest = httpServletRequest;
    this.authenticationService = authenticationService;
    this.userService = userService;
  }

  @Override
  public ApiAuthUrlDto createAuthorization(final String redirectUrl) {

    final OAuth2ConnectionFactory<?> connectionFactory = getOauth2ConnectionFactory();
    final OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
    final OAuth2Parameters params = getOauth2Params();
    final String identificationToken = GeneralUtils.generateUuid();

    httpSession.setAttribute(
        "identificationToken", new IdentificationTokenDto(identificationToken));

    final Map<String, String> queryParams =
        new HashMap<String, String>() {
          {
            put(TEMPORARY_IDENTIFIER, identificationToken);
            put(REDIRECT_URL, redirectUrl);
          }
        };

    params.setState(JacksonUtils.serialize(queryParams));

    return new ApiAuthUrlDto(oauthOperations.buildAuthenticateUrl(params));
  }

  protected abstract OAuth2ConnectionFactory<?> getOauth2ConnectionFactory();

  protected abstract OAuth2Parameters getOauth2Params();

  @Override
  public AuthTokenDto createAccessToken(final String code, final Map<String, String> stateMap) {

    final String identificationToken = HttpUtils.fetchIdentificationToken(stateMap);

    if (identificationTokenExistsCriteria.criteriaMet(identificationToken)) {

      final OAuth2ConnectionFactory<?> connectionFactory = getOauth2ConnectionFactory();

      final AccessGrant accessGrant =
          connectionFactory.getOAuthOperations().exchangeForAccess(code, getApiRedirectUrl(), null);

      return mapResponseToAuthTokenDto(accessGrant);
    } else {
      throw new SystemApiException("Cannot process identifier token", HttpStatus.BAD_REQUEST);
    }
  }

  protected abstract String getApiRedirectUrl();

  protected abstract AuthTokenDto mapResponseToAuthTokenDto(final AccessGrant accessGrant);

  @Override
  @Transactional
  public JwtTokenDto authenticateInSystem(final String code, final Map<String, String> stateMap) {
    try {
      final AuthTokenDto authToken = createAccessToken(code, stateMap);
      final UserDto userDto = createOrGetAlreadySavedUser(getUserInfo(authToken));
      final JwtTokenDto jwtTokenDto = authenticateUserInSystem(userDto);
      authToken.setUser(userDto);
      authTokenService.saveAuthToken(authToken);
      return jwtTokenDto;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new SystemApiException(
          String.format("Couldn't login via %s.", targetDestination), INTERNAL_SERVER_ERROR);
    }
  }

  private UserDto createOrGetAlreadySavedUser(final UserDto mappedUserDto) {
    final Optional<UserDto> user =
        Optional.ofNullable(userService.getUser(mappedUserDto.getUserName()));
    return user.orElseGet(() -> userService.saveUser(mappedUserDto));
  }

  private JwtTokenDto authenticateUserInSystem(final UserDto userDto) {
    final Authentication authenticate =
        authenticationService.authenticateWithoutPassword(userDto, httpServletRequest);
    authenticationService.saveInSecurityContext(authenticate);
    return authenticationService.generateToken((UserPrincipal) authenticate.getPrincipal());
  }

  @Override
  public DestinationType getType() {
    return targetDestination;
  }
}
