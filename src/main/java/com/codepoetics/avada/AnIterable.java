package com.codepoetics.avada;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;

public class AnIterable<T, V> extends TypeSafeDiagnosingMatcher<Iterable<T>> {

  public static <T, V> AnIterable<T, V> withItems(String name, Getter<T, V> getter, Matcher<Iterable<? extends V>> matcher) {
    return withItems(ObjectProperty.of(name, getter), matcher);
  }

  public static <T, V> AnIterable<T, V> withItems(ObjectProperty<T, V> itemProperty, Matcher<Iterable<? extends V>> matcher) {
    return new AnIterable<>(itemProperty, matcher);
  }

  private final ObjectProperty<T, V> itemProperty;
  private final Matcher<Iterable<? extends V>> matcher;

  private AnIterable(ObjectProperty<T, V> itemProperty, Matcher<Iterable<? extends V>> matcher) {
    this.itemProperty = itemProperty;
    this.matcher = matcher;
  }

  @Override
  protected boolean matchesSafely(Iterable<T> iterable, Description mismatch) {
    List<V> projectedItems = new ArrayList<>();
    for (T item : iterable) {
      projectedItems.add(itemProperty.get(item));
    }
    if (matcher.matches(projectedItems)) {
      return true;
    }
    matcher.describeMismatch(projectedItems, mismatch);
    return false;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A collection of items with ").appendText(itemProperty.getName()).appendText(" matching ").appendDescriptionOf(matcher);
  }
}
