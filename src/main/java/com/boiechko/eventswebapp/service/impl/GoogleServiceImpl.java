package com.boiechko.eventswebapp.service.impl;

import static com.boiechko.eventswebapp.config.AppConstants.REDIRECT_URL;
import static com.boiechko.eventswebapp.config.AppConstants.TEMPORARY_IDENTIFIER;
import static com.boiechko.eventswebapp.util.DateUtils.convertEpochMillis;
import static com.boiechko.eventswebapp.util.DateUtils.getCurrentDateTime;
import static com.boiechko.eventswebapp.util.DateUtils.getLocalDateFromValues;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.criteria.auth.IdentificationTokenExistsCriteria;
import com.boiechko.eventswebapp.dto.ApiAuthUrlDto;
import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.dto.IdentificationTokenDto;
import com.boiechko.eventswebapp.dto.JwtTokenDto;
import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.enums.DestinationType;
import com.boiechko.eventswebapp.enums.TokenType;
import com.boiechko.eventswebapp.exception.SystemApiException;
import com.boiechko.eventswebapp.service.AuthenticationService;
import com.boiechko.eventswebapp.service.GoogleService;
import com.boiechko.eventswebapp.service.QueryService;
import com.boiechko.eventswebapp.service.UserService;
import com.boiechko.eventswebapp.service.impl.auth.AuthTokenService;
import com.boiechko.eventswebapp.util.Assert;
import com.boiechko.eventswebapp.util.GeneralUtils;
import com.boiechko.eventswebapp.util.HttpUtils;
import com.boiechko.eventswebapp.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
public class GoogleServiceImpl implements GoogleService {

  private final QueryService queryService;
  private final AuthTokenService authTokenService;
  private final UserService userService;
  private final AuthenticationService authenticationService;
  private final IdentificationTokenExistsCriteria identificationTokenExistsCriteria;
  private final HttpSession httpSession;
  private final HttpServletRequest httpServletRequest;
  @Value("${google.clientId}")
  private String clientId;
  @Value("${google.clientSecret}")
  private String clientSecret;
  @Value("${google.scope}")
  private String scope;
  @Value("${google.api-url}")
  private String apiUrl;
  @Value("${google.redirect-url}")
  private String redirectUrl;
  @Value("${google.refresh-token-expires-in-days}")
  private Long refreshTokenExpiresInDays;

  public GoogleServiceImpl(
      final QueryService queryService,
      @Qualifier("googleAuthTokenService") final AuthTokenService authTokenService,
      final UserService userService,
      final AuthenticationService authenticationService,
      final IdentificationTokenExistsCriteria identificationTokenExistsCriteria,
      final HttpSession httpSession,
      final HttpServletRequest httpServletRequest) {
    this.queryService = queryService;
    this.authTokenService = authTokenService;
    this.userService = userService;
    this.authenticationService = authenticationService;
    this.identificationTokenExistsCriteria = identificationTokenExistsCriteria;
    this.httpSession = httpSession;
    this.httpServletRequest = httpServletRequest;
  }

  @Override
  public ApiAuthUrlDto createGoogleAuthorization(final String redirectUrl) {

    final GoogleConnectionFactory connectionFactory =
        new GoogleConnectionFactory(clientId, clientSecret);
    final OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
    final OAuth2Parameters params = new OAuth2Parameters();
    final String identifierToken = GeneralUtils.generateUuid();

    httpSession.setAttribute("identificationToken", new IdentificationTokenDto(identifierToken));

    params.setRedirectUri(this.redirectUrl);
    params.setScope(scope);
    params.set("access_type", "offline");
    params.set("prompt", "select_account consent");

    final Map<String, String> queryParams =
        new HashMap<String, String>() {
          {
            put(TEMPORARY_IDENTIFIER, identifierToken);
            put(REDIRECT_URL, redirectUrl);
          }
        };
    params.setState(JacksonUtils.serialize(queryParams));

    return new ApiAuthUrlDto(oauthOperations.buildAuthenticateUrl(params));
  }

