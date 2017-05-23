// Casey Scott
// AID - 1705
// Persons.java

package com.fullsail.caseyscott.scottcasey_ce05;

public class Persons {

     String firstName;
     String lastName;

     String getFirstName(){
          return firstName;
     }

     String getLastName(){
          return lastName;
     }
     @Override
     public String toString() {
          return firstName+ " "+ lastName;
     }
}
