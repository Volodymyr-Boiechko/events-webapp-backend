package com.boiechko.eventswebapp.util;

import java.util.Objects;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class GeneralUtils {

  private static final int PUBLIC_ID_LENGTH = 8;

  public static String generatePublicId() {
    return RandomStringUtils.random(PUBLIC_ID_LENGTH, true, true);
  }

  public static String compactString(String val) {
    if (Objects.isNull(val)) {
      return null;
    }
    return val.replaceAll("[\\n\\t\\r ]", "");
  }

  public static String generateUuid() {
    return UUID.randomUUID().toString();
  }
}
