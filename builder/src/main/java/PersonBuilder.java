import org.gradle.Person;

public class PersonBuilder {
    private String name;

    private PersonBuilder() {
    }

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }

    public Person build() {
        Person object = new Person();
        object.setName(name);
        return object;
    }

    public PersonBuilder name(String name) {
        this.name = name;
        return this;
    }
}