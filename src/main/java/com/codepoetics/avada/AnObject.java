package com.codepoetics.avada;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.*;

public final class AnObject<T> extends TypeSafeDiagnosingMatcher<T> {

  private static <T> ObjectProperty<T, Class<T>> type(Class<T> klass) {
    return ObjectProperty.of("type", new Getter<T, Class<T>>() {
      @Override
      public Class<T> get(T target) {
        return (Class<T>) target.getClass();
      }
    });
  }

  @SafeVarargs
  public static <T> AnObject<T> having(Expectation<T>... expectations) {
    return new AnObject<>(toMap(expectations));
  }

  public static <T> AnObject<T> ofExactType(Class<T> expectedType) {
    return having(type(expectedType).of(expectedType));
  }

  public static <T> AnObject<T> assignableTo(Class<T> expectedType) {
    return having(type(expectedType).matching(IsAssignable.to(expectedType)));
  }

  private static <T> Map<String, Expectation<T>> toMap(Expectation<T>[] expectations) {
    Map<String, Expectation<T>> map = new LinkedHashMap<>();
    for (Expectation<T> expectation : expectations) {
      map.put(expectation.getPropertyName(), expectation);
    }
    return map;
  }

  private final Map<String, Expectation<T>> expectations;

  private AnObject(Map<String, Expectation<T>> expectations) {
    this.expectations = expectations;
  }

  public <V> AnObject<T> with(String name, Getter<T, V> getter, V expected) {
    return with(ObjectProperty.of(name, getter).of(expected));
  }

  public <V> AnObject<T> with(String name, Getter<T, V> getter, Matcher<? super V> matcher) {
    return with(ObjectProperty.of(name, getter).matching(matcher));
  }

  @SafeVarargs
  public final AnObject<T> with(Expectation<T>... expectations) {
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
      matched = expectation.test(item, mismatch) && matched;
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
