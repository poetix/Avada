package com.codepoetics.avada;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ObjectProperty<T, V> {

  public static <T, V> ObjectProperty<T, V> of(String name, Getter<T, V> getter) {
    return new ObjectProperty<>(name, getter);
  }

  private final String name;
  private final Getter<T, V> getter;

  private ObjectProperty(String name, Getter<T, V> getter) {
    this.name = name;
    this.getter = getter;
  }

  public Expectation<T> of(V expected) {
    return matching(Matchers.equalTo(expected));
  }

  public Expectation<T> matching(Matcher<? super V> matcher) {
    return new Expectation<>(this, matcher);
  }

  public String getName() {
    return name;
  }

  public V get(T target) {
    return getter.get(target);
  }

  public <V2> ObjectProperty<T, V2> then(final ObjectProperty<V, V2> next) {
    return new ObjectProperty<>(name + "." + next.name, new Getter<T, V2>() {
      @Override
      public V2 get(T target) {
        return next.get(getter.get(target));
      }
    });
  }

  <V2> ObjectProperty<T, Pair<V, V2>> pairWith(final ObjectProperty<T, V2> second) {
    return new ObjectProperty<T, Pair<V, V2>>(String.format("(%s, %s)", name, second.name), new Getter<T, Pair<V, V2>>() {
      @Override
      public Pair<V, V2> get(T target) {
        return Pair.of(getter.get(target), second.get(target));
      }
    });
  }

}
