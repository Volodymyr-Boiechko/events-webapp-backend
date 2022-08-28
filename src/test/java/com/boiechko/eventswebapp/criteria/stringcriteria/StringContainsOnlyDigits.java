package com.boiechko.eventswebapp.criteria.stringcriteria;

import com.boiechko.eventswebapp.criteria.Criteria;
import org.springframework.stereotype.Component;

@Component
public class StringContainsOnlyDigits implements Criteria<String> {

  private static final String ONLY_DIGITS_REGEX = "[0-9]+";

  @Override
  public boolean criteriaMet(String s) {
    return s.matches(ONLY_DIGITS_REGEX);
  }
}
