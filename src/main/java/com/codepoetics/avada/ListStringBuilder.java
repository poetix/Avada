package com.codepoetics.avada;

public class ListStringBuilder {

  public static ListStringBuilder forTuple() {
    return new ListStringBuilder("(", ",", ")");
  }

  private final String prefix;
  private final String separator;
  private final String suffix;

  private final StringBuilder stringBuilder = new StringBuilder();

  public ListStringBuilder(String prefix, String separator, String suffix) {
    this.prefix = prefix;
    this.separator = separator;
    this.suffix = suffix;
  }

  public ListStringBuilder start() {
    stringBuilder.append(prefix);
    return this;
  }

  public ListStringBuilder append(Object value) {
    if (stringBuilder.length() > 1) {
      stringBuilder.append(separator);
    }
    stringBuilder.append(value);
    return this;
  }

  public ListStringBuilder end() {
    stringBuilder.append(suffix);
    return this;
  }

  @Override
  public String toString() {
    return stringBuilder.toString();
  }


}
