package com.codepoetics.avada;

import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

public final class Tuples {

  private Tuples() {
  }

  public static <T, A, B> ObjectProperty<T, Pair<A, B>> tupleWith(ObjectProperty<T, A> first, ObjectProperty<T, B> second) {
    return first.pairWith(second);
  }

  public static <T, A, B, C> ObjectProperty<T, Pair<A, Pair<B, C>>> tupleWith(ObjectProperty<T, A> first, ObjectProperty<T, B> second, ObjectProperty<T, C> third) {
    return first.pairWith(second.pairWith(third));
  }

  public static <T, A, B, C, D> ObjectProperty<T, Pair<A, Pair<B, Pair<C, D>>>> tupleWith(ObjectProperty<T, A> first, ObjectProperty<T, B> second, ObjectProperty<T, C> third, ObjectProperty<T, D> fourth) {
    return first.pairWith(second.pairWith(third.pairWith(fourth)));
  }

  public static <A, B> PairMatcher<A, B> tuple(A first, B second) {
    return tuple(equalTo(first), equalTo(second));
  }

  public static <A, B> PairMatcher<A, B> tuple(Matcher<? super A> first, Matcher<? super B> second) {
    return PairMatcher.of(first, second);
  }

  public static <A, B, C> PairMatcher<A, Pair<B, C>> tuple(A first, B second, C third) {
    return tuple(equalTo(first), equalTo(second), equalTo(third));
  }

  public static <A, B, C> PairMatcher<A, Pair<B, C>> tuple(Matcher<? super A> first, Matcher<? super B> second, Matcher<? super C> third) {
    return PairMatcher.of(first, PairMatcher.of(second, third));
  }

  public static <A, B, C, D> PairMatcher<A, Pair<B, Pair<C, D>>> tuple(A first, B second, C third, D fourth) {
    return tuple(equalTo(first), equalTo(second), equalTo(third), equalTo(fourth));
  }

  public static <A, B, C, D> PairMatcher<A, Pair<B, Pair<C, D>>> tuple(Matcher<? super A> first, Matcher<? super B> second, Matcher<? super C> third, Matcher<? super D> fourth) {
    return PairMatcher.of(first, PairMatcher.of(second, PairMatcher.of(third, fourth)));
  }

}
