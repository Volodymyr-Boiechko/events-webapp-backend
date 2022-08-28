package com.boiechko.eventswebapp.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

  public static long getCurrentEpoch() {
    return Instant.now().toEpochMilli();
  }

  public static LocalDateTime getCurrentDateTime() {
    return LocalDateTime.now();
  }

  public static LocalDateTime convertEpochMillis(final Long millis) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
  }

  public static LocalDateTime convertDate(final Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static LocalDate getLocalDateFromValues(final int year, final int month, final int day) {
    return LocalDate.of(year, month, day);
  }
}
