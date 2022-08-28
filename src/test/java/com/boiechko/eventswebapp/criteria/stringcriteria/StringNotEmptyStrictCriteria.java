package com.boiechko.eventswebapp.criteria.stringcriteria;

import com.boiechko.eventswebapp.criteria.StrictCriteria;

public class StringNotEmptyStrictCriteria extends StrictCriteria<String> {

  @Override
  protected boolean strictCriteriaMet(String s) {
    return !s.isEmpty();
  }
}
