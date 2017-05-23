// Casey Scott
// AID - 1705
// Student.JAVA

package com.fullsail.caseyscott.scottcasey_ce05;


public class Student extends Persons{


    private final String id;

    public String getId() {
        return id;
    }



    public Student(String fName, String lName, String id) {
        firstName = fName;
        lastName = lName;
        this.id = id;
    }
    public String toString() {
        return firstName + " " + lastName;
    }

}