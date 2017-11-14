// Casey Scott
// AID - 1705
// Person.JAVA

package com.fullsail.caseyscott.scottcasey_ce07.Objects;

import java.io.Serializable;

public class Person implements Serializable {

    private final String firstName;
    private final String lastName;
    private final String age;

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getAge() {
        return age;
    }


    public Person(String firstName, String lastName, String age) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public String toString() {
        return firstName + " "+ lastName;
    }
}
