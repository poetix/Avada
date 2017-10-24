package com.codepoetics.avada;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public final class Utils {

  private Utils() {
  }

  static String descriptionOf(Matcher<?> matcher) {
    StringDescription description = new StringDescription();
    matcher.describeTo(description);
    return description.toString();
  }

  static <T> String mismatchDescriptionOf(Matcher<? super T> matcher, T value) {
    StringDescription description = new StringDescription();
    matcher.describeMismatch(value, description);
    return description.toString();
  }
}
