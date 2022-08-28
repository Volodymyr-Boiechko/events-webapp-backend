package com.boiechko.eventswebapp.util;

import java.util.Map;
import java.util.Objects;
import lombok.experimental.UtilityClass;

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
}
