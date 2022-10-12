package com.boiechko.eventswebapp.service.impl.apisocialmedia;

import static com.boiechko.eventswebapp.enums.TokenType.BEARER;
import static com.boiechko.eventswebapp.util.Assert.ifTrueThen;
import static com.boiechko.eventswebapp.util.DateUtils.getCurrentDateTime;
import static com.boiechko.eventswebapp.util.DateUtils.parseDateFromString;
import static com.boiechko.eventswebapp.util.HttpUtils.getAuthorizationHeader;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.boiechko.eventswebapp.criteria.auth.IdentificationTokenExistsCriteria;
import com.boiechko.eventswebapp.dto.AddressDto;
import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.dto.LocationDto;
import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.enums.DestinationType;
import com.boiechko.eventswebapp.exception.SystemApiException;
import com.boiechko.eventswebapp.service.AuthenticationService;
import com.boiechko.eventswebapp.service.QueryService;
import com.boiechko.eventswebapp.service.UserService;
import com.boiechko.eventswebapp.service.impl.auth.AuthTokenService;
import com.boiechko.eventswebapp.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
public class FacebookServiceImpl extends AbstractSocialMediaService {

  private static final String STRING_DATE_TIME_BIRTH_PATTERN = "MM/dd/yyyy";

  @Value("${facebook.appId}")
  private String appId;

  @Value("${facebook.appSecret}")
  private String secret;

  @Value("${facebook.scope}")
  private String scope;

  @Value("${facebook.api-url}")
  private String apiUrl;

  @Value("${facebook.redirect-url}")
  private String redirectUrl;

  public FacebookServiceImpl(
      final IdentificationTokenExistsCriteria identificationTokenExistsCriteria,
      final QueryService queryService,
      @Qualifier("facebookAuthTokenService") final AuthTokenService authTokenService,
      final HttpSession httpSession,
      final HttpServletRequest httpServletRequest,
      final AuthenticationService authenticationService,
      final UserService userService) {
    super(
        DestinationType.FACEBOOK,
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
    return new FacebookConnectionFactory(appId, secret);
  }

  @Override
  protected OAuth2Parameters getOauth2Params() {
    final OAuth2Parameters params = new OAuth2Parameters();
    params.setRedirectUri(this.redirectUrl);
    params.setScope(scope);
    return params;
  }

  @Override
  protected String getApiRedirectUrl() {
    return this.redirectUrl;
  }

  @Override
  protected AuthTokenDto mapResponseToAuthTokenDto(final AccessGrant accessGrant) {
    final AuthTokenDto authTokenDto = new AuthTokenDto(accessGrant);
    authTokenDto.setIssued(getCurrentDateTime());
    authTokenDto.setTokenType(BEARER.name());
    authTokenDto.setDestinationType(DestinationType.FACEBOOK);
    return authTokenDto;
  }

  @Override
  public UserDto getUserInfo(final AuthTokenDto authTokenDto) {

    final Map<String, String> requestParams = new HashMap<>();
    requestParams.put("fields", "id,name,birthday,email,first_name,last_name,location");

    final Map<String, String> headers = getAuthorizationHeader(authTokenDto.getAccessToken());

    try {
      final ResponseEntity<String> userResponse =
          queryService.makeGetRequest(apiUrl + "/me", requestParams, headers);
      return mapResponseToDto(JacksonUtils.OBJECT_MAPPER.readTree(userResponse.getBody()));
    } catch (Exception e) {
      throw new SystemApiException(
          String.format("Failed to retrieve user's info from %s", targetDestination),
          INTERNAL_SERVER_ERROR);
    }
  }

  private UserDto mapResponseToDto(final JsonNode jsonNode) {

    final UserDto userDto = new UserDto();

    setStringIfNotBlankValueFromResponse(jsonNode, "first_name", userDto::setFirstName);
    setStringIfNotBlankValueFromResponse(jsonNode, "last_name", userDto::setLastName);
    setStringIfNotBlankValueFromResponse(jsonNode, "email", userDto::setEmail);
    setStringIfNotBlankValueFromResponse(jsonNode, "email", userDto::setUserName);
    setBirthDate(jsonNode, userDto);
    setAddressIfPresent(jsonNode, userDto);

    return userDto;
  }

  private void setStringIfNotBlankValueFromResponse(
      final JsonNode jsonNode,
      final String jsonValueName,
      final Consumer<String> consumerToSetStringValue) {
    ifTrueThen(
        jsonNode.has(jsonValueName),
        () -> {
          final String stringValue = jsonNode.get(jsonValueName).asText();
          ifTrueThen(
              StringUtils.isNotBlank(stringValue),
              () -> consumerToSetStringValue.accept(stringValue));
        });
  }

  private void setBirthDate(final JsonNode jsonNode, final UserDto userDto) {
    final String birthDateString = jsonNode.get("birthday").asText();
    final LocalDate birthDate =
        parseDateFromString(birthDateString, ofPattern(STRING_DATE_TIME_BIRTH_PATTERN));
    userDto.setBirthDate(birthDate);
  }

  private void setAddressIfPresent(final JsonNode jsonNode, final UserDto userDto) {
    final Optional<AddressDto> addressDto = getAddress(jsonNode);
    addressDto.ifPresent(userDto::setAddress);
  }

  private Optional<AddressDto> getAddress(final JsonNode jsonNode) {
    final boolean doesUserHasLocation =
        jsonNode.has("location") && jsonNode.get("location").has("name");

    if (doesUserHasLocation) {
      return Optional.of(new AddressDto(parseLocation(jsonNode)));
    } else {
      return Optional.empty();
    }
  }

  private LocationDto parseLocation(final JsonNode jsonNode) {
    final String locationName = jsonNode.get("location").get("name").asText();
    final String[] locations = locationName.split(",");
    return LocationDto.builder().country(locations[1].trim()).city(locations[0].trim()).build();
  }
}
