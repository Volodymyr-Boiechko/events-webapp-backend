package com.boiechko.eventswebapp.criteria.stringcriteria;

import com.boiechko.eventswebapp.criteria.Criteria;

public class StringNotEmptyCriteria implements Criteria<String> {

  @Override
  public boolean criteriaMet(final String s) {
    return !s.isEmpty();
  }
}
