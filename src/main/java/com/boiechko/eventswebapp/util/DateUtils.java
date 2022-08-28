package com.boiechko.eventswebapp.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

  public static LocalDateTime getCurrentDateTime() {
    return LocalDateTime.now();
  }

  public static LocalDateTime convertEpochMillis(final Long millis) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
  }
}
