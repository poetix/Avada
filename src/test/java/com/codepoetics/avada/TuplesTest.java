package com.codepoetics.avada;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.codepoetics.avada.Tuples.tuple;
import static com.codepoetics.avada.Utils.descriptionOf;
import static com.codepoetics.avada.Utils.mismatchDescriptionOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TuplesTest {

  @Test
  public void matchPair() {
    Matcher<Pair<String, Integer>> matcher = tuple(containsString("oo"), greaterThan(20));

    assertThat(Pair.of("Foo", 23), tuple("Foo", 23));
    assertThat(Pair.of("Foo", 23), matcher);
    assertThat(descriptionOf(matcher), equalTo("(a string containing \"oo\",a value greater than <20>)"));

    assertThat(mismatchDescriptionOf(matcher, Pair.of("Foo", 19)), equalTo("(\u2713,<19> was less than <20>)"));
  }

  @Test
  public void matchTriple() {
    Matcher<Pair<String, Pair<Boolean, Integer>>> matcher = tuple(containsString("ba"), equalTo(true), lessThan(5));

    assertThat(Pair.of("baz", Pair.of(true, 4)), tuple("baz", true, 4));
    assertThat(Pair.of("baz", Pair.of(true, 4)), matcher);
    assertThat(descriptionOf(matcher), equalTo("(a string containing \"ba\",<true>,a value less than <5>)"));

    assertThat(mismatchDescriptionOf(matcher, Pair.of("Foo", Pair.of(false, 3))), equalTo("(was \"Foo\",was <false>,\u2713)"));
  }

  @Test
  public void matchQuadruple() {
    Matcher<Pair<String, Pair<Boolean, Pair<Integer, String>>>> matcher = tuple(containsString("ba"), equalTo(true), lessThan(5), equalTo("xyzzy"));

    assertThat(Pair.of("baz", Pair.of(true, Pair.of(4, "xyzzy"))), tuple("baz", true, 4, "xyzzy"));
    assertThat(Pair.of("baz", Pair.of(true, Pair.of(4, "xyzzy"))), matcher);
    assertThat(descriptionOf(matcher), equalTo("(a string containing \"ba\",<true>,a value less than <5>,\"xyzzy\")"));

    assertThat(mismatchDescriptionOf(matcher, Pair.of("Foo", Pair.of(false, Pair.of(3, "xyzzy")))), equalTo("(was \"Foo\",was <false>,\u2713,\u2713)"));
  }
}
