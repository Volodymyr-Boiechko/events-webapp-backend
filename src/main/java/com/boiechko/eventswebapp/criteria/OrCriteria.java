package com.boiechko.eventswebapp.criteria;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * This type of criteria can not be used with {@link StrictCriteria} because of their inconsistency
 * in terms of conditions. {@link StrictCriteria} need that either whole expression should be
 * satisfied or rejected otherwise. And {@link OrCriteria} need at least one expression candidate to
 * be true to be satisfied.
 */
@AllArgsConstructor
@NoArgsConstructor
public class OrCriteria<T> implements Criteria<T> {

  private List<Criteria<T>> criteria = new ArrayList<>();

  public OrCriteria<T> of(final Criteria<T> criteria) {
    this.criteria.clear();
    this.criteria.add(criteria);
    return this;
  }

  public OrCriteria<T> or(final Criteria<T> criteria) {
    this.criteria.add(criteria);
    return this;
  }

  @Override
  public boolean criteriaMet(final T t) {
    return criteria.stream().anyMatch(c -> c.criteriaMet(t));
  }
}
