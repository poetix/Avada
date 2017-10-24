package com.codepoetics.avada;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.codepoetics.avada.Tuples.tuple;
import static com.codepoetics.avada.Tuples.tupleWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AnIterableTest {

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

  private static final ObjectProperty<Person, String> name = ObjectProperty.of("name", nameGetter);
  private static final ObjectProperty<Person, Integer> age = ObjectProperty.of("age", ageGetter);
  private static final ObjectProperty<Person, Address> address = ObjectProperty.of("address", addressGetter);
  private static final ObjectProperty<Address, String> postcode = ObjectProperty.of("postcode", postcodeGetter);
  private static final ObjectProperty<Person, String> addressPostcode = address.then(postcode);

  @Test
  public void testPropertiesOfMultiplePeople() {
    List<Person> people = Arrays.asList(
        new Person("Arthur Putey", 42, new Address("VB6 5UX")),
        new Person("Arthur 'Two Sheds' Jackson", 30, new Address("RA8 81T"))
    );

    assertThat(people, AnIterable.withItems(name, contains("Arthur Putey", "Arthur 'Two Sheds' Jackson")));

    ObjectProperty<Person, Pair<String, Integer>> nameAndAge = name.pairWith(age);
    assertThat(people.get(0), nameAndAge.matching(tuple("Arthur Putey", 42)));

    assertThat(people, AnIterable.withItems(
        tupleWith(name, age, addressPostcode),
        contains(
          tuple("Arthur Putey", 42, "VB6 5UX"),
          tuple(containsString("Two Sheds"), lessThan(40), equalTo("RA8 81T"))
    )));
  }
}
