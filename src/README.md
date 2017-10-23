# AVADA

A "magic matchers" library that works with Hamcrest, created for the slightly unusual case where we want to write Java 8 but convert to JDK7 bytecode using Retrolambda.

Accordingly, this is intended to be used with a language that supports method references, but is written in a language that doesn't.

Assuming that's your situation, here's how it works:

```java
assertThat(
  person,
  AnObject.with("name", Person::getName, "Arthur Putey")
    .and("age", Person::getAge, greaterThanOrEqualTo(42));
```

If you're going to write lots of tests referencing the same property, you can pull it out into a re-usable object that gives you a slightly neater syntax:

```java
ObjectProperty<Person, String> name = ObjectProperty.of("name", Person::getName);
ObjectProperty<Person, Integer> age = ObjectProperty.of("age", Person::getAge);

assertThat(person, AnObject.with(
  name.of("Arthur Putey"),
  age.matching(greaterThanOrEqualTo(42))));
```

The lambda supplied as a "getter" doesn't have to be a method reference - it can reflect any property of the object we want to make assertions about:

```java
ObjectProperty<Person, Integer> numberOfSheds = ObjectProperty.of("number of sheds", person -> person.getSheds().size());

assertThat(person, AnObject.with(numberOfSheds.of(2)));
```

As a shorthand for the case where you're just testing a single property, you can instead write:

```java
assertThat(person, numberOfSheds.of(2));
```

There's some rudimentary lens-like behaviour too, because why not:

```java
ObjectProperty<Person, Address> address = ObjectProperty.of("address", Person::getAddress);
ObjectProperty<Address, String> postcode = ObjectProperty.of("postcode", Address:getPostcode);
ObjectProperty<Person, String> addressPostcode = address.then(postcode);

assertThat(person, addressPostcode.of("VB6 5UX"));
```

Finally, you can extend an existing matcher with further properties, getting a new matcher that leaves the original unchanged:

```java
AnObject<Person> baseSpec = AnObject.with(name.of("Arthur Putey"));
AnObject<Person> oldArthur = baseSpec.and(age.matching(greaterThanOrEqualTo(40)));
AnObject<Person> youngArthur = baseSpec.and(age.matching(lessThan(40)));

assertThat(person, oldArthur);   // Will match Arthur Putey aged 40+
assertThat(person, youngArthur); // Will match Arthur Putey in the prime of youth
assertThat(person, baseSpec);    // Will match Arthur Putey irrespective of age
```