package com.baseproject.util;

public class DateParser {

  public static String parseLocalDateToString(java.time.LocalDate date) {
    if (date == null) {
      return null;
    }

    return date.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }
}
