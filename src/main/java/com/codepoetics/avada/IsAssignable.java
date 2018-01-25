package com.codepoetics.avada;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class IsAssignable<T> extends TypeSafeDiagnosingMatcher<Class<?>> {

  public static <T> Matcher<Class<?>> to(final Class<T> expectedType) {
    return new IsAssignable(expectedType);
  }

  private final Class<T> expectedType;

  private IsAssignable(Class<T> expectedType) {
    this.expectedType = expectedType;
  }

  @Override
  protected boolean matchesSafely(Class<?> aClass, Description description) {
    if (expectedType.isAssignableFrom(aClass)) {
      return true;
    }
    description.appendText("was ").appendValue(aClass.getName());
    return false;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A class assignable to ").appendValue(expectedType.getName());
  }
}