  @Override
  @Transactional
  public JwtTokenDto authenticateInSystem(final String code, final Map<String, String> stateMap) {
    try {
      final AuthTokenDto googleToken = createGoogleAccessToken(code, stateMap);
      final UserDto userDto = createOrGetAlreadySavedUser(getUserInfo(googleToken));
      final JwtTokenDto jwtTokenDto = authenticateUserInSystem(userDto);
      googleToken.setUser(userDto);
      authTokenService.saveAuthToken(googleToken);
      return jwtTokenDto;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new SystemApiException("Couldn't login via Google.", HttpStatus.INTERNAL_SERVER_ERROR);
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
  public AuthTokenDto createGoogleAccessToken(
      final String code, final Map<String, String> stateMap) {

    final String identificationToken = HttpUtils.fetchIdentificationToken(stateMap);

    if (identificationTokenExistsCriteria.criteriaMet(identificationToken)) {

      final GoogleConnectionFactory connectionFactory =
          new GoogleConnectionFactory(clientId, clientSecret);
      final AccessGrant accessGrant =
          connectionFactory.getOAuthOperations().exchangeForAccess(code, redirectUrl, null);
      return mapResponseToAuthTokenDto(accessGrant);
    } else {
      throw new SystemApiException("Cannot process identifier token", HttpStatus.BAD_REQUEST);
    }
  }

  private AuthTokenDto mapResponseToAuthTokenDto(final AccessGrant accessGrant) {
    final AuthTokenDto dto = new AuthTokenDto();
    final LocalDateTime now = getCurrentDateTime();
    dto.setAccessToken(accessGrant.getAccessToken());
    dto.setIssued(now);
    dto.setExpires(convertEpochMillis(accessGrant.getExpireTime()));
    dto.setRefreshToken(accessGrant.getRefreshToken());
    dto.setRefreshTokenExpiresIn(now.plusDays(refreshTokenExpiresInDays));
    dto.setTokenType(TokenType.BEARER.name());
    dto.setDestinationType(DestinationType.GOOGLE);
    return dto;
  }

  @Override
  public UserDto getUserInfo(final AuthTokenDto authTokenDto) {

    final Map<String, String> requestParams =
        new HashMap<String, String>() {
          {
            put("personFields", "addresses,birthdays,emailAddresses,genders,names,phoneNumbers");
            put("sources", "READ_SOURCE_TYPE_PROFILE");
          }
        };

    final Map<String, String> headers =
        new HashMap<String, String>() {
          {
            put(HttpHeaders.AUTHORIZATION, "Bearer " + authTokenDto.getAccessToken());
          }
        };

    try {
      final ResponseEntity<String> userResponse =
          queryService.makeGetRequest(apiUrl + "/people/me", requestParams, headers);
      return mapResponseToDto(JacksonUtils.OBJECT_MAPPER.readTree(userResponse.getBody()));
    } catch (HttpClientErrorException e) {
      throw new SystemApiException(e.getResponseBodyAsString(), e.getStatusCode());
    } catch (JsonProcessingException e) {
      throw new SystemApiException(
          "Failed to process Google user response", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private UserDto mapResponseToDto(final JsonNode jsonNode) {

    final UserDto userDto = new UserDto();

    Assert.ifTrueThen(
        jsonNode.has("names"),
        () -> {
          final JsonNode nameNode = jsonNode.get("names").get(0);
          final String firstName = nameNode.get("givenName").asText();
          final String lastName = nameNode.get("familyName").asText();
          Assert.ifTrueThen(
              StringUtils.isNotBlank(firstName), () -> userDto.setFirstName(firstName));
          Assert.ifTrueThen(StringUtils.isNotBlank(lastName), () -> userDto.setLastName(lastName));
        });

    Assert.ifTrueThen(
        jsonNode.has("birthdays"),
        () -> {
          final JsonNode birthdayNode = jsonNode.get("birthdays").get(0).get("date");
          userDto.setBirthDate(
              getLocalDateFromValues(
                  birthdayNode.get("year").asInt(),
                  birthdayNode.get("month").asInt(),
                  birthdayNode.get("day").asInt()));
        });

    Assert.ifTrueThen(
        jsonNode.has("emailAddresses"),
        () -> {
          final JsonNode emailNode = jsonNode.get("emailAddresses").get(0);
          final String email = emailNode.get("value").asText();
          userDto.setEmail(email);
          userDto.setUserName(email);
        });

    return userDto;
  }
}
