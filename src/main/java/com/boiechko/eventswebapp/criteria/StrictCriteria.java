package com.boiechko.eventswebapp.criteria;

import com.boiechko.eventswebapp.exception.CriteriaNotMetException;
import com.boiechko.eventswebapp.exception.SystemApiException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

/**
 * Correct implementation of criteria of this type should either return true if criteria is met or
 * throw an {@link CriteriaNotMetException} with well detailed description why it isn't.
 */
public abstract class StrictCriteria<T> implements Criteria<T> {

  /**
   * Check if criteria is met on type T.
   *
   * @return true if criteria is met
   * @throws CriteriaNotMetException with detailed description if criteria is not met
   * @throws SystemApiException      if {@link StrictCriteria} is implemented in wrong way
   */
  public boolean criteriaMet(@NotNull T t) {
    boolean criteriaMet;

    criteriaMet = strictCriteriaMet(t);

    if (!criteriaMet) {
      throw new SystemApiException(
          String.format("It seems %s strict criteria has been implemented wrong, "
                  + "because criteria should be met and you shouldn't see this message",
              this.getClass().getSimpleName()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  protected abstract boolean strictCriteriaMet(final T t);
}
