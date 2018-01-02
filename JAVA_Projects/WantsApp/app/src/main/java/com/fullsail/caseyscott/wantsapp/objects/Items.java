// Casey Scott
// PAPVI - 1710
// Items.java

package com.fullsail.caseyscott.wantsapp.objects;


import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Items implements Serializable, DatabaseReference.CompletionListener {

    //Member Variables
    private String name;
    private String locations;
    private String description;
    private String price;
    private Integer priority_level;
    private String id;

    public Integer getPriority_level() {
        return priority_level;
    }


    public String getName() {
        return name;
    }

    public String getLocations() {
        return locations;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }


    //Default Constructor
    public Items(){}

    //Main Constructor
    public Items(String name, String locations, String description, String price, Integer priority_level, String id) {
        this.name = name;
        this.locations = locations;
        this.description = description;
        this.price = price;
        this.priority_level = priority_level;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if(databaseError != null){
            Log.e("ERROR", databaseError.getMessage());
        }
    }


}
