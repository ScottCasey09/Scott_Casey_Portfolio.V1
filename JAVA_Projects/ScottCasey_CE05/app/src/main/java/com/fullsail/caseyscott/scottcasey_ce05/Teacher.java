// Casey Scott
// AID - 1705
// Teacher.JAVA

package com.fullsail.caseyscott.scottcasey_ce05;

public class Teacher extends Persons{

    private final String tClass;

    public Teacher(String firstname, String lastname, String cls) {
        firstName = firstname;
        lastName = lastname;
        tClass = cls;
    }

    public String gettClass() {
        return tClass;
    }


}
