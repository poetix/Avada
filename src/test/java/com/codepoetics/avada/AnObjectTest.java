package com.codepoetics.avada;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AnObjectTest {

  public static final class Outer {
    public final String outerValue;
    public final Inner inner;

    public Outer(String outerValue, Inner inner) {
      this.outerValue = outerValue;
      this.inner = inner;
    }
  }

  public static final class Inner {
    public final String innerValue;

    public Inner(String innerValue) {
      this.innerValue = innerValue;
    }
  }

  public static final ObjectProperty<Outer, String> outerValue = ObjectProperty.of("outerValue", new Getter<Outer, String>() {
    @Override
    public String get(Outer target) {
      return target.outerValue;
    }
  });

  public static final ObjectProperty<Outer, Inner> inner = ObjectProperty.of("inner", new Getter<Outer, Inner>() {
    @Override
    public Inner get(Outer target) {
      return target.inner;
    }
  });

  public static final ObjectProperty<Inner, String> innerValue = ObjectProperty.of("innerValue", new Getter<Inner, String>() {
    @Override
    public String get(Inner target) {
      return target.innerValue;
    }
  });

  @Test
  public void indentsDescriptions() {
    Outer outer = new Outer("The outer value", new Inner("The inner value"));

    Matcher<Outer> matcher = AnObject.with(
        outerValue.of("The outer value"),
        inner.matching(AnObject.with(innerValue.of("The inner value, only not"))));

    assertThat(descriptionOf(matcher), equalTo(
        "An object with:\n" +
            "\touterValue: \"The outer value\"\n" +
            "\tinner: An object with:\n" +
              "\t\tinnerValue: \"The inner value, only not\""));

    assertThat(mismatchDescriptionOf(matcher, outer), equalTo(
            "\n\tinner: \n" +
            "\t\tinnerValue: was \"The inner value\""
    ));
  }

  @Test
  public void inheritanceAndOverriding() {
    AnObject<Outer> baseSpec = AnObject.with(outerValue.of("The outer value"));

    AnObject<Outer> inheritorA = baseSpec.and(inner.matching(AnObject.with(innerValue.of("The inner value"))));
    AnObject<Outer> inheritorB = baseSpec.and(
        inner.matching(AnObject.with(innerValue.of("A different inner value"))));
    AnObject<Outer> inheritorC = baseSpec.and(
        outerValue.of("A different outer value"),
        inner.matching(AnObject.with(innerValue.of("A different inner value"))));

    assertThat(descriptionOf(baseSpec), equalTo(
        "An object with:\n" +
            "\touterValue: \"The outer value\""
    ));

    assertThat(descriptionOf(inheritorA), equalTo(
        "An object with:\n" +
            "\touterValue: \"The outer value\"\n" +
            "\tinner: An object with:\n" +
            "\t\tinnerValue: \"The inner value\""
    ));

    assertThat(descriptionOf(inheritorB), equalTo(
        "An object with:\n" +
            "\touterValue: \"The outer value\"\n" +
            "\tinner: An object with:\n" +
            "\t\tinnerValue: \"A different inner value\""
    ));

    assertThat(descriptionOf(inheritorC), equalTo(
        "An object with:\n" +
            "\touterValue: \"A different outer value\"\n" +
            "\tinner: An object with:\n" +
            "\t\tinnerValue: \"A different inner value\""
    ));
  }

  public static final class Shed {
  }

  public static final class Address {
    private final String postcode;

    public Address(String postcode) {
      this.postcode = postcode;
    }

    public String getPostcode() {
      return postcode;
    }
  }

  public static final class Person {
    private final String name;
    private final int age;
    private final Collection<Shed> sheds = new ArrayList<>();
    private final Address address;

    public Person(String name, int age, Address address) {
      this.name = name;
      this.age = age;
      this.address = address;
    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }

    public Collection<Shed> getSheds() {
      return sheds;
    }

    public Address getAddress() {
      return address;
    }
  }

  private static final Getter<Person, String> nameGetter = new Getter<Person, String>() {
    @Override
    public String get(Person target) {
      return target.getName();
    }
  };

  private static final Getter<Person, Integer> ageGetter = new Getter<Person, Integer>() {
    @Override
    public Integer get(Person target) {
      return target.getAge();
    }
  };

  private static final Getter<Person, Integer> shedCountGetter = new Getter<Person, Integer>() {
    @Override
    public Integer get(Person target) {
      return target.getSheds().size();
    }
  };

  private static final Getter<Person, Address> addressGetter = new Getter<Person, Address>() {
    @Override
    public Address get(Person target) {
      return target.getAddress();
    }
  };

  private static final Getter<Address, String> postcodeGetter = new Getter<Address, String>() {
    @Override
    public String get(Address target) {
      return target.getPostcode();
    }
  };

  @Test
  public void readmeExample1() {
    Person person = new Person("Arthur Putey", 42, new Address("VB6 5UX"));

    assertThat(
        person,
        AnObject.with("name", nameGetter, "Arthur Putey")
            .and("age", ageGetter, greaterThanOrEqualTo(42)));
  }

  @Test
  public void readmeExample2() {
    Person person = new Person("Arthur Putey", 42, new Address("VB6 5UX"));

    ObjectProperty<Person, String> name = ObjectProperty.of("name", nameGetter);
    ObjectProperty<Person, Integer> age = ObjectProperty.of("age", ageGetter);

    assertThat(person, AnObject.with(
        name.of("Arthur Putey"),
        age.matching(greaterThanOrEqualTo(42))));
  }

  @Test
  public void readMeExample3() {
    Person person = new Person("Arthur 'Two Sheds' Jackson", 42, new Address("VB6 5UX"));
    person.getSheds().add(new Shed());
    person.getSheds().add(new Shed());

    ObjectProperty<Person, Integer> numberOfSheds = ObjectProperty.of("number of sheds", shedCountGetter);

    assertThat(person, AnObject.with(numberOfSheds.of(2)));
    assertThat(person, numberOfSheds.of(2));
  }

  @Test
  public void readMeExample4() {
    Person person = new Person("Arthur 'Two Sheds' Jackson", 42, new Address("VB6 5UX"));

    ObjectProperty<Person, Address> address = ObjectProperty.of("address", addressGetter);
    ObjectProperty<Address, String> postcode = ObjectProperty.of("postcode", postcodeGetter);
    ObjectProperty<Person, String> addressPostcode = address.then(postcode);

    assertThat(person, addressPostcode.of("VB6 5UX"));
  }

  @Test
  public void readMeExample5() {
    ObjectProperty<Person, String> name = ObjectProperty.of("name", nameGetter);
    ObjectProperty<Person, Integer> age = ObjectProperty.of("age", ageGetter);

    AnObject<Person> baseSpec = AnObject.with(name.of("Arthur Putey"));
    AnObject<Person> oldArthur = baseSpec.and(age.matching(greaterThanOrEqualTo(40)));
    AnObject<Person> youngArthur = baseSpec.and(age.matching(lessThan(40)));

    Person oldPerson = new Person("Arthur Putey", 42, new Address("VB6 5UX"));
    Person youngPerson = new Person("Arthur Putey", 39, new Address("VB6 5UX"));

    assertThat(oldPerson, oldArthur);   // Will match Arthur Putey aged 40+
    assertThat(youngPerson, youngArthur); // Will match Arthur Putey in the prime of youth
    assertThat(oldPerson, baseSpec);    // Will match Arthur Putey irrespective of age
    assertThat(youngPerson, baseSpec);    // Will match Arthur Putey irrespective of age
  }

  private String descriptionOf(Matcher<?> matcher) {
    StringDescription description = new StringDescription();
    matcher.describeTo(description);
    return description.toString();
  }

  private <T> String mismatchDescriptionOf(Matcher<? super T> matcher, T value) {
    StringDescription description = new StringDescription();
    matcher.describeMismatch(value, description);
    return description.toString();
  }
}
