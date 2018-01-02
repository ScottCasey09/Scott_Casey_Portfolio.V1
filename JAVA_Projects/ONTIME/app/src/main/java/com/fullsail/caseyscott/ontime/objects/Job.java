// Casey Scott
// FINAL PROJECT - 1712
// Job.java

package com.fullsail.caseyscott.ontime.objects;


import java.io.Serializable;

public class Job implements Serializable{

    private String title;
    private String location_lat_long;
    private String notes;
    private String dateCreated;
    private String key;

    public String getLocation_lat_long() {
        return location_lat_long;
    }

    public String getAddress() {
        return address;
    }

//    public void setLocation_lat_long(String location_lat_long) {
//
//        this.location_lat_long = location_lat_long;
//    }

//    public void setAddress(String address) {
//        this.address = address;
//    }

    private String address;

    public Job(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDateCreated() {
        return dateCreated;
    }

//    public void setDateCreated(String dateCreated) {
//        this.dateCreated = dateCreated;
//    }

    public Job(String title, String address, String location_lat_long, String notes, String dateCreated, String key) {

        this.title = title;
        this.location_lat_long= location_lat_long;
        this.notes = notes;
        this.dateCreated = dateCreated;
        this.address = address;
        this.key = key;
    }

    public Job(String title, String address, String notes, String dateCreated ) {

        this.title = title;
        this.notes = notes;
        this.dateCreated = dateCreated;
        this.address = address;
    }

    public String getKey() {
        return key;
    }
}
