package com.codepoetics.avada;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public final class BeanProperty<T, V> {

  public static <T, V> BeanProperty<T, V> of(String name, Getter<T, V> getter) {
    return new BeanProperty<>(name, getter);
  }

  private final String name;
  private final Getter<T, V> getter;

  private BeanProperty(String name, Getter<T, V> getter) {
    this.name = name;
    this.getter = getter;
  }

  public Expectation<T> of(V expected) {
    return new Expectation<>(this, Matchers.equalTo(expected));
  }

  public Expectation<T> that(Matcher<? super V> matcher) {
    return new Expectation<>(this, matcher);
  }

  public String getName() {
    return name;
  }

  public V get(T target) {
    return getter.get(target);
  }

  public <V2> BeanProperty<T, V2> then(final BeanProperty<V, V2> next) {
    return new BeanProperty<>(name + "." + next.name, new Getter<T, V2>() {
      @Override
      public V2 get(T target) {
        return next.get(getter.get(target));
      }
    });
  }
}
