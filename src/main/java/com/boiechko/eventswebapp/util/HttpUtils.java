package com.boiechko.eventswebapp.util;

import static com.boiechko.eventswebapp.config.AppConstants.TEMPORARY_IDENTIFIER;
import static com.boiechko.eventswebapp.enums.TokenType.BEARER;

import com.boiechko.eventswebapp.exception.SystemApiException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@UtilityClass
public class HttpUtils {

  public static String buildQueryUrlWithoutEncoding(
      final String url, final Map<String, String> requestParams) {
    if (Objects.isNull(requestParams)) {
      return url;
    }
    final StringBuilder urlBuilder = new StringBuilder(url);
    requestParams.forEach((key, val) -> urlBuilder.append("&").append(key).append("=").append(val));
    return urlBuilder.toString().replaceFirst("&", "?");
  }

  public static String fetchIdentificationToken(final Map<String, String> stateMap) {
    final String identificationToken = stateMap.get(TEMPORARY_IDENTIFIER);
    if (StringUtils.isEmpty(identificationToken)) {
      throw new SystemApiException("Can not process temporary identifier", HttpStatus.BAD_REQUEST);
    }
    return identificationToken;
  }

  public static Map<String, String> getAuthorizationHeader(final String token) {
    return new HashMap<String, String>() {
      {
        put(HttpHeaders.AUTHORIZATION, getAuthorizationBearerToken(token));
      }
    };
  }

  public static String getAuthorizationBearerToken(final String token) {
    return BEARER.getValue() + " " + token;
  }
}
