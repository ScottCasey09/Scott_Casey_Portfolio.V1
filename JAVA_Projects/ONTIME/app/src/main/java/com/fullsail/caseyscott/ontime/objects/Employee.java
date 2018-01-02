// Casey Scott
// FINAL PROJECT - 1712
// Employee.java

package com.fullsail.caseyscott.ontime.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Employee implements Serializable{

    public Employee(){
    }

    private String email;

    public Employee(String email, String firstname, String lastname, String password, String id, String company, String account_type, long dateCreated, String imageUrl, HashMap<String, Job> jobs, HashMap<String, Employee> employees, String adminID, HashMap<String, Day> days, String dob, Integer defaultHour) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.id = id;
        this.company = company;
        this.account_type = account_type;
        this.dateCreated.set(dateCreated);
        this.imageUrl = imageUrl;
        this.jobs = jobs;
        this.employees = employees;
        this.adminID = adminID;
        this.days = days;
        AtomicReference<String> dob1 = new AtomicReference<>(dob);
        this.defaultHour = defaultHour;
    }

    private String firstname;
    private String lastname;
    private String password;
    private String id;

    private String company;
    private String account_type;
    private final AtomicLong dateCreated = new AtomicLong();
    private String imageUrl;
    private Integer defaultHour;


    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public HashMap<String,Job> getJobs() {
        return jobs;
    }

    public void setJobs(HashMap<String,Job> jobs) {
        this.jobs = jobs;
    }

    private HashMap<String,Job> jobs;

    public HashMap<String,Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(HashMap<String,Employee> employees) {
        this.employees = employees;
    }

    private HashMap<String,Employee> employees;

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    private String adminID;

    public HashMap<String,Day> getDays() {
        return days;
    }

    public void setDays(HashMap<String,Day> days) {
        this.days = days;
    }

    private HashMap<String,Day> days;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getDob() {
//        return dob;
//    }
//
//    public void setDob(String dob) {
//        this.dob = dob;
//    }


    @Override
    public String toString() {
        return firstname + " " + lastname;
    }

    public String getCompany() {
        return company;
    }

//    public void setCompany(String company) {
//        this.company = company;
//    }

    public String getAdminID() {
        return adminID;
    }

    public String getEmployeeType() {
        return account_type;
    }


//    public long getDateCreated() {
//        return dateCreated;
//    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getDefaultHour() {
        return defaultHour;
    }

    public void setDefaultHour(Integer defaultHour) {
        this.defaultHour = defaultHour;
    }
}
