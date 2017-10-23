package com.codepoetics.avada;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public final class Expectation<T> {

  private final BeanProperty<T, ?> property;
  private final Matcher<?> expected;

  <V> Expectation(BeanProperty<T, V> property, Matcher<? super V> expected) {
    this.property = property;
    this.expected = expected;
  }

  public void describe(Description description) {
    Indentation.newLine(description)
        .appendText(property.getName())
        .appendText(": ")
        .appendDescriptionOf(expected);
  }

  public boolean test(T target, Description mismatchDescription) {
    Object value = property.get(target);
    if (expected.matches(value)) {
      return true;
    }

    expected.describeMismatch(
        value,
        Indentation.newLine(mismatchDescription)
            .appendText(property.getName())
            .appendText(": "));
    return false;
  }
}
