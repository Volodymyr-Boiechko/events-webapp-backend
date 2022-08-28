package com.boiechko.eventswebapp.util;

import com.boiechko.eventswebapp.exception.SystemApiException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;

@UtilityClass
public class Assert {

  public static final Action NONE_ACTION = () -> {};

  public static void isTrue(final boolean condition, final Supplier<RuntimeException> ex) {
    if (condition) {
      throw ex.get();
    }
  }

  public static void isFalse(final boolean condition, final Supplier<RuntimeException> ex) {
    if (!condition) {
      throw ex.get();
    }
  }

  public static <T> void ifTrueThen(boolean condition, Action actionIfTrue) {
    ifTrueElse(condition, actionIfTrue, NONE_ACTION);
  }

  public static <T> void ifFalseThen(boolean condition, Action actionIfFalse) {
    ifTrueElse(condition, NONE_ACTION, actionIfFalse);
  }

  public static <T> void ifTrueElse(boolean condition, Action actionIfTrue, Action actionIfFalse) {
    if (condition) {
      actionIfTrue.act();
    } else {
      actionIfFalse.act();
    }
  }

  @SafeVarargs
  public static <E> boolean equalsAnyOf(E e, E... elements) {
    return Arrays.asList(elements).contains(e);
  }

  @SafeVarargs
  public static <E> boolean containsAnyOf(List<E> list, E... array) {
    return Stream.of(array).anyMatch(list::contains);
  }

  public static <E> void noNullElements(Collection<E> elements, Supplier<SystemApiException> ex) {
    if (elements.stream().anyMatch(Objects::isNull)) {
      throw ex.get();
    }
  }

  public static <T> boolean containsParticularNumberOfNeededArgs(
      @NonNull JoinPoint joinPoint, Class<T> neededArgsType, Predicate<Long> amountPredicate) {
    return amountPredicate.test(
        Stream.of(joinPoint.getArgs())
            .map(Object::getClass)
            .filter(neededArgsType::isAssignableFrom)
            .count());
  }

  @FunctionalInterface
  public interface Action {

    void act();
  }

  @FunctionalInterface
  public interface ReturnableAction<T> {

    T act();
  }
}
