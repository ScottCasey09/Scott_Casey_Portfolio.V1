// Casey Scott
// AID - 1705
// Contact.JAVA

package com.fullsail.caseyscott.scottcasey_ce06;

import java.io.Serializable;

public class Contact implements Serializable{

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

   public String getPhoneNUmber() {
        return phoneNUmber;
    }

    private final String firstName;
    private final String lastName;
    private final String phoneNUmber;

    public Contact(String firstName, String lastName, String phoneNUmber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNUmber = phoneNUmber;

    }

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }
}
