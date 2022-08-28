package com.boiechko.eventswebapp.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class GeneralUtils {

  private static final int PUBLIC_ID_LENGTH = 8;

  public static String generatePublicId() {
    return RandomStringUtils.random(PUBLIC_ID_LENGTH, true, true);
  }

}