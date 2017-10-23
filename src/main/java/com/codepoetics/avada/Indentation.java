package com.codepoetics.avada;

import org.hamcrest.Description;

public final class Indentation {
  private static final ThreadLocal<Integer> indentationLevel = new ThreadLocal<>();

  private static int getLevel() {
    Integer current = indentationLevel.get();
    return current == null ? 0 : current;
  }

  public static void indent() {
    indentationLevel.set(getLevel() + 1);
  }

  public static void outdent() {
    indentationLevel.set(getLevel() - 1);
  }

  public static Description newLine(Description description) {
    description.appendText("\n");
    for (int i = 0; i < getLevel(); i++) {
      description.appendText("\t");
    }
    return description;
  }

}
