package com.codepoetics.avada;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;

public final class ABean<T> extends TypeSafeDiagnosingMatcher<T> {

  public static <T> ABean<T> ofClass(Class<T> expectedClass) {
    return new ABean<>(expectedClass);
  }

  private final Class<T> expectedClass;
  private final List<Expectation<T>> expectations = new ArrayList<>();

  private ABean(Class<T> expectedClass) {
    this.expectedClass = expectedClass;
  }

  public <V> ABean<T> with(String name, Getter<T, V> getter, V expected) {
    return with(BeanProperty.of(name, getter).of(expected));
  }

  public <V> ABean<T> with(String name, Getter<T, V> getter, Matcher<? super V> matcher) {
    return with(BeanProperty.of(name, getter).that(matcher));
  }

  public ABean<T> with(Expectation<T> expectation) {
    expectations.add(expectation);
    return this;
  }

  @Override
  protected boolean matchesSafely(T item, Description mismatch) {
    boolean matched = true;
    for (Expectation<T> expectation : expectations) {
      matched = matched && expectation.test(item, mismatch);
    }
    return matched;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A bean of class ").appendValue(expectedClass);
    if (expectations.size() == 0) {
      return;
    }
    description.appendText(" with:");
    Indentation.indent();
    for (Expectation<T> expectation : expectations) {
      expectation.describe(description);
    }
    Indentation.outdent();
  }
}
