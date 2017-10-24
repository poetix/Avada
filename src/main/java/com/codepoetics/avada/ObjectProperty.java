package com.codepoetics.avada;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.*;

public final class ObjectProperty<T, V> {

  public static <T, V> ObjectProperty<T, V> of(String name, Getter<T, V> getter) {
    return new ObjectProperty<>(Collections.singletonList(name), getter);
  }

  private final List<String> names;
  private final Getter<T, V> getter;

  private ObjectProperty(List<String> names, Getter<T, V> getter) {
    this.names = names;
    this.getter = getter;
  }

  public Expectation<T> of(V expected) {
    return matching(Matchers.equalTo(expected));
  }

  public Expectation<T> matching(Matcher<? super V> matcher) {
    return new Expectation<>(this, matcher);
  }

  public String getName() {
    return names.size() == 1
        ? names.get(0)
        : nameList(names);
  }

  public List<String> getInnerNames() {
    return names;
  }

  private String nameList(List<String> names) {
    ListStringBuilder sb = ListStringBuilder.forTuple().start();

    for (String name : names) {
      sb.append(name);
    }

    return sb.end().toString();
  }

  public V get(T target) {
    return getter.get(target);
  }

  public <V2> ObjectProperty<T, V2> then(final ObjectProperty<V, V2> next) {
    return new ObjectProperty<>(Collections.singletonList(getName() + "/" + next.getName()), new Getter<T, V2>() {
      @Override
      public V2 get(T target) {
        return next.get(getter.get(target));
      }
    });
  }

  public <V2> ObjectProperty<T, Pair<V, V2>> pairWith(final ObjectProperty<T, V2> second) {
    List<String> newNames = new ArrayList<>();
    newNames.addAll(names);
    newNames.addAll(second.names);
    return new ObjectProperty<>(newNames, new Getter<T, Pair<V, V2>>() {
      @Override
      public Pair<V, V2> get(T target) {
        return Pair.of(getter.get(target), second.get(target));
      }
    });
  }

}
