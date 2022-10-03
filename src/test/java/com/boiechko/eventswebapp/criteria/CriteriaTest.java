package com.boiechko.eventswebapp.criteria;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.boiechko.eventswebapp.criteria.stringcriteria.StringContainsOnlyDigits;
import com.boiechko.eventswebapp.criteria.stringcriteria.StringLongerSpecificNumber;
import com.boiechko.eventswebapp.criteria.stringcriteria.StringNotEmptyCriteria;
import com.boiechko.eventswebapp.criteria.stringcriteria.StringNotEmptyStrictCriteria;
import com.boiechko.eventswebapp.exception.SystemApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CriteriaTest {

  @Spy private StringContainsOnlyDigits stringContainsOnlyDigitsCriteria;
  @Spy private StringNotEmptyCriteria stringNotEmptyCriteria;
  @Spy private StringLongerSpecificNumber stringLongerSpecificNumberCriteria;
  @Spy private StringNotEmptyStrictCriteria stringNotEmptyStrictCriteria;

  @Test
  void shouldMetCriteria() {
    assertTrue(stringNotEmptyCriteria.criteriaMet("test string"));
    assertFalse(stringNotEmptyCriteria.criteriaMet(""));

    assertTrue(stringContainsOnlyDigitsCriteria.criteriaMet("123456789"));
    assertFalse(stringContainsOnlyDigitsCriteria.criteriaMet("test123512"));
  }

  @Test
  void shouldMetAndCriteria() {

    final AndCriteria<String> andCriteria =
        new AndCriteria<String>()
            .of(stringNotEmptyCriteria)
            .and(stringContainsOnlyDigitsCriteria)
            .and(stringLongerSpecificNumberCriteria);

    assertTrue(andCriteria.criteriaMet("1234"));
    assertFalse(andCriteria.criteriaMet("123"));
    assertFalse(andCriteria.criteriaMet("test123"));
    assertFalse(andCriteria.criteriaMet(""));
  }

  @Test
  void shouldMetOrCriteria() {

    final OrCriteria<String> orCriteria =
        new OrCriteria<String>().of(stringNotEmptyCriteria).or(stringContainsOnlyDigitsCriteria);

    assertTrue(orCriteria.criteriaMet("1234"));
    assertTrue(orCriteria.criteriaMet("123"));
    assertTrue(orCriteria.criteriaMet("test123"));
    assertTrue(orCriteria.criteriaMet("a"));
    assertFalse(orCriteria.criteriaMet(""));
  }

  @Test
  void shouldMetStrictCriteria() {
    assertDoesNotThrow(() -> stringNotEmptyStrictCriteria.criteriaMet("test message"));

    assertThrows(SystemApiException.class, () -> stringNotEmptyStrictCriteria.criteriaMet(""));
  }
}
