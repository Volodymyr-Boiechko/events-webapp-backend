package com.boiechko.eventswebapp.criteria;

/**
 * Any class that acts as criteria validator.
 *
 * @param <T> an object that criteria is checked on
 */
public interface Criteria<T> {

  /**
   * Checks if criteria is met.
   *
   * @return true if criteria is met and false otherwise
   */
  boolean criteriaMet(final T t);
}
