package com.codepoetics.avada;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pair<A, B> {

  public static <A, B> Pair<A, B> of(A first, B second) {
    return new Pair<>(first, second);
  }

  private final A first;
  private final B second;

  private Pair(A first, B second) {
    this.first = first;
    this.second = second;
  }

  public A getFirst() {
    return first;
  }

  public B getSecond() {
    return second;
  }

  public List<Object> asList() {
    List<Object> items = new ArrayList<>();
    if (first instanceof Pair) {
      items.addAll(((Pair) first).asList());
    } else {
      items.add(first);
    }

    if (second instanceof Pair) {
      items.addAll(((Pair) second).asList());
    } else {
      items.add(second);
    }

    return items;
  }

  @Override
  public boolean equals(Object o) {
    return this == o ||
        (o instanceof Pair
            && asList().equals(((Pair) o).asList()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    boolean first = true;
    for (Object o: asList()) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      sb.append(o.toString());
    }
    sb.append(")");
    return sb.toString();
  }
}
