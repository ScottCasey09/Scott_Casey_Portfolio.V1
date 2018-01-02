// Casey Scott
// PAPVI - 1710
// User.java

package com.fullsail.caseyscott.wantsapp.objects;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {

    //Member variables
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String phoneNumber;
    private String id;
    private HashMap<String,User> contacts;
    private HashMap<String, HashMap<String, Items>> lists;
    private String searchByEmail;
    private String searchByUsername;
    private String searchByFullName;

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstName) {

        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //Default
    public User(){

    }

    //Main Constructor
    public User(String firstname, String lastname, String username, String phoneNumber, String email, String id, HashMap<String,User> contacts, HashMap<String, HashMap<String,Items>> lists, String searchByEmail, String searchByUsername, String searchByFullName) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.id = id;
        this.contacts = contacts;
        this.lists = lists;
        this.searchByEmail = searchByEmail;
        this.searchByUsername = searchByUsername;
        this.searchByFullName = searchByFullName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return firstname +" "+ lastname;
    }

    public HashMap<String, User> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String, User> contacts) {
        this.contacts = contacts;
    }

    public HashMap<String, HashMap<String, Items>> getItems() {
        return lists;
    }

    public void setItems(HashMap<String, HashMap<String,Items>> items) {
        this.lists = lists;
    }

    public String getSearchByEmail() {
        return searchByEmail;
    }

    public void setSearchByEmail(String searchByEmail) {
        this.searchByEmail = searchByEmail;
    }

    public String getSearchByUsername() {
        return searchByUsername;
    }

    public void setSearchByUsername(String searchByUsername) {
        this.searchByUsername = searchByUsername;
    }

    public String getSearchByFullName() {
        return searchByFullName;
    }

    public void setSearchByFullName(String searchByFullName) {
        this.searchByFullName = searchByFullName;
    }
}
