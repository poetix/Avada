package com.codepoetics.avada;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PairMatcher<A, B> extends TypeSafeDiagnosingMatcher<Pair<A, B>> {

  public static <A, B> PairMatcher<A, B> of(Matcher<? super A> first, Matcher<? super B> second) {
    return new PairMatcher<>(first, second);
  }

  private final Matcher<? super A> first;
  private final Matcher<? super B> second;

  private PairMatcher(Matcher<? super A> first, Matcher<? super B> second) {
    this.first = first;
    this.second = second;
  }

  public List<Matcher<?>> asList() {
    List<Matcher<?>> items = new ArrayList<>();
    if (first instanceof PairMatcher) {
      items.addAll(((PairMatcher<?, ?>) first).asList());
    } else {
      items.add(first);
    }

    if (second instanceof PairMatcher) {
      items.addAll(((PairMatcher<?, ?>) second).asList());
    } else {
      items.add(second);
    }

    return items;
  }

  @Override
  public boolean equals(Object o) {
    return this == o ||
        (o instanceof PairMatcher
            && asList().equals(((PairMatcher) o).asList()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  protected boolean matchesSafely(Pair<A, B> pair, Description mismatch) {
    List<Object> items = pair.asList();
    List<Matcher<?>> matchers = asList();
    if (items.size() != matchers.size()) {
      mismatch.appendText("Matcher matches ")
          .appendValue(matchers.size())
          .appendText(" items, but ")
          .appendValue(items.size())
          .appendText(" in value ")
          .appendValueList("(", ", ", ")", items);
      return false;
    }

    boolean matches = true;

    boolean first = true;
    mismatch.appendText("(");
    for (int i = 0; i < items.size(); i++) {
      if (first) {
        first = false;
      } else {
        mismatch.appendText(",");
      }
      Matcher<?> matcher = matchers.get(i);
      Object item = items.get(i);
      if (matcher.matches(item)) {
        mismatch.appendText("\u2713");
        continue;
      }
      matches = false;
      matcher.describeMismatch(item, mismatch);
    }
    mismatch.appendText(")");

    return matches;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("(");
    boolean first = true;
    for (Matcher<?> matcher : asList()) {
      if (first) {
        first = false;
      } else {
        description.appendText(",");
      }
      description.appendDescriptionOf(matcher);
    }
    description.appendText(")");
  }
}
