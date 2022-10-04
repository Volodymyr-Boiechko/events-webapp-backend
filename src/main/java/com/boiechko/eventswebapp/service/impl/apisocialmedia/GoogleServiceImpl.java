package com.boiechko.eventswebapp.service.impl.apisocialmedia;

import static com.boiechko.eventswebapp.util.DateUtils.getCurrentDateTime;
import static com.boiechko.eventswebapp.util.DateUtils.getLocalDateFromValues;
import static com.boiechko.eventswebapp.util.HttpUtils.getAuthorizationHeader;

import com.boiechko.eventswebapp.criteria.auth.IdentificationTokenExistsCriteria;
import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.enums.DestinationType;
import com.boiechko.eventswebapp.enums.TokenType;
import com.boiechko.eventswebapp.exception.SystemApiException;
import com.boiechko.eventswebapp.service.AuthenticationService;
import com.boiechko.eventswebapp.service.QueryService;
import com.boiechko.eventswebapp.service.UserService;
import com.boiechko.eventswebapp.service.impl.auth.AuthTokenService;
import com.boiechko.eventswebapp.util.Assert;
import com.boiechko.eventswebapp.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
public class GoogleServiceImpl extends AbstractSocialMediaService {

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

  protected GoogleServiceImpl(
      final IdentificationTokenExistsCriteria identificationTokenExistsCriteria,
      final QueryService queryService,
      @Qualifier("googleAuthTokenService") final AuthTokenService authTokenService,
      final HttpSession httpSession,
      final HttpServletRequest httpServletRequest,
      final UserService userService,
      final AuthenticationService authenticationService) {
    super(
        DestinationType.GOOGLE,
        identificationTokenExistsCriteria,
        queryService,
        authTokenService,
        httpSession,
        httpServletRequest,
        authenticationService,
        userService);
  }

  @Override
  protected OAuth2ConnectionFactory<?> getOauth2ConnectionFactory() {
    return new GoogleConnectionFactory(clientId, clientSecret);
  }

  @Override
  protected OAuth2Parameters getOauth2Params() {
    final OAuth2Parameters params = new OAuth2Parameters();
    params.setRedirectUri(this.redirectUrl);
    params.setScope(scope);
    params.set("access_type", "offline");
    params.set("prompt", "select_account consent");
    return params;
  }

  @Override
  protected String getApiRedirectUrl() {
    return this.redirectUrl;
  }

  @Override
  protected AuthTokenDto mapResponseToAuthTokenDto(final AccessGrant accessGrant) {
    final AuthTokenDto dto = new AuthTokenDto(accessGrant);
    final LocalDateTime now = getCurrentDateTime();
    dto.setIssued(now);
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

    final Map<String, String> headers = getAuthorizationHeader(authTokenDto.getAccessToken());

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
