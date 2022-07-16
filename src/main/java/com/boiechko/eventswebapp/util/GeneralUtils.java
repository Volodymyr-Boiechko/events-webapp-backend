package com.boiechko.eventswebapp.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralUtils {

  private static final int PUBLIC_ID_LENGTH = 8;

  public static String generatePublicId() {
    return RandomStringUtils.random(PUBLIC_ID_LENGTH, true, true);
  }

}