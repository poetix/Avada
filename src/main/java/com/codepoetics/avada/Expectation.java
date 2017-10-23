package com.codepoetics.avada;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class Expectation<T> extends TypeSafeDiagnosingMatcher<T> {

  private final ObjectProperty<T, ?> property;
  private final Matcher<?> expected;

  <V> Expectation(ObjectProperty<T, V> property, Matcher<? super V> expected) {
    this.property = property;
    this.expected = expected;
  }

  public String getPropertyName() {
    return property.getName();
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

  @Override
  protected boolean matchesSafely(T t, Description description) {
    return AnObject.with(this).matchesSafely(t, description);
  }

  @Override
  public void describeTo(Description description) {
    AnObject.with(this).describeTo(description);
  }
}
