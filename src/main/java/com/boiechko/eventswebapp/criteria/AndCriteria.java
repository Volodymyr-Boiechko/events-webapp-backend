package com.boiechko.eventswebapp.criteria;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Checks whether the whole list of {@link Criteria} is satisfied on T parameter.
 *
 * @param <T> object that criteria is checked on
 */
@AllArgsConstructor
@NoArgsConstructor
public class AndCriteria<T> implements Criteria<T> {

  private List<Criteria<T>> criteria = new ArrayList<>();

  public AndCriteria<T> of(final Criteria<T> criteria) {
    this.criteria.clear();
    this.criteria.add(criteria);
    return this;
  }

  public AndCriteria<T> and(final Criteria<T> criteria) {
    this.criteria.add(criteria);
    return this;
  }

  @Override
  public boolean criteriaMet(T t) {
    return criteria.stream().allMatch(c -> c.criteriaMet(t));
  }
}
