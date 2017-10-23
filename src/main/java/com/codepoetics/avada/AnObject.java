package com.codepoetics.avada;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.*;

public final class AnObject<T> extends TypeSafeDiagnosingMatcher<T> {

  @SafeVarargs
  public static <T> AnObject<T> with(Expectation<T>... expectations) {
    return new AnObject<>(toMap(expectations));
  }

  public static <T, V> AnObject<T> with(String name, Getter<T, V> getter, V expected) {
    return with(ObjectProperty.of(name, getter).of(expected));
  }

  public static <T, V> AnObject<T> with(String name, Getter<T, V> getter, Matcher<? super V> matcher) {
    return with(ObjectProperty.of(name, getter).matching(matcher));
  }

  private static <T> Map<String, Expectation<T>> toMap(Expectation<T>[] expectations) {
    Map<String, Expectation<T>> map = new LinkedHashMap<>();
    for (Expectation<T> expectation : expectations) {
      map.put(expectation.getPropertyName(), expectation);
    }
    return map;
  }

  private final Map<String, Expectation<T>> expectations;;

  private AnObject(Map<String, Expectation<T>> expectations) {
    this.expectations = expectations;
  }

  public <V> AnObject<T> and(String name, Getter<T, V> getter, V expected) {
    return with(ObjectProperty.of(name, getter).of(expected));
  }

  public <V> AnObject<T> and(String name, Getter<T, V> getter, Matcher<? super V> matcher) {
    return with(ObjectProperty.of(name, getter).matching(matcher));
  }

  @SafeVarargs
  public final AnObject<T> and(Expectation<T>... expectations) {
    Map<String, Expectation<T>> newExpectations = new LinkedHashMap<>();
    newExpectations.putAll(this.expectations);
    newExpectations.putAll(toMap(expectations));
    return new AnObject<>(newExpectations);
  }

  @Override
  protected boolean matchesSafely(T item, Description mismatch) {
    boolean matched = true;
    Indentation.indent();
    for (Expectation<T> expectation : expectations.values()) {
      matched = matched && expectation.test(item, mismatch);
    }
    Indentation.outdent();
    return matched;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An object");
    if (expectations.size() == 0) {
      return;
    }
    description.appendText(" with:");
    Indentation.indent();
    for (Expectation<T> expectation : expectations.values()) {
      expectation.describe(description);
    }
    Indentation.outdent();
  }
}
