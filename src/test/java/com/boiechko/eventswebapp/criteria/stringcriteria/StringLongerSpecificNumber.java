package com.boiechko.eventswebapp.criteria.stringcriteria;

import com.boiechko.eventswebapp.criteria.Criteria;

public class StringLongerSpecificNumber implements Criteria<String> {

  private static final int MIN_LENGTH = 3;

  @Override
  public boolean criteriaMet(String s) {
    return s.length() > MIN_LENGTH;
  }
}
